import socket
import os
import json
import logging

from Analyzer import Analyzer
from datetime import datetime

from YaralyzeServerDB import YaralyzeServerDB

BASE_PATH = os.path.abspath(os.getcwd())
CLIENT_SAMPLES_PATH = BASE_PATH + "/Analysis_samples"
LOGS_DIRECTORY_PATH = BASE_PATH + "/Logs"
ANALYSIS_TOOLS_PATH = BASE_PATH + "/Analysis_tools"
DATABASE_DIRECTORY = BASE_PATH + "/Database"

HASHES_DIRECTORY_PATH = ANALYSIS_TOOLS_PATH + "/MalwareHashes"
YARA_RULES_DIRECTORY_PATH =  ANALYSIS_TOOLS_PATH + "/YaraRules"

PARTIAL_MALWARE_HASHES_PATH = HASHES_DIRECTORY_PATH + "/Partial_hashes.txt"

EXTENSION = ".apk"

#Comunicacion con el cliente
STATIC_ANALYSIS_QUERY = 1;
HASH_ANALYSIS_QUERY = 2;
UPDATE_DB_QUERY = 3;

BUFFERSIZE = 16384
IP = "0.0.0.0"
PORT = 3389

class Server:
    def __init__(self) -> None:
        self.__host = self.__getCurrentAddress()
        self.__serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__createDirectories()
        self.__setupLogConfig()

        self.__db = YaralyzeServerDB()

        self.__analyzer = Analyzer()
        self.__startServer()
        self.__waitForConnections()


    # Métodos relacionados con la configuración del servidor
    # ------------------------------------------------------

    def __getCurrentAddress(self) -> str:
        tempSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        tempSocket.connect((socket.gethostbyname("www.google.com"), 80))
        local_ip = tempSocket.getsockname()[0]
        tempSocket.close()
        return local_ip
    
    def __createDirectories(self) -> None:
        if not os.path.isdir(LOGS_DIRECTORY_PATH):
            try:
                os.makedirs(LOGS_DIRECTORY_PATH)
            except:
                print("[!][!][!] - Error al crear el el directorio de logs")

        if not os.path.isdir(DATABASE_DIRECTORY):
            try:
                os.makedirs(DATABASE_DIRECTORY)
            except:
                self.__logger.error("[!][!][!] - Error al crear el el directorio de la base de datos")

        if not os.path.isdir(ANALYSIS_TOOLS_PATH):
            try:
                os.makedirs(ANALYSIS_TOOLS_PATH)
            except:
                self.__logger.error("[!][!][!] - Error al crear el el directorio de herramientas")

        if not os.path.isdir(YARA_RULES_DIRECTORY_PATH):
            try:
                os.makedirs(YARA_RULES_DIRECTORY_PATH)
            except:
                self.__logger.error("[!][!][!] - Error al crear el el directorio de reglas Yara")

        if not os.path.isdir(HASHES_DIRECTORY_PATH):
            try:
                os.makedirs(HASHES_DIRECTORY_PATH)
            except:
                self.__logger.error("[!][!][!] - Error al crear el el directorio de hashes")

        if not os.path.isdir(CLIENT_SAMPLES_PATH):
            try:
                os.makedirs(CLIENT_SAMPLES_PATH)
            except:
                self.__logger.error("[!][!][!] - Error al crear el el directorio de muestras")
                

    def __setupLogConfig(self) -> None:
        logfilePath = LOGS_DIRECTORY_PATH + "/" + str(datetime.now().strftime("%d")) + "_server.log"
        if(not os.path.exists(logfilePath)):
            open(logfilePath, 'a').close()

        handler = logging.FileHandler(logfilePath)        
        handler.setFormatter(logging.Formatter('%(asctime)s: %(levelname)s - %(message)s', "%H:%M:%S"))

        self.__logger = logging.getLogger("serverLogger")
        self.__logger.setLevel(logging.DEBUG)
        self.__logger.addHandler(handler)
        

    # Métodos relacionados con la gestión de requests
    # -----------------------------------------------

    def __startServer(self) -> None:
        try:
            try:
                self.__logger.info("[!][!] - Iniciando servicio en " + IP + ":" + str(PORT))
                self.__serverSocket.bind((IP, PORT))
            except: 
                self.__logger.warning("[!][!] - Ejecutando el servidor en modo local")
                self.__logger.info("[!][!] - Iniciando servicio en " + str(self.__host) + ":" + str(PORT))
                self.__serverSocket.bind((self.__host, PORT))
        except socket.error as e:
            self.__logger.error("[!][!][!] - Error al iniciar el servicio: " + str(e))
    
    def __waitForConnections(self) -> None:
        while(1):
            self.__serverSocket.listen(1)

            print("Servidor esperando peticiones en: " + str(self.__host) + ":" + str(PORT))

            clientConnection, _ = self.__serverSocket.accept()  
            try:
                self.__proccessClientConnection(clientConnection)
            except Exception as e:
                self.__logger.error("[!][!][!] - Error al procesar la petición de un cliente: " + str(e))
                clientConnection.close()

    
    def __proccessClientConnection(self, clientConnection) -> None:
        request = self.__receiveRequestType(clientConnection)

        if request == STATIC_ANALYSIS_QUERY:
            self.__logger.info("[!] - Petición de análisis estático recibida")
            self.__staticAnalysis(clientConnection)
            self.__logger.info("[!] - Análisis estático terminado")
        elif request == HASH_ANALYSIS_QUERY:
            self.__logger.info("[!] - Petición de análisis del hash recibida")
            self.__hashAnalysis(clientConnection)
            self.__logger.info("[!] - Análisis del hash terminado")
        elif request == UPDATE_DB_QUERY:
            self.__logger.info("[!] - Petición de actualización de la base de datos recibida")
            self.__sendPartialHashesDB(clientConnection)
            self.__logger.info("[!] - Actualización de la base de datos terminada")
        

    def __receiveRequestType(self, clientConnection):
        requestType = clientConnection.recv(1024)
        return int.from_bytes(requestType, "big")


    # Métodos relacionados con peticiones de análisis del hash
    # --------------------------------------------------------

    def __hashAnalysis(self, clientConnection) -> None:
        clientHash = json.loads(self.__receiveClientSampleHeader(clientConnection))["hash"]
        print(clientHash)
        malwareFound = self.__db.getHashCoincidence(clientHash)
        analysisOutcome = {}
        analysisOutcome["detected"] = malwareFound

        self.__sendAnalysisOutcome(clientConnection, analysisOutcome)

    
    # Métodos relacionados con peticiones de análisis estático
    # --------------------------------------------------------

    def __staticAnalysis(self, clientConnection) -> None:
        clientSamplePath = self.receiveClientSample(clientConnection)
        self.__sendAnalysisOutcome(clientConnection, self.__startStaticAnalysis(clientSamplePath))
        self.__removeClientSample(clientSamplePath)
        self.__analyzer.cleanAnalysisOutcome()

    def receiveClientSample(self, clientConnection) -> str:
        header = json.loads(self.__receiveClientSampleHeader(clientConnection))
        clientSamplePath = self.__receiveSampleFile(clientConnection, header)
        return clientSamplePath

    def __receiveSampleFile(self, clientConnection, header) -> str:
        sampleName, sampleSize = header["name"], int(header["size"])

        self.__logger.info("Recibiendo fichero " + sampleName)
        with open(CLIENT_SAMPLES_PATH + "/" + sampleName + EXTENSION, "wb") as sampleFile:
            while True:
                bytes_readed = clientConnection.recv(min(BUFFERSIZE, sampleSize))
                if not bytes_readed:
                    sampleFile.close()
                    break

                sampleFile.write(bytes_readed)
                sampleSize = sampleSize - len(bytes_readed) 
        
        self.__logger.info("Fichero " + sampleName + " recibido")
        return CLIENT_SAMPLES_PATH + "/" + sampleName
    
    def __startStaticAnalysis(self, clientSamplePath) -> dict:
        self.__logger.info("[!] - Iniciando análisis estático sobre " + clientSamplePath + EXTENSION)
        return self.__analyzer.executeStaticAnalysis(clientSamplePath + EXTENSION)


    # Métodos comunes a todos los tipos de análisis
    # ---------------------------------------------
    
    def __receiveClientSampleHeader(self, clientConnection) -> str:
        try:
            header = clientConnection.recv(1024).decode("utf-8")[2:]
            return header
        except:
            raise OSError("Error al procesar la cabecera del archivo")

    def __sendAnalysisOutcome(self, clientConnection, analysisOutcome):
        self.__logger.info("Enviando resultado del análisis")
            
        analysisOutcome = json.dumps(analysisOutcome)
        clientConnection.sendall(bytes(analysisOutcome + "\n", encoding="utf-8"))

    def __removeClientSample(self, clientSamplePath):
        os.remove(clientSamplePath + EXTENSION)


    # Métodos relacionados con peticiones de actualización de la base de datos
    # ------------------------------------------------------------------------
    
    def __sendPartialHashesDB(self, clientConnection):
        if os.path.isfile(PARTIAL_MALWARE_HASHES_PATH):
            malwareHashesFile = open(PARTIAL_MALWARE_HASHES_PATH, "r")
            lines = malwareHashesFile.readlines()

            for line in lines:
                clientConnection.sendall(bytes(line, encoding="utf-8"))

            clientConnection.close()
        else:
            self.__logger.error("[!][!][!] - Error, archivo partial hashes no encontrado")
            clientConnection.close()

if __name__ == "__main__":
    try:
        Server()
    except KeyboardInterrupt:
        print("\nCerrando servidor...")
        exit(1)
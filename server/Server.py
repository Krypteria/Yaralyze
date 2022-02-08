from http import client
import re
import socket
import os
import json
import logging

from Analyzer import Analyzer
from datetime import datetime

from YaralyzeServerDB import YaralyzeServerDB

CLIENT_SAMPLES_PATH = ".\\Analysis_samples"
LOGS_DIRECTORY_PATH = ".\\Logs\\"
ANALYSIS_TOOLS_PATH = ".\\Analysis_tools"
HASHES_DIRECTORY_PATH = ANALYSIS_TOOLS_PATH + "\\MalwareHashes"
YARA_RULES_DIRECTORY_PATH =  ANALYSIS_TOOLS_PATH + "\\YaraRules"
PARTIAL_MALWARE_HASHES_PATH = ".\\Analysis_tools\\MalwareHashes\\Partial_hashes.txt"
DATABASE_DIRECTORY = ".\\Database"

LOGS_PATH = ".\\Logs\\serverLogs.log"

EXTENSION = ".apk"

#Comunicacion con el cliente
STATIC_ANALYSIS_QUERY = 1;
HASH_ANALYSIS_QUERY = 2;
UPDATE_DB_QUERY = 3;

BUFFERSIZE = 8192
PORT = 2020

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
    
    def __createDirectories(self) -> None: #Añadir el resto de directorios que faltan
        if not os.path.isdir(LOGS_DIRECTORY_PATH):
            try:
                os.makedirs(LOGS_DIRECTORY_PATH)
            except:
                print("[!] - Error al crear el el directorio de logs")

        if not os.path.isdir(DATABASE_DIRECTORY):
            try:
                os.makedirs(DATABASE_DIRECTORY)
            except:
                self.__logger.error("[!] - Error al crear el el directorio de la base de datos")

        if not os.path.isdir(ANALYSIS_TOOLS_PATH):
            try:
                os.makedirs(ANALYSIS_TOOLS_PATH)
            except:
                self.__logger.error("[!] - Error al crear el el directorio de herramientas")

        if not os.path.isdir(YARA_RULES_DIRECTORY_PATH):
            try:
                os.makedirs(YARA_RULES_DIRECTORY_PATH)
            except:
                self.__logger.error("[!] - Error al crear el el directorio de reglas Yara")

        if not os.path.isdir(HASHES_DIRECTORY_PATH):
            try:
                os.makedirs(HASHES_DIRECTORY_PATH)
            except:
                self.__logger.error("[!] - Error al crear el el directorio de hashes")

        if not os.path.isdir(CLIENT_SAMPLES_PATH):
            try:
                os.makedirs(CLIENT_SAMPLES_PATH)
            except:
                print("[!] - Error al crear el el directorio de muestras")
                self.__logger.error("Error al crear el el directorio de muestras")
                

    def __setupLogConfig(self) -> None:
        if(not os.path.exists(LOGS_PATH)):
            open(LOGS_PATH, 'a').close()

        file = open(LOGS_PATH, "a")
        file.write("--------------------------- \n")
        file.write(str(datetime.now().strftime("%d-%B-%Y \n")))
        file.write("--------------------------- \n")
        file.close()

        handler = logging.FileHandler(LOGS_PATH)        
        handler.setFormatter(logging.Formatter('%(asctime)s: %(levelname)s - %(message)s', "%H:%M:%S"))

        self.__logger = logging.getLogger("serverLogger")
        self.__logger.setLevel(logging.DEBUG)
        self.__logger.addHandler(handler)
        

    # Métodos relacionados con la gestión de requests
    # -----------------------------------------------

    def __startServer(self) -> None:
        try:
            self.__serverSocket.bind((self.__host, PORT))
        except socket.error as e:
            print(str(e))
            self.__logger.error(str(e))
    
    def __waitForConnections(self) -> None:
        while(1):
            self.__serverSocket.listen(1)

            print("Servidor esperando peticiones en: " + str(self.__host) + ":" + str(PORT))
            self.__logger.info("Servidor esperando peticiones en: " + str(self.__host) + ":" + str(PORT))

            clientConnection, clientAddress = self.__serverSocket.accept()  
            self.__logger.info("Gestionando petición proveniente de " + str(clientAddress[0]) + ":" + str(clientAddress[1]))

            self.__proccessClientRequest(clientConnection)
    
    def __proccessClientRequest(self, clientConnection) -> None:
        request = self.__receiveRequestType(clientConnection)
        if request == STATIC_ANALYSIS_QUERY:
            self.__logger.info("Tramitando petición de análisis estático")
            self.__staticAnalysis(clientConnection)
        elif request == HASH_ANALYSIS_QUERY:
            self.__logger.info("Tramitando petición de análisis del hash")
            print("Tramitando petición de análisis del hash")
            self.__hashAnalysis(clientConnection)
        elif request == UPDATE_DB_QUERY:
            self.__logger.info("Tramitando petición de actualización de base de datos")
            self.__sendPartialHashesDB(clientConnection)
        

    def __receiveRequestType(self, clientConnection):
        requestType = clientConnection.recv(1024)
        return int.from_bytes(requestType, "big")


    # Métodos relacionados con peticiones de análisis del hash
    # --------------------------------------------------------

    def __hashAnalysis(self, clientConnection) -> None:
        hash = json.loads(self.__receiveClientSampleHeader(clientConnection))["hash"]

        malwareFound = self.__db.getHashCoincidence(hash)
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
        sampleName, sampleSize = header["name"], header["size"]

        with open(CLIENT_SAMPLES_PATH + sampleName + EXTENSION, "wb") as sampleFile:
            while True:
                bytes_readed = clientConnection.recv(min(BUFFERSIZE, sampleSize))
                if not bytes_readed:
                    sampleFile.close()
                    break

                sampleFile.write(bytes_readed)
                sampleSize = sampleSize - len(bytes_readed) 
        
        self.__logger.info("Fichero a analizar recibido correctamente desde el cliente")
        return CLIENT_SAMPLES_PATH + sampleName
    
    def __startStaticAnalysis(self, clientSamplePath) -> bool:
        print("[!] Iniciando análisis estático en el sample del cliente")
        self.__logger.info("Iniciando análisis estático en " + clientSamplePath + EXTENSION)
        return self.__analyzer.executeStaticAnalysis(clientSamplePath + EXTENSION)


    # Métodos comunes a todos los tipos de análisis
    # ---------------------------------------------
    
    def __receiveClientSampleHeader(self, clientConnection) -> str:
        return clientConnection.recv(1024).decode("utf-8")[2:]

    def __sendAnalysisOutcome(self, clientConnection, analysisOutcome):
        print("Enviando resultado del análisis al cliente")
        self.__logger.info("Enviando resultado del análisis al cliente")
            
        analysisOutcome = json.dumps(analysisOutcome)
        clientConnection.sendall(bytes(analysisOutcome + "\n", encoding="utf-8"))
        clientConnection.close()

    def __removeClientSample(self, clientSamplePath):
        os.remove(clientSamplePath + EXTENSION)


    # Métodos relacionados con peticiones de actualización de la base de datos
    # ------------------------------------------------------------------------
    
    def __sendPartialHashesDB(self, clientConnection):
        malwareHashesFile = open(PARTIAL_MALWARE_HASHES_PATH, "r")
        lines = malwareHashesFile.readlines()

        for line in lines:
            clientConnection.sendall(bytes(line, encoding="utf-8"))

        clientConnection.close()



if __name__ == "__main__":
    Server()
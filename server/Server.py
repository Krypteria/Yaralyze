import socket
import threading
import os
import json
import logging

from Analyzer import Analyzer
from datetime import datetime

CLIENT_SAMPLES_PATH = ".\\AnalysisSamples\\ClientSamples\\"
MALWARE_HASHES_PATH = ".\\AnalysisSamples\\MalwareHashes\\hashes.txt"
LOGS_DIRECTORY_PATH = ".\\Logs\\"
LOGS_PATH = ".\\Logs\\serverLogs.log"

EXTENSION = ".apk"

#Comunicacion con el cliente
STATIC_ANALYSIS_QUERY = 0;
UPDATE_DB_QUERY = 1;

BUFFERSIZE = 8192
PORT = 2020

class Server:
    def __init__(self) -> None:
        self.__host = self.__getCurrentAddress()
        self.__serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__createDirectories()
        self.__setupLogConfig()

        self.__analyzer = Analyzer()
        self.__startServer()
        self.__waitForConnections()

    def __getCurrentAddress(self) -> str:
        tempSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        tempSocket.connect((socket.gethostbyname("www.google.com"), 80))
        local_ip = tempSocket.getsockname()[0]
        tempSocket.close()
        return local_ip
    
    def __createDirectories(self) -> None:
        if(not os.path.isdir(LOGS_DIRECTORY_PATH)):
            try:
                os.makedirs(LOGS_DIRECTORY_PATH)
            except:
                print("[!] - Error al crear el el directorio de logs")

        if(not os.path.isdir(CLIENT_SAMPLES_PATH)):
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

            clientSamplePath = self.__proccessSample(clientConnection)
            self.__sendAnalysisOutcome(clientConnection, self.__requestStaticAnalysis(clientSamplePath))
            self.__removeClientSample(clientSamplePath)
            self.__analyzer.cleanAnalysisOutcome()
        elif request == UPDATE_DB_QUERY:
            self.__logger.info("Tramitando petición de actualización de base de datos")

            self.__sendMalwareHashes(clientConnection)
        

    def __receiveRequestType(self, clientConnection):
        requestType = clientConnection.recv(1024)
        return int.from_bytes(requestType, "big")

    # Métodos relacionados con peticiones de análisis estático
    # --------------------------------------------------------

    def __proccessSample(self, clientConnection) -> str:
        header = json.loads(self.__receiveSampleHeader(clientConnection))
        clientSamplePath = self.__receiveSampleFile(clientConnection, header)
        return clientSamplePath
    
    def __receiveSampleHeader(self, clientConnection) -> str:
        header = clientConnection.recv(1024).decode("utf-8")
        return header[2:]

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
    
    def __requestStaticAnalysis(self, clientSamplePath) -> bool:
        print("[!] Iniciando análisis estático en el sample del cliente")
        return self.__analyzer.executeStaticAnalysis(clientSamplePath + EXTENSION)

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
    
    def __sendMalwareHashes(self, clientConnection):
        malwareHashesFile = open(MALWARE_HASHES_PATH, "r")
        lines = malwareHashesFile.readlines()

        for line in lines:
            clientConnection.sendall(bytes(line, encoding="utf-8"))

        clientConnection.close()



if __name__ == "__main__":
    threading.Thread(target=Server())
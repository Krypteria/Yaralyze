import socket
import threading
import os
from Analyzer import Analyzer

CLIENT_SAMPLES_PATH = ".\\AnalysisSamples\\ClientSamples\\"
BUFFERSIZE = 2048
PORT = 2020

class Server:

    def __init__(self) -> None:
        self.__host = self.__getCurrentAddress()
        self.__serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.__analyzer = Analyzer()

        self.__endServerActivity = False;
        self.__createClientsSampleDirectories()
        self.__startServer()
        self.__waitForConnections()

    def __getCurrentAddress(self) -> str:
        tempSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        tempSocket.connect((socket.gethostbyname("www.google.com"), 80))
        local_ip = tempSocket.getsockname()[0]
        tempSocket.close()
        return local_ip
    
    def __createClientsSampleDirectories(self) -> None:
        if(not os.path.isdir(CLIENT_SAMPLES_PATH)):
            try:
                os.makedirs(CLIENT_SAMPLES_PATH)
            except:
                print("[!] Error al crear el el directorio de muestras")

    def __startServer(self) -> None:
        try:
            self.__serverSocket.bind((self.__host, PORT))
        except socket.error as msg:
            print(msg)

    def __endServerActivity(self) -> None:
        self.__endServerActivity = True
    
    def __waitForConnections(self) -> None:
        while(not self.__endServerActivity):
            self.__serverSocket.listen(1)
            print("[!] Servidor esperando peticiones en: ", self.__host, PORT)
            clientConnection, clientAddress = self.__serverSocket.accept()  
            self.__proccessClientRequest(clientConnection, clientAddress)
    
    def __proccessClientRequest(self, clientConnection, clientAddress) -> None:
        print("[!] Gestionando petición proveniente de ", clientAddress)
        clientSamplePath = self.__receiveSample(clientConnection)
        self.__sendAnalysisOutcome(clientConnection, self.__requestStaticAnalysis(clientSamplePath))
        clientConnection.close()
    
    def __receiveSample(self, clientConnection) -> str:
        header = self.__receiveSampleHeader(clientConnection)
        print("[!] El header recibido es: ", header)
        clientSamplePath = self.__receiveSampleFile(clientConnection, header)
        return clientSamplePath
    
    def __receiveSampleHeader(self, clientConnection) -> str:
        header = clientConnection.recv(1024).decode("utf-8")
        return header[2:]

    def __receiveSampleFile(self, clientConnection, header) -> str:
        sampleName, sampleSize = header[0:header.find("-")], int(header[header.find("-") + 1:])

        with open(CLIENT_SAMPLES_PATH + sampleName, "wb") as sampleFile:
            while True:
                bytes_readed = clientConnection.recv(min(BUFFERSIZE, sampleSize))
                if not bytes_readed:
                    sampleFile.close()
                    break

                sampleFile.write(bytes_readed)
                sampleSize = sampleSize - len(bytes_readed) 
        
        print("[!] Fichero recibido correctamente desde el client")
        return CLIENT_SAMPLES_PATH + sampleName
    
    def __requestStaticAnalysis(self, clientSamplePath) -> bool:
        print("[!] Iniciando análisis estático en el sample del cliente")
        return self.__analyzer.executeStaticAnalysis(clientSamplePath)

    def __sendAnalysisOutcome(self, clientConnection, malwareDetected):
        print("[!] Enviando resultado del análisis al cliente")
        if malwareDetected:
            print("     [!] Malware detectado")
            clientConnection.send(str(1).encode())
        else:
            print("     [!] Malware no detectado")
            clientConnection.send(str(0).encode())
            


if __name__ == "__main__":
    threading.Thread(target=Server())
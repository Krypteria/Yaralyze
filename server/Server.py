import socket
import threading
import os

BUFFERSIZE = 2048
PORT = 2020

class Server:

    def __init__(self) -> None:
        self.__host = self.__getCurrentAddress()
        self.__serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        self.__clientSamplesPath = ".\\Analysis\\ClientSamples\\"
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
    
    def __createClientsSampleDirectories(self):
        if(not os.path.isdir(self.__clientSamplesPath)):
            try:
                os.makedirs(self.__clientSamplesPath)
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
    
    def __proccessClientRequest(self, clientConnection, clientAddress):
        print("[!] Gestionando petición proveniente de ", clientAddress)
        self.__receiveAPK(clientConnection)
    
    def __receiveAPK(self, clientConnection):
        header = self.__receiveAPKHeader(clientConnection)
        print("[!] El header recibido es: ", header)
        self.__receiveAPKFile(clientConnection, header)
    
    def __receiveAPKHeader(self, clientConnection) -> str:
        header = clientConnection.recv(1024).decode("utf-8")
        return header[2:]

    def __receiveAPKFile(self, clientConnection, header):
        apkName, apkSize = header[0:header.find("-")], int(header[header.find("-") + 1:])

        with open(self.__clientSamplesPath + apkName, "wb") as apkFile:
            while True:
                bytes_readed = clientConnection.recv(min(BUFFERSIZE, apkSize))
                if not bytes_readed:
                    apkFile.close()
                    break

                apkFile.write(bytes_readed)
                apkSize = apkSize - len(bytes_readed) 
        
        print("[!] Fichero recibido correctanebte desde el client")
        #No cierro el socket del cliente ya que le tengo que mandar el callback con el resultado del análisis

if __name__ == "__main__":
    threading.Thread(target=Server())
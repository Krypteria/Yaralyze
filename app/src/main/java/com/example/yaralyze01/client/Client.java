package com.example.yaralyze01.client;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client implements Runnable{
    private Socket clientSocket;
    private DataOutputStream output;
    private DataInputStream input;
    private byte[] buffer;

    private String apkName;
    private String apkPath;

    private final int BUFFERSIZE = 2048;
    private final String serverIP = "192.168.1.36";
    private final int PORT = 2020;

    public Client(String apkName, String apkPath){
        this.buffer = new byte[this.BUFFERSIZE];
        this.apkPath = apkPath;
        this.apkName = apkName;
    }

    @Override
    public void run(){
        sendStaticAnalysisRequest();
    }

    public void sendStaticAnalysisRequest(){
        this.clientSocket = new Socket();
        try{
            this.clientSocket.connect(new InetSocketAddress(this.serverIP, this.PORT));
            this.sendAPK(this.apkName, this.apkPath);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void sendAPK(String apkName, String apkPath) throws IOException {
        File apk = new File(apkPath);
        this.input = new DataInputStream(new BufferedInputStream(new FileInputStream(apk)));
        this.output = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));

        //Envio el header del apk al servidor
        this.sendAPKHeader(apkName, apk);

        long fileSize = apk.length();

        //La longitud del fichero por lo general sobrepasa al valor máximo de un entero
        int integerFileSizeValue = Integer.MAX_VALUE;
        boolean integerMaxValueExceeded = true;
        if(fileSize < Integer.MAX_VALUE){
            integerFileSizeValue = (int)fileSize;
            integerMaxValueExceeded = false;
        }

        int readedBytes;
        while(integerFileSizeValue > 0 && (readedBytes = this.input.read(this.buffer, 0, Math.min(this.BUFFERSIZE, integerFileSizeValue))) >= 0){
            this.output.write(this.buffer, 0, readedBytes);
            this.output.flush();

            fileSize -= readedBytes;
            if(integerMaxValueExceeded && fileSize < Integer.MAX_VALUE){
                integerFileSizeValue = (int)fileSize;
                integerMaxValueExceeded = false;
            }
        }
    }

    //Header de tipo: <nombre>-<tamaño>
    private void sendAPKHeader(String apkName, File apk) throws IOException {
        String header = apkName + "-" + apk.length();
        System.out.println(header);

        this.output = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));
        this.output.writeUTF(header);
        this.output.flush();
    }
}

package com.example.yaralyze01.client;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private DataOutputStream output;
    private DataInputStream input;
    private byte[] buffer;

    private final int BUFFERSIZE = 2048;
    private final String serverIP = "127.0.0.1";
    private final int PORT = 2020;

    public Client(){
        this.buffer = new byte[this.BUFFERSIZE];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendStaticAnalysisRequest(String apkPath){
        this.clientSocket = new Socket();
        try{
            this.clientSocket.connect(new InetSocketAddress(this.serverIP, this.PORT));
            this.sendAPK(apkPath);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendAPK(String apkPath) throws IOException {
        File file = new File(apkPath);
        this.input = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        this.output = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));

        long fileSize = file.length();

        //File lenght could be much bigger than the Integer max value
        int integerFileSizeValue = Integer.MAX_VALUE;
        boolean integerMaxValueExceeded = true;
        if(fileSize < Integer.MAX_VALUE){
            integerFileSizeValue = Math.toIntExact(fileSize);
            integerMaxValueExceeded = false;
        }

        int readedBytes;
        while(integerFileSizeValue > 0 && (readedBytes = this.input.read(this.buffer, 0, Math.min(this.BUFFERSIZE, integerFileSizeValue))) >= 0){
            this.output.write(this.buffer, 0, readedBytes);
            this.output.flush();

            fileSize -= readedBytes;
            if(integerMaxValueExceeded && fileSize < Integer.MAX_VALUE){
                integerFileSizeValue = Math.toIntExact(fileSize);
                integerMaxValueExceeded = false;
            }
        }
    }
}

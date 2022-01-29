package com.example.yaralyze01.client;


import android.content.Context;
import android.graphics.Color;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcome;
import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcomeManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable{

    private final int STATIC_ANALYSIS_QUERY = 0;
    private final int UPDATE_DB_QUERY = 1;

    private final String serverIP = "192.168.1.34"; // placeholder
    private final int BUFFERSIZE = 8192;
    private final int PORT = 2020;

    private Socket clientSocket;
    private DataOutputStream bytesOutput;
    private DataInputStream bytesInput;

    private BufferedReader textInput;

    private int requestType;

    private String apkName;
    private String apkPath;
    private byte[] buffer;

    private AppDetails appDetails;
    private Context context;
    private AnalysisOutcomeManagement analysisOutcomeManagement;

    public Client(AnalysisOutcomeManagement analysisOutcomeManagement, AppDetails appDetails){
        this.analysisOutcomeManagement = analysisOutcomeManagement;
        this.buffer = new byte[this.BUFFERSIZE];
        this.appDetails = appDetails;
        this.apkPath = appDetails.getAppSrc();
        this.apkName = appDetails.getAppName();
        this.requestType = STATIC_ANALYSIS_QUERY;
    }

    public Client(Context context){
        this.context = context;
        this.requestType = UPDATE_DB_QUERY;
    }

    @Override
    public void run(){
        this.connectSocket();
        this.sendRequestType();
        switch (this.requestType){
            case STATIC_ANALYSIS_QUERY:
                this.sendStaticAnalysisRequest();
                try {
                    this.receiveServerAnalysisOutcome();
                } catch (IOException | JSONException e) { //MEJORAR LAS EXCEPCIONES -> TOAST
                    e.printStackTrace();
                }
                break;
            case UPDATE_DB_QUERY:
                try {
                    this.receiveServerDBHashes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void connectSocket(){
        this.clientSocket = new Socket();
        try {
            this.clientSocket.connect(new InetSocketAddress(this.serverIP, this.PORT));
        } catch (IOException e) {
            e.printStackTrace(); //MEJORAR LAS EXCEPCIONES -> TOAST
        }
    }

    private void sendRequestType(){
        try {
            this.bytesOutput = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));
            this.bytesOutput.writeInt(this.requestType);
            this.bytesOutput.flush();
        } catch (IOException e) {
            e.printStackTrace(); //MEJORAR LAS EXCEPCIONES -> TOAST
        }
    }

    // Métodos relacionados con peticiones de actualización de la base de datos
    // ------------------------------------------------------------------------

    private void receiveServerDBHashes() throws IOException {
        this.textInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        YaralyzeDB db = YaralyzeDB.getInstance(this.context);

        String hash;
        while((hash = this.textInput.readLine()) != null) {
            db.insertHash(hash);
        }

        this.textInput.close();
        this.clientSocket.close();
    }

    // Métodos relacionados con peticiones de análisis estático
    // --------------------------------------------------------

    private void sendStaticAnalysisRequest(){
        File apk = new File(apkPath);
        try{
            this.sendAPKHeader(apkName, apk.length());
            this.sendAPK(apk);
        }
        catch(IOException e){ //MEJORAR LAS EXCEPCIONES -> TOAST
            e.printStackTrace();
        }
    }

    private void sendAPKHeader(String apkName, long apkLength) throws IOException {
        String header = apkName + "-" + apkLength;

        this.bytesOutput = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));
        this.bytesOutput.writeUTF(header);
        this.bytesOutput.flush();
    }

    private void sendAPK(File apk) throws IOException {
        this.bytesInput = new DataInputStream(new BufferedInputStream(new FileInputStream(apk)));
        this.bytesOutput = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));

        long fileSize = apk.length();

        //La longitud del fichero por lo general sobrepasa al valor máximo de un entero
        int integerFileSizeValue = Integer.MAX_VALUE;
        boolean integerMaxValueExceeded = true;
        if(fileSize < Integer.MAX_VALUE){
            integerFileSizeValue = (int)fileSize;
            integerMaxValueExceeded = false;
        }

        int readBytes;
        while(integerFileSizeValue > 0 && (readBytes = this.bytesInput.read(this.buffer, 0, Math.min(this.BUFFERSIZE, integerFileSizeValue))) >= 0){
            this.bytesOutput.write(this.buffer, 0, readBytes);
            this.bytesOutput.flush();

            fileSize -= readBytes;
            if(integerMaxValueExceeded && fileSize < Integer.MAX_VALUE){
                integerFileSizeValue = (int)fileSize;
                integerMaxValueExceeded = false;
            }
        }
    }

    private void receiveServerAnalysisOutcome() throws IOException, JSONException {
        this.textInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        String outcome = this.textInput.readLine();

        JSONObject analysisOutcomeJSON = new JSONObject(outcome);
        AnalysisOutcome analysisOutcome = this.buildAnalysisOutcomeObject(analysisOutcomeJSON);

        YaralyzeDB db = YaralyzeDB.getInstance(this.context);
        db.insertAnalysisOutcome(analysisOutcome);

        this.analysisOutcomeManagement.showAnalysisOutcome(analysisOutcome);

        this.textInput.close();
        this.clientSocket.close();
    }

    private AnalysisOutcome buildAnalysisOutcomeObject(JSONObject analysisOutcomeJSON) throws JSONException {
        ArrayList<String> matchedRules = new ArrayList<>();

        int numMatchedRules = analysisOutcomeJSON.getInt("numMatchedRules");
        JSONArray matchedRulesJSON = analysisOutcomeJSON.getJSONArray("matchedRules");

        for(int i = 0; i < numMatchedRules; i++){
           matchedRules.add(matchedRulesJSON.get(i).toString());
        }

        return new AnalysisOutcome(1, this.apkName, this.appDetails.getPackageName(), analysisOutcomeJSON.getBoolean("detected"), matchedRules);
    }
}

package com.example.yaralyze01.client;


import android.content.Context;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcome;
import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcomeManagement;
import com.example.yaralyze01.ui.common.AnalysisType;
import com.example.yaralyze01.ui.loading.LoadingAppFragment;

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
import java.net.SocketException;
import java.util.ArrayList;

public class Client implements Runnable{

    private final int UPDATE_DB_QUERY = 3;

    private final String serverIP = "192.168.1.33";
    private final int PORT = 3389;
    private final int BUFFER_SIZE = 16384;

    private Socket clientSocket;
    private DataOutputStream bytesOutput;
    private DataInputStream bytesInput;

    private BufferedReader textInput;

    private int requestType;

    private String appName;
    private String appPath;
    private String appHash;

    private byte[] buffer;

    private AppDetails appDetails;

    private LoadingAppFragment loadingAppFragment;
    private Context context;
    private AnalysisOutcomeManagement analysisOutcomeManager;

    //Constructor -> Analisis
    public Client(AnalysisOutcomeManagement analysisOutcomeManager, AppDetails appDetails, int analysisType){
        this.analysisOutcomeManager = analysisOutcomeManager;
        this.buffer = new byte[this.BUFFER_SIZE];
        this.appDetails = appDetails;

        switch (analysisType){
            case AnalysisType.COMPLETE:
                this.appPath = appDetails.getAppSrc();
                this.appName = appDetails.getAppName();
                this.appHash = this.appDetails.getSha256hash();
                this.requestType = AnalysisType.COMPLETE;
                break;
            case AnalysisType.STATIC:
                this.appPath = appDetails.getAppSrc();
                this.appName = appDetails.getAppName();
                this.requestType = AnalysisType.STATIC;
                break;
            case AnalysisType.HASH:
                this.appName = appDetails.getAppName();
                this.appHash = appDetails.getSha256hash();
                this.requestType = AnalysisType.HASH;
                break;
            default:
                break;
        }
    }

    //Constructor -> actualizar DB
    public Client(Context context, LoadingAppFragment fragment){
        this.context = context;
        this.loadingAppFragment = fragment;
        this.requestType = UPDATE_DB_QUERY;
    }

    @Override
    public void run(){
        this.connectSocket();
        switch (this.requestType){
            case AnalysisType.COMPLETE:
                this.completeAnalysis();
                break;
            case AnalysisType.STATIC:
                this.staticAnalysis();
                break;
            case AnalysisType.HASH:
                this.hashAnalysis();
                break;
            case UPDATE_DB_QUERY:
                this.updateDB();
                break;
            default:
                break;
        }
    }

    private void connectSocket() {
        this.clientSocket = new Socket();

        try {
            this.clientSocket.connect(new InetSocketAddress(this.serverIP, this.PORT));
        } catch (IOException e) {
            switch (this.requestType){
                case UPDATE_DB_QUERY:
                    this.loadingAppFragment.showClientExceptionDialog("Ha ocurrido un error al conectarse al servidor para actualizar la base de datos.");
                    break;
                default:
                    this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al conectarse al servidor.");
                    break;
            }
        }
    }

    private void sendRequestType(){
        try {
            this.bytesOutput = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));
            this.bytesOutput.writeInt(this.requestType);
            this.bytesOutput.flush();
        } catch (IOException e) {
            switch (this.requestType){
                case UPDATE_DB_QUERY:
                    this.loadingAppFragment.showClientExceptionDialog("Ha ocurrido un error al enviar la petición al servidor.");
                    break;
                default:
                    this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al enviar la petición al servidor.");
                    break;
            }
        }
    }

    // Métodos relacionados con peticiones de análisis completo
    // --------------------------------------------------------

    private void completeAnalysis(){
        try {
            this.performCompleteAnalysis();
        } catch (IOException e) {
            this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al recibir el resultado del análisis completo.");
        } catch (JSONException e) {
            this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al procesar el resultado del análisis completo.");
            e.printStackTrace();
        }
    }

    private void performCompleteAnalysis() throws IOException, JSONException {
        this.requestType = AnalysisType.STATIC;
        this.sendStaticAnalysisRequest();
        AnalysisOutcome staticAnalysisOutcome = this.receiveServerAnalysisOutcome();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.connectSocket();
        this.requestType = AnalysisType.HASH;
        this.sendHashAnalysisRequest();
        AnalysisOutcome hashAnalysisOutcome = this.receiveHashAnalysisOutcome();

        this.clientSocket.close();
        this.sendCompleteAnalysisOutcomeToView(staticAnalysisOutcome, hashAnalysisOutcome);
    }

    // Métodos relacionados con peticiones de análisis estático
    // --------------------------------------------------------

    private void staticAnalysis(){
        this.sendStaticAnalysisRequest();
        try {
            AnalysisOutcome outcome = this.receiveServerAnalysisOutcome();
            this.clientSocket.close();
            this.sendSimpleAnalysisOutcomeToView(outcome);
        } catch (IOException e) {
            this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al recibir el resultado del análisis estático.");
        } catch (JSONException e) {
            this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al procesar el resultado del análisis estático.");
        }
    }

    private void sendStaticAnalysisRequest(){
        this.sendRequestType();
        File apk = new File(appPath);
        try{
            this.sendAPKHeader(appName, apk.length());
            this.sendAPK(apk);
        }
        catch(IOException | JSONException e){
            this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al enviar los datos al servidor para realizar el análisis estático.");
        }
    }

    private void sendAPKHeader(String apkName, long apkLength) throws IOException, JSONException {
        JSONObject headerJSON = new JSONObject();
        headerJSON.put("name", apkName);
        headerJSON.put("size", String.valueOf(apkLength));

        this.bytesOutput = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));
        this.bytesOutput.writeUTF(headerJSON.toString());
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
        while(integerFileSizeValue > 0 && (readBytes = this.bytesInput.read(this.buffer, 0, Math.min(this.BUFFER_SIZE, integerFileSizeValue))) >= 0){
            this.bytesOutput.write(this.buffer, 0, readBytes);
            this.bytesOutput.flush();

            fileSize -= readBytes;
            if(integerMaxValueExceeded && fileSize < Integer.MAX_VALUE){
                integerFileSizeValue = (int)fileSize;
                integerMaxValueExceeded = false;
            }
        }
    }

    private AnalysisOutcome receiveServerAnalysisOutcome() throws IOException, JSONException {
        this.textInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        String outcome = this.textInput.readLine();

        JSONObject analysisOutcomeJSON = new JSONObject(outcome);
        AnalysisOutcome analysisOutcome = this.buildAnalysisOutcomeObject(analysisOutcomeJSON);

        this.textInput.close();

        return analysisOutcome;
    }

    private AnalysisOutcome buildAnalysisOutcomeObject(JSONObject analysisOutcomeJSON) throws JSONException {
        ArrayList<String> matchedRules = new ArrayList<>();

        if(analysisOutcomeJSON.has("numMatchedRules")){
            int numMatchedRules = analysisOutcomeJSON.getInt("numMatchedRules");
            JSONArray matchedRulesJSON = analysisOutcomeJSON.getJSONArray("matchedRules");

            for(int i = 0; i < numMatchedRules; i++){
                matchedRules.add(matchedRulesJSON.get(i).toString());
            }
        }

        return new AnalysisOutcome(-1, this.requestType, null, this.appName, this.appDetails.getPackageName(), analysisOutcomeJSON.getBoolean("detected"), null, matchedRules);
    }

    // Métodos relacionados con peticiones de análisis de hash
    // -------------------------------------------------------

    private void hashAnalysis(){
        AnalysisOutcome outcome = this.receiveHashAnalysisOutcome();
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.sendSimpleAnalysisOutcomeToView(outcome);
    }

    private AnalysisOutcome receiveHashAnalysisOutcome(){
        YaralyzeDB db = YaralyzeDB.getInstance(this.context);
        AnalysisOutcome analysisOutcome = db.getCoincidence(this.appName, appDetails.getPackageName(), this.appHash);
        if(!analysisOutcome.isMalwareDetected()){
            try {
                this.sendHashAnalysisRequest();
                analysisOutcome = this.receiveServerAnalysisOutcome();
            } catch (IOException e) {
                this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al recibir el resultado del análisis del hash del servidor.");
                e.printStackTrace();
            } catch (JSONException e) {
                this.analysisOutcomeManager.showAnalysisException("Ha ocurrido un error al procesar el resultado del análisis del hash del servidor.");
            }

        }

        return analysisOutcome;
    }

    private void sendHashAnalysisRequest() throws IOException, JSONException {
        this.sendRequestType();

        JSONObject hashJSON = new JSONObject();
        hashJSON.put("hash", this.appHash);

        this.bytesOutput = new DataOutputStream(new BufferedOutputStream(this.clientSocket.getOutputStream()));
        this.bytesOutput.writeUTF(hashJSON.toString());
        this.bytesOutput.flush();
    }

    // Métodos relacionados con peticiones de actualización de la base de datos
    // ------------------------------------------------------------------------

    private void updateDB(){
        try {
            this.sendRequestType();
            this.receiveServerDBHashes();
            this.loadingAppFragment.malwareHashesLoaded();
        } catch (IOException e) {
            this.loadingAppFragment.showClientExceptionDialog("Ha ocurrido un error al actualizar la base de datos de hashes.");
        }
    }

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

    // Métodos para mostrar el outcome de los diferentes análisis
    // ----------------------------------------------------------

    private void sendSimpleAnalysisOutcomeToView(AnalysisOutcome analysisOutcome){
        this.analysisOutcomeManager.showAnalysisOutcome(analysisOutcome);
    }

    private void sendCompleteAnalysisOutcomeToView(AnalysisOutcome staticOutcome, AnalysisOutcome hashOutcome){
        this.analysisOutcomeManager.showAnalysisOutcome(staticOutcome, hashOutcome);
    }
}

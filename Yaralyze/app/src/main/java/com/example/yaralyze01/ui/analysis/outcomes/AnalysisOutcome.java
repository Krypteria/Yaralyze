package com.example.yaralyze01.ui.analysis.outcomes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AnalysisOutcome {

    private int analysisType;

    private String analyzedAppName;
    private String analyzedAppPackage;
    private boolean malwareDetected;
    private ArrayList<String> matchedRules;
    private String analysisDate;

    public AnalysisOutcome(int analysisType, String analyzedAppName, String analyzedAppPackage, boolean malwareDetected, ArrayList<String> matchedRules){
        this.analysisType = analysisType;
        this.analyzedAppName = analyzedAppName;
        this.analyzedAppPackage = analyzedAppPackage;
        this.malwareDetected = malwareDetected;
        this.matchedRules = matchedRules;
        this.analysisDate = this.getCurrentDateTime();
    }

    private String getCurrentDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy  HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        return dateFormat.format(new Date());
    }

    public int getAnalysisType() {
        return this.analysisType;
    }

    public String getAnalyzedAppName() {
        return this.analyzedAppName;
    }

    public String getAnalyzedAppPackage(){ return this.analyzedAppPackage; }

    public boolean isMalwareDetected() {
        return this.malwareDetected;
    }

    public ArrayList<String> getMatchedRules() {
        return this.matchedRules;
    }

    public String getAnalysisDate() {
        return this.analysisDate;
    }
}

package com.example.yaralyze01.ui.analysis.outcomes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AnalysisOutcome {

    private int analysisType;

    private String analyzedAppName;
    private boolean malwareDetected;
    private ArrayList<String> matchedRules;
    private String analysisDate;

    public AnalysisOutcome(int analysisType, String analyzedAppName, boolean malwareDetected, ArrayList<String> matchedRules){
        this.analysisType = analysisType;
        this.analyzedAppName = analyzedAppName;
        this.malwareDetected = malwareDetected;
        this.matchedRules = matchedRules;
        this.analysisDate = this.getCurrentDateTime();
    }

    private String getCurrentDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy  HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public int getAnalysisType() {
        return this.analysisType;
    }

    public String getAnalyzedAppName() {
        return this.analyzedAppName;
    }

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

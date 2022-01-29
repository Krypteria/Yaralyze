package com.example.yaralyze01.ui.analysis.outcomes;

import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AnalysisOutcome {

    private int analysisType;

    private String analyzedAppName;
    private String analyzedAppPackage;
    private Drawable analyzedAppIcon;

    private boolean malwareDetected;
    private ArrayList<String> matchedRules;
    private String analysisDate;

    public AnalysisOutcome(int analysisType, Drawable analyzedAppIcon, String analyzedAppName, String analyzedAppPackage, boolean malwareDetected,
                                String analysisDate, ArrayList<String> matchedRules){
        this.analysisType = analysisType;
        this.analyzedAppName = analyzedAppName;
        this.analyzedAppPackage = analyzedAppPackage;
        this.analyzedAppIcon = analyzedAppIcon;

        this.malwareDetected = malwareDetected;
        this.matchedRules = matchedRules;

        if(analysisDate == null)
            this.analysisDate = this.getCurrentDateTime();
        else
            this.analysisDate = analysisDate;
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

    public Drawable getAnalyzedAppIcon(){ return this.analyzedAppIcon; }

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

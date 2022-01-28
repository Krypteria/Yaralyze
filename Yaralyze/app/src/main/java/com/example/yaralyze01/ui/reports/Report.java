package com.example.yaralyze01.ui.reports;

import android.graphics.drawable.Drawable;

public class Report {

    private final static String malwareDetectedString = "Malware detectado";
    private final static String malwareNotDetectedString = "Malware no detectado";

    private int idOutcome;
    private String appName;
    private Drawable appIcon;
    private String reportDate;
    private boolean reportDetection;

    public Report(int idOutcome, String appName, String reportDate, int reportDetection){
        this.idOutcome = idOutcome;
        this.appName = appName;
        this.reportDate = reportDate;
        this.reportDetection = reportDetection == 1;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public int getIdApp(){
        return this.idOutcome;
    }

    public String getAppName(){
        return this.appName;
    }

    public Drawable getAppIcon(){
        return this.appIcon;
    }

    public String getReportDate(){
        return this.reportDate;
    }

    public String getDetectionText(){
        if(this.reportDetection){
            return malwareDetectedString;
        }

        return malwareNotDetectedString;
    }
}

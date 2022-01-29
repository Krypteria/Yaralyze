package com.example.yaralyze01.ui.reports;

import android.graphics.drawable.Drawable;

public class Report {

    private final static String malwareDetectedString = "Malware detectado";
    private final static String malwareNotDetectedString = "Malware no detectado";

    private int idOutcome;
    private String appName;
    private String appPackage;
    private Drawable appIcon;
    private String reportDate;
    private boolean reportDetection;

    public Report(int idOutcome, String appName, Drawable appIcon, String appPackage, String reportDate, int reportDetection){
        this.idOutcome = idOutcome;
        this.appIcon = appIcon;
        this.appName = appName;
        this.appPackage = appPackage;
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

    public String getAppPackage(){ return this.appPackage; }

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

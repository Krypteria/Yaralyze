package com.example.yaralyze01.ui.reports;

import android.graphics.drawable.Drawable;

public class Report {
    private String appName;
    private Drawable appIcon;
    private String reportDate;
    private boolean reportDetection;

    public Report(){

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
            return "Malware detectado";
        }

        return "Malware no detectado";
    }
}

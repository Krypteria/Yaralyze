package com.example.yaralyze01.ui.analysis;

import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//En la segunda además de lo de la primera muestro: firstInstallTime, installLocation, lastUpdateTime, providers, (permissions), (requested permissions),
public class AppDetails {
    private String appName;
    private String packageName;
    private String appVersion;
    private Drawable appIcon;

    private String appSrc;
    private String firstTimeInstalledDate;
    private String lastTimeUpdatedDate;

    public AppDetails(String appName, String packageName, String appVersion, Drawable appIcon, String appSrc, long firstTimeInstalledDate, long lastTimeUpdatedDate){
        this.appName = appName;
        this.packageName = packageName;
        this.appVersion = appVersion;
        this.appIcon = appIcon;

        this.appSrc = appSrc;
        this.firstTimeInstalledDate = getDateFormated(firstTimeInstalledDate);
        this.lastTimeUpdatedDate = getDateFormated(lastTimeUpdatedDate);
    }

    public String getAppName(){
        return this.appName;
    }
    public String getPackageName(){
        return this.packageName;
    }
    public String getAppVersion(){ return this.appVersion; }
    public Drawable getAppIcon(){ return this.appIcon; }

    public String getAppSrc(){ return this.appSrc; }

    private String getDateFormated(long milisecondsDate){
        Date date = new Date(milisecondsDate);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Spain/Madrid"));
        String formatedDate = format.format(date);

        return formatedDate;
    }
}

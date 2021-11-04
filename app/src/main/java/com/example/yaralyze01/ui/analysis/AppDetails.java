package com.example.yaralyze01.ui.analysis;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    public AppDetails(PackageInfo packageInfo, PackageManager packageManager){
        this.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        this.packageName = packageInfo.packageName;
        this.appVersion = packageInfo.versionName;
        this.appIcon = packageInfo.applicationInfo.loadIcon(packageManager);

        this.appSrc = packageInfo.applicationInfo.sourceDir;
        this.firstTimeInstalledDate = getDateFormated(packageInfo.firstInstallTime);
        this.lastTimeUpdatedDate = getDateFormated(packageInfo.lastUpdateTime);
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
    public String getFirstTimeInstalledDate(){ return this.firstTimeInstalledDate; }
    public String getLastTimeUpdatedDate(){ return this.lastTimeUpdatedDate; }

    private String getDateFormated(long milisecondsDate){
        Date date = new Date(milisecondsDate);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Spain/Madrid"));

        return format.format(date);
    }
}

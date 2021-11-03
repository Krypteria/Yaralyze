package com.example.yaralyze01.ui.analysis;

//En la primera activity muestro: Nombre, Foto

import android.graphics.drawable.Drawable;

//En la segunda además de lo de la primera muestro: firstInstallTime, installLocation, lastUpdateTime, providers, (permissions), (requested permissions),
public class AppDetails {
    private String appName;
    private String packageName;
    private String appVersion;
    private Drawable appIcon;

    public AppDetails(String appName, String packageName, String appVersion, Drawable appIcon){
        this.appName = appName;
        this.packageName = packageName;
        this.appVersion = appVersion;
        this.appIcon = appIcon;
    }

    public String getAppName(){
        return this.appName;
    }
    public String getPackageName(){
        return this.packageName;
    }
    public String getAppVersion(){ return this.appVersion; }
    public Drawable getAppIcon(){ return this.appIcon; }
}

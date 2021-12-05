package com.example.yaralyze01.ui.analysis.appDetails;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AppDetails {
    private String appName;
    private String packageName;
    private String appVersion;
    private Drawable appIcon;

    private String appSrc;
    private String firstTimeInstalledDate;
    private String lastTimeUpdatedDate;
    private String sha256hash;
    private String md5hash;

    public AppDetails(PackageInfo packageInfo, PackageManager packageManager){
        this.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        this.packageName = packageInfo.packageName;
        this.appVersion = packageInfo.versionName;
        this.appIcon = packageInfo.applicationInfo.loadIcon(packageManager);

        this.appSrc = packageInfo.applicationInfo.sourceDir;

        this.firstTimeInstalledDate = getDateFormated(packageInfo.firstInstallTime);
        this.lastTimeUpdatedDate = getDateFormated(packageInfo.lastUpdateTime);
        this.sha256hash = null;
        this.md5hash = null;
    }


    public void setSha256hash(String hash){
        this.sha256hash = hash;
    }

    public void setMd5hash(String hash){
        this.md5hash = hash;
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

    public String getSha256hash(){ return this.sha256hash; }

    public String getMd5hash(){ return this.md5hash; }

    private String getDateFormated(long ms){
        Date date = new Date(ms);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Spain/Madrid"));

        return format.format(date);
    }
}

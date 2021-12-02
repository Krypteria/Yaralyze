package com.example.yaralyze01.ui.analysis;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    }

    public void calculateHashes(){
        File apk = new File(this.appSrc);
        this.sha256hash = getAppHash("SHA-256", apk);
        this.md5hash = getAppHash("MD5", apk);
    }

    private String getAppHash(String algorithm, File apk){
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            //Leo todos los bytes del fichero
            FileInputStream fileReader = new FileInputStream(apk);
            byte[] buffer = new byte[1024];
            int readedBytes = 0;

            while((readedBytes = fileReader.read(buffer)) != -1){
                digest.update(buffer, 0 , readedBytes);
            }

            fileReader.close();

            //Calculo el hash (NO FUNCIONA DEL TODO BIEN)
            byte[] digestBuffer = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < digestBuffer.length; i++){
                stringBuilder.append(Integer.toString((buffer[i] & 0xff) + 0x100, 16).substring(1));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
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

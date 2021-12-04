package com.example.yaralyze01.ui.home;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Pair;

import com.example.yaralyze01.BackgroundTask;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.home.HomeFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class GetInstalledAppsTask extends BackgroundTask {
    private HomeFragment fragment;
    private ArrayList<AppDetails> installedApps;
    private PackageManager packageManager;

    public GetInstalledAppsTask(HomeFragment fragment){
        super(fragment);
        this.fragment = fragment;
        this.installedApps = new ArrayList<>();
        this.packageManager = this.fragment.getActivity().getPackageManager();
    }

    public void startOnBackground(){
        this.startBackground();
    }

    @Override
    public void doInBackground() {
        this.getInstalledAppsIntent(false);
    }

    @Override
    public void onPostExecute() {
        this.fragment.installedAppsTaskCallback(this.installedApps);
    }

    private void getInstalledAppsIntent(boolean getSysPackages){
        for(PackageInfo packageInfo : this.packageManager.getInstalledPackages(0)){
            if(this.packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null){
                if((!getSysPackages) && (packageInfo.versionName == null)){
                    continue;
                }
                AppDetails app = new AppDetails(packageInfo, this.packageManager);
                this.getAppHashesIntent(app);
                this.installedApps.add(app);
            }
        }
    }

    private void getAppHashesIntent(AppDetails app){
        File apk = new File(app.getAppSrc());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Pair<String, String> hashes = getAppHash(apk);
                app.setSha256hash(hashes.first);
                app.setMd5hash(hashes.second);
            }
        }).start();
    }

    private Pair<String, String> getAppHash(File apk){
        FileInputStream fileReader = null;
        try {
            fileReader = new FileInputStream(apk);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            MessageDigest md5digest = MessageDigest.getInstance("MD5");
            MessageDigest sha256digest = MessageDigest.getInstance("SHA-256");

            //Leo todos los bytes del fichero y computo el checksum
            byte[] buffer = new byte[8192];
            int readedBytes;

            while((readedBytes = fileReader.read(buffer)) > 0) {
                md5digest.update(buffer, 0, readedBytes);
                sha256digest.update(buffer, 0, readedBytes);
            }

            //Convierto el checksum en el hash
            byte[] md5digestBuffer = md5digest.digest();
            byte[] sha256digestBuffer = sha256digest.digest();
            BigInteger md5bigInt = new BigInteger(1, md5digestBuffer);
            BigInteger sha256bigInt = new BigInteger(1, sha256digestBuffer);

            String md5Hash = md5bigInt.toString(16);
            String sha256Hash = sha256bigInt.toString(16);

            sha256Hash = String.format("%64s", sha256Hash).replace(' ', '0');
            md5Hash = String.format("%32s", md5Hash).replace(' ', '0');

            return new Pair<>(sha256Hash, md5Hash);
        }
        catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
                fileReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.example.yaralyze01.ui.home;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.yaralyze01.BackgroundTask;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.home.HomeFragment;

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
                this.installedApps.add(new AppDetails(packageInfo, this.packageManager));
            }
        }
    }
}

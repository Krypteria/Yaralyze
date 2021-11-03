package com.example.yaralyze01.ui.analysis;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.yaralyze01.ui.analysis.AppDetails;
import com.example.yaralyze01.ui.analysis.BackgroundTask;
import com.example.yaralyze01.ui.analysis.staticAnalysis.StaticAnalysisActivity;
import com.example.yaralyze01.ui.analysis.staticAnalysis.StaticAnalysisMainMenuFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class GetInstalledAppsTask extends BackgroundTask {
    private StaticAnalysisMainMenuFragment fragment;
    private ArrayList<AppDetails> apps;
    private PackageManager packageManager;

    public GetInstalledAppsTask(StaticAnalysisMainMenuFragment fragment){
        super(fragment);
        this.fragment = fragment;
        this.apps = new ArrayList<AppDetails>();
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
        this.fragment.GetAllAppsTaskCallback(this.apps);
    }

    private void getInstalledAppsIntent(boolean getSysPackages){
        for(PackageInfo packageInfo : this.packageManager.getInstalledPackages(0)){
            if(this.packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null){
                if((!getSysPackages) && (packageInfo.versionName == null)){
                    continue;
                }

                this.apps.add(new AppDetails(packageInfo.applicationInfo.loadLabel(this.packageManager).toString(),
                        packageInfo.packageName, packageInfo.versionName, packageInfo.applicationInfo.loadIcon(this.packageManager), packageInfo.applicationInfo.sourceDir,
                            packageInfo.firstInstallTime, packageInfo.lastUpdateTime));
            }
        }
    }
}

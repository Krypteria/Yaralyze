package com.example.yaralyze01.WIP;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.yaralyze01.ui.analysis.staticAnalysis.StaticAnalysisActivity;

import java.util.ArrayList;

public class GetAllAppsTask extends BackgroundTask {
    private StaticAnalysisActivity appsActivity;
    private ArrayList<AppDetails> apps;
    private PackageManager packageManager;

    public GetAllAppsTask(StaticAnalysisActivity activity){
        super(activity);
        this.appsActivity = activity;
        this.apps = new ArrayList<AppDetails>();
        this.packageManager = this.activity.getPackageManager();
    }

    @Override
    public void doInBackground() {
        this.getAllAppsIntent();
    }

    @Override
    public void onPostExecute() {
        //this.appsActivity.GetAllAppsTaskCallback(this.apps);
    }

    private void getAllAppsIntent(){
        for(ApplicationInfo applicationInfo : this.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)){
            try{
                if(this.packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null){
                    System.out.println(applicationInfo.name + " - " + applicationInfo.packageName + " - " + applicationInfo.dataDir + " - " + applicationInfo.sourceDir);
                    this.apps.add(new AppDetails(applicationInfo.name, applicationInfo.icon));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

package com.example.yaralyze01;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class GetAllAppsTask extends BackgroundTask{
    private List<ApplicationInfo> apps;
    private PackageManager packageManager;

    public GetAllAppsTask(AppsActivity activity){
        super(activity);
        this.apps = new ArrayList<ApplicationInfo>();
        this.packageManager = this.activity.getPackageManager();
    }

    @Override
    public void doInBackground() {
        this.getAllAppsIntent();
    }

    @Override
    public void onPostExecute() {
        //Actualizo la GUI de Apps
    }

    private void getAllAppsIntent(){
        for(ApplicationInfo applicationInfo : this.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)){
            try{
                if(this.packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null){
                    this.apps.add(applicationInfo);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

package com.example.yaralyze01.ui.analysis;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.yaralyze01.ui.analysis.AppDetails;
import com.example.yaralyze01.ui.analysis.BackgroundTask;
import com.example.yaralyze01.ui.analysis.staticAnalysis.StaticAnalysisActivity;
import com.example.yaralyze01.ui.analysis.staticAnalysis.StaticAnalysisMainMenuFragment;

import java.util.ArrayList;

public class GetInstalledAppsTask extends BackgroundTask {
    private StaticAnalysisMainMenuFragment fragment;
    private ArrayList<AppDetails> apps;
    private PackageManager packageManager;

    //Esto luego lo tendré que cambiar para que solo acepte el fragmento correspondiente a la lista de apps
    public GetInstalledAppsTask(StaticAnalysisMainMenuFragment fragment){
        super(fragment);
        this.fragment = fragment;
        this.apps = new ArrayList<AppDetails>();
        this.packageManager = this.fragment.getActivity().getPackageManager();
    }

    //Función de lanzamiento
    public void startOnBackground(){
        this.startBackground();
    }

    @Override
    public void doInBackground() {
        this.getInstalledAppsIntent();
    }

    @Override
    public void onPostExecute() {
        this.fragment.GetAllAppsTaskCallback(this.apps);
    }

    private void getInstalledAppsIntent(){
        for(ApplicationInfo applicationInfo : this.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)){
            try{
                if(this.packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null){
                    //MIRAR QUE ME INTERESA DE AQUÍ
                    System.out.println(applicationInfo.name + " - " + applicationInfo.packageName + " - " + applicationInfo.dataDir + " - " + applicationInfo.sourceDir);
                    //this.apps.add(new AppDetails(applicationInfo.name, applicationInfo.icon));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

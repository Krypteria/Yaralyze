package com.example.yaralyze01.ui.analysis.staticAnalysis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;


public class StaticAnalysisMainMenuFragment extends Fragment {

    //private ArrayList<AppDetails> apps;
    //private RecyclerView recyclerApps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_static_analysis_main_menu, container, false);
         /*this.apps = new ArrayList<AppDetails>();
         this.recyclerApps = findViewById(R.id.recyclerViewApps);

        //Lleno la lista
        new GetAllAppsTask(AppsActivity.this).doInBackground();*/

        //Al lanzarse en otro thread seguramente me de problemas
         /*@Override
        protected void onStart(){
        super.onStart();

        //Lo llamo en onStart para que se actualice cuando la iniciemos
        new GetAllAppsTask(AppsActivity.this).doInBackground();
    }*/

        /*public void GetAllAppsTaskCallback(ArrayList<AppDetails> apps){
        this.apps = apps;

        //Enlazo el adaptador al recycler
        AppsAdapter appsAdapter = new AppsAdapter(this.apps);
        this.recyclerApps.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerApps.setAdapter(appsAdapter);
        }*/
    }
}
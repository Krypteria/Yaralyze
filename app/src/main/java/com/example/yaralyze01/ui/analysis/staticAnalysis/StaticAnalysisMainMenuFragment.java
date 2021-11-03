package com.example.yaralyze01.ui.analysis.staticAnalysis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.AppsAdapter;
import com.example.yaralyze01.ui.analysis.AppDetails;
import com.example.yaralyze01.ui.analysis.GetInstalledAppsTask;

import java.util.ArrayList;


public class StaticAnalysisMainMenuFragment extends Fragment {

    private ArrayList<AppDetails> apps;
    private RecyclerView recyclerApps;
    private AppsAdapter appsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.apps = new ArrayList<AppDetails>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_static_analysis_main_menu, container, false);

        this.recyclerApps = view.findViewById(R.id.recyclerViewApps);
        //Enlazo el adaptador al recycler
        this.appsAdapter = new AppsAdapter();
        this.recyclerApps.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        this.recyclerApps.setAdapter(appsAdapter);

        //Lleno la lista (Al lanzarse en otro thread seguramente me de problemas)
        new GetInstalledAppsTask(this).startOnBackground();

        return view;
    }

    public void GetAllAppsTaskCallback(ArrayList<AppDetails> apps){
        this.apps = apps;
        this.appsAdapter.updateData(apps);
        this.appsAdapter.notifyDataSetChanged();
    }
}
package com.example.yaralyze01.ui.analysis.staticAnalysis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.AnalysisCallbackInterface;
import com.example.yaralyze01.ui.analysis.AppsAdapter;
import com.example.yaralyze01.ui.analysis.AppDetails;
import com.example.yaralyze01.ui.analysis.GetInstalledAppsTask;

import java.util.ArrayList;


public class StaticAnalysisMainMenuFragment extends Fragment implements OnAppListener{

    private ArrayList<AppDetails> apps;
    private RecyclerView recyclerApps;
    private AppsAdapter appsAdapter;
    private AnalysisCallbackInterface analysisCallbackInterface;

    public StaticAnalysisMainMenuFragment(AnalysisCallbackInterface analysisCallbackInterface){
        this.analysisCallbackInterface = analysisCallbackInterface;
        this.apps = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetInstalledAppsTask(this).startOnBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_static_analysis_main_menu, container, false);

        this.recyclerApps = view.findViewById(R.id.recyclerViewApps);

        this.appsAdapter = new AppsAdapter(this);
        this.recyclerApps.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        this.recyclerApps.setAdapter(appsAdapter);

        return view;
    }

    public void GetAllAppsTaskCallback(ArrayList<AppDetails> apps){
        this.apps = apps;
        this.appsAdapter.updateData(this.apps);
        this.appsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAppClick(int position) {
        this.apps.get(position).calculateHashes();
        this.analysisCallbackInterface.callBackMethod(this.apps.get(position));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.appsAdapter.updateData(this.apps);
        this.appsAdapter.notifyDataSetChanged();
    }
}
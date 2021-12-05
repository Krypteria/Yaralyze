package com.example.yaralyze01.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.installedApps.InstalledAppsFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Button staticAnalysisButton;
    private ArrayList<AppDetails> installedApps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Cargo en background las aplicaciones instaladas en el dispositivo para tenerlas disponibles en el siguiente fragmento
        new GetInstalledAppsTask(this).startOnBackground();

        this.staticAnalysisButton = view.findViewById(R.id.staticAnalysisButton);
        this.staticAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstalledAppsFragment fragment = new InstalledAppsFragment(installedApps);
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
            }
        });

        return view;
    }

    public void installedAppsTaskCallback(ArrayList<AppDetails> apps){
        this.installedApps = apps;
        /*this.appsAdapter.updateData(this.installedApps);
        this.appsAdapter.notifyDataSetChanged();*/
    }
}
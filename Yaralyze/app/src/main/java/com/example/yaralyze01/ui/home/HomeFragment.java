package com.example.yaralyze01.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.installedApps.AppsAdapter;
import com.example.yaralyze01.ui.analysis.installedApps.InstalledAppsFragment;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;
import com.example.yaralyze01.ui.analysis.reports.ReportTabbedFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnAppListener {

    private Button analyzeAppsButton;
    private Button reportsButton;
    private TextView lastAnalysisDate;
    private RecyclerView lastAnalyzedApps;
    private ArrayList<AppDetails> installedApps;

    public HomeFragment(ArrayList<AppDetails> installedApps){
        this.installedApps = installedApps;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.analyzeAppsButton = view.findViewById(R.id.analyzeAppsButton);
        this.reportsButton = view.findViewById(R.id.reportsButton);
        this.lastAnalysisDate = view.findViewById(R.id.lastAnalysisDate);
        this.lastAnalyzedApps = view.findViewById(R.id.lastAnalyzedApps);

        YaralyzeDB db = YaralyzeDB.getInstance(getContext());
        String date = db.getLastAnalysisDate();

        if(date == null){
            this.lastAnalysisDate.setText("Todavía no se ha realizado ningún análisis");
        }
        else{
            this.lastAnalysisDate.setText(date);
        }

        LastAppsAdapter lastAppsAdapter = new LastAppsAdapter(this);
        this.lastAnalyzedApps.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        this.lastAnalyzedApps.setNestedScrollingEnabled(false);
        this.lastAnalyzedApps.setAdapter(lastAppsAdapter);

        lastAppsAdapter.updateData(db.getLastAnalyzedAppsIcons());

        this.analyzeAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstalledAppsFragment fragment = new InstalledAppsFragment(installedApps);

                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
            }
        });

        this.reportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportTabbedFragment fragment = new ReportTabbedFragment(installedApps);

                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView toolbarText = getActivity().findViewById(R.id.toolbarText);
        toolbarText.setText("Menú principal");
    }

    @Override
    public void onAppClick(int position) { }
}
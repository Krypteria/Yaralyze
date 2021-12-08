package com.example.yaralyze01.ui.home;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.installedApps.InstalledAppsFragment;

public class HomeFragment extends Fragment {

    private Button analyzeAppsButton;
    private TextView lastAnalysisDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.analyzeAppsButton = view.findViewById(R.id.analyzeAppsButton);
        this.lastAnalysisDate = view.findViewById(R.id.lastAnalysisDate);

        YaralyzeDB db = YaralyzeDB.getInstance(getContext());
        String date = db.getLastAnalysisDate();

        if(date == null){
            this.lastAnalysisDate.setText("Todavía no se ha realizado ningún análisis");
        }
        else{
            this.lastAnalysisDate.setText(date);
        }

        this.analyzeAppsButton.setOnClickListener(new View.OnClickListener() {
            private HomeFragment homeFragment;
            private PackageManager packageManager;

            @Override
            public void onClick(View v) {
                InstalledAppsFragment fragment = new InstalledAppsFragment();
                new GetInstalledAppsTask((MainActivity) this.homeFragment.getActivity(), fragment, this.packageManager).startOnBackground();

                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
            }

            private View.OnClickListener getPackageManager(HomeFragment fragment){
                this.homeFragment = fragment;
                this.packageManager = fragment.getActivity().getPackageManager();
                return this;
            }
        }.getPackageManager(this));

        return view;
    }
}
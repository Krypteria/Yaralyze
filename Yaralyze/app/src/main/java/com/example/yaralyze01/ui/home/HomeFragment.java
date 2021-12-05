package com.example.yaralyze01.ui.home;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.installedApps.InstalledAppsFragment;

public class HomeFragment extends Fragment {

    private Button staticAnalysisButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        this.staticAnalysisButton = view.findViewById(R.id.staticAnalysisButton);
        this.staticAnalysisButton.setOnClickListener(new View.OnClickListener() {
            private PackageManager packageManager;

            @Override
            public void onClick(View v) {
                InstalledAppsFragment fragment = new InstalledAppsFragment();
                new GetInstalledAppsTask(fragment, this.packageManager).startOnBackground();

                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
            }

            private View.OnClickListener getPackageManager(HomeFragment fragment){
                this.packageManager = fragment.getActivity().getPackageManager();
                return this;
            }
        }.getPackageManager(this));

        return view;
    }
}
package com.example.yaralyze01.ui.loading;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.installedApps.InstalledAppsFragment;
import com.example.yaralyze01.ui.home.GetInstalledAppsTask;
import com.example.yaralyze01.ui.home.HomeFragment;

import java.util.ArrayList;

public class LoadingAppFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading_app, container, false);
        new GetInstalledAppsTask(this, this.getActivity().getPackageManager()).startOnBackground();
        return view;
    }

    public void loadingComplete(ArrayList<AppDetails> installedApps){
        HomeFragment fragment = new HomeFragment(installedApps);

        FragmentManager manager = getParentFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
    }
}
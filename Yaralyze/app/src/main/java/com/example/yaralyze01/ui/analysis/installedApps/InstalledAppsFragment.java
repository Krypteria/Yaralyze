package com.example.yaralyze01.ui.analysis.installedApps;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetailsFragment;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;

import java.util.ArrayList;


public class InstalledAppsFragment extends Fragment implements OnAppListener{

    private ArrayList<AppDetails> installedApps;
    private RecyclerView recyclerApps;
    private AppsAdapter appsAdapter;

    public InstalledAppsFragment(){
        this.installedApps = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_installed_apps, container, false);

        this.recyclerApps = view.findViewById(R.id.recyclerViewApps);

        this.appsAdapter = new AppsAdapter(this);
        this.recyclerApps.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        this.recyclerApps.setAdapter(appsAdapter);

        this.appsAdapter.updateData(this.installedApps);

        return view;
    }

    @Override
    public void onAppClick(int position) {
        AppDetailsFragment fragment = new AppDetailsFragment(this.installedApps.get(position));
        FragmentManager manager = getParentFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.appsAdapter.updateData(this.installedApps);
    }

    public void onStepUpdateInstalledAppsList(AppDetails installedApp){
        this.appsAdapter.addItem(installedApp);
    }
}
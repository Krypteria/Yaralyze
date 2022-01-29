package com.example.yaralyze01.ui.reports;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetailsFragment;
import com.example.yaralyze01.ui.analysis.installedApps.AppsAdapter;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;

import java.util.ArrayList;

public class ReportListFragment extends Fragment implements OnAppListener {

    private final static int HASH = 0;
    private final static int STATIC = 1;
    private final static int COMPLETE = 2;

    private RecyclerView recyclerApps;
    private ReportsAdapter reportsAdapter;

    private int reportType;
    private ArrayList<Report> reports;
    private ArrayList<AppDetails> installedApps;

    public ReportListFragment(int reportType, ArrayList<AppDetails> installedApps){
        this.reportType = reportType;
        this.reports = new ArrayList<>();
        this.installedApps = installedApps;

        getReports();
    }

    private void getReports(){
        YaralyzeDB db = YaralyzeDB.getInstance(getContext());
        switch(reportType){
            case HASH:
                this.reports = db.getReports(HASH);
                break;
            case STATIC:
                this.reports = db.getReports(STATIC);
                break;
            case COMPLETE:
                this.reports = db.getReports(COMPLETE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        this.recyclerApps = view.findViewById(R.id.recyclerViewReports);

        this.reportsAdapter = new ReportsAdapter(this);
        this.recyclerApps.setLayoutManager(new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false));
        this.recyclerApps.setAdapter(reportsAdapter);

        System.out.println(this.reports.size());
        this.reportsAdapter.updateData(this.reports);

        return view;
    }

    @Override
    public void onAppClick(int position) {
        AppDetails appDetails = findAppDetailsByReport(this.reports.get(position));
        switch(this.reportType){
            case HASH:
                break;
            case STATIC:
                break;
            case COMPLETE:
                break;
            default:
                break;
        }
        //Crear página con detailed report

        /*AppDetailsFragment fragment = new AppDetailsFragment(this.installedApps.get(position));
        FragmentManager manager = getParentFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack("InstalledAppsFragment").commit();*/
    }

    private AppDetails findAppDetailsByReport(Report report){
        AppDetails selectedAppDetails = null;

        for(AppDetails appDetails : this.installedApps){
            if(report.getAppPackage().equals(appDetails.getPackageName())){
                selectedAppDetails = appDetails;
                break;
            }
        }

        return selectedAppDetails;
    }
}
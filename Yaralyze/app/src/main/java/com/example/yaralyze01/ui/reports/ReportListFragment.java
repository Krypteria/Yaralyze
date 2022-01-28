package com.example.yaralyze01.ui.reports;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;
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

    public ReportListFragment(int reportType){
        this.reportType = reportType;
        this.reports = new ArrayList<>();
        getReports();
    }

    //Llamadas a la db para recuperar una lista de tipo report dependiendo del tipo de reporte
    private void getReports(){
        switch(reportType){
            case HASH:
                System.out.println("HASH");
                break;
            case STATIC:
                System.out.println("STATIC");
                break;
            case COMPLETE:
                System.out.println("COMPLETE");
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
        this.recyclerApps.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        this.recyclerApps.setAdapter(reportsAdapter);

        this.reportsAdapter.updateData(this.reports);

        return view;
    }

    @Override
    public void onAppClick(int position) {
        //Crear página con detailed report

        /*AppDetailsFragment fragment = new AppDetailsFragment(this.installedApps.get(position));
        FragmentManager manager = getParentFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack("InstalledAppsFragment").commit();*/
    }
}
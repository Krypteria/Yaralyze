package com.example.yaralyze01.ui.analysis.reports;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;
import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcome;
import com.example.yaralyze01.ui.analysis.outcomes.HashAnalysisOutcomeFragment;
import com.example.yaralyze01.ui.analysis.outcomes.StaticAnalysisOutcomeFragment;
import com.example.yaralyze01.ui.common.AnalysisType;

import java.util.ArrayList;

public class ReportListFragment extends Fragment implements OnAppListener {

    private RecyclerView recyclerApps;
    private ReportsAdapter reportsAdapter;

    private int reportType;
    private ArrayList<AnalysisOutcome> analysisOutcomes;
    private ArrayList<AppDetails> installedApps;

    public ReportListFragment(int reportType, ArrayList<AppDetails> installedApps){
        this.reportType = reportType;
        this.analysisOutcomes = new ArrayList<>();
        this.installedApps = installedApps;

        getReports();
    }

    private void getReports(){
        YaralyzeDB db = YaralyzeDB.getInstance(getContext());
        switch(reportType){
            case AnalysisType.HASH:
                this.analysisOutcomes = db.getReports(AnalysisType.HASH);
                break;
            case AnalysisType.STATIC:
                this.analysisOutcomes = db.getReports(AnalysisType.STATIC);
                break;
            case AnalysisType.COMPLETE:
                this.analysisOutcomes = db.getReports(AnalysisType.COMPLETE);
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

        this.reportsAdapter.updateData(this.analysisOutcomes);

        return view;
    }

    @Override
    public void onAppClick(int position) {
        AppDetails appDetails = findAppDetailsByReport(this.analysisOutcomes.get(position));
        FragmentManager manager = getParentFragmentManager();
        switch(this.reportType){
            case AnalysisType.HASH:
                HashAnalysisOutcomeFragment fragment = new HashAnalysisOutcomeFragment(appDetails, this.analysisOutcomes.get(position));
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
                break;
            case AnalysisType.STATIC:
                StaticAnalysisOutcomeFragment staticFragment = new StaticAnalysisOutcomeFragment(appDetails, this.analysisOutcomes.get(position));
                manager.beginTransaction().replace(R.id.fragmentContainer, staticFragment, staticFragment.getTag()).addToBackStack(null).commit();
                break;
            case AnalysisType.COMPLETE:
                break;
            default:
                break;
        }
    }

    private AppDetails findAppDetailsByReport(AnalysisOutcome analysisOutcome){
        AppDetails selectedAppDetails = null;

        for(AppDetails appDetails : this.installedApps){
            if(analysisOutcome.getAnalyzedAppPackage().equals(appDetails.getPackageName())){
                selectedAppDetails = appDetails;
                break;
            }
        }

        return selectedAppDetails;
    }
}
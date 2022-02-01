package com.example.yaralyze01.ui.analysis.reports;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.common.AnalysisType;

import java.util.ArrayList;

public class ReportViewPageAdapter extends FragmentStateAdapter {

    private ArrayList<AppDetails> installedApps;

    public ReportViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setInstalledApps(ArrayList<AppDetails> installedApps) {
        this.installedApps = installedApps;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;

        switch(position) {
            case AnalysisType.HASH:
                fragment = new ReportListFragment(AnalysisType.HASH, this.installedApps);
                break;
            case AnalysisType.STATIC:
                fragment = new ReportListFragment(AnalysisType.STATIC, this.installedApps);
                break;
            case AnalysisType.COMPLETE:
                fragment = new ReportListFragment(AnalysisType.COMPLETE, this.installedApps);
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

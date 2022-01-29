package com.example.yaralyze01.ui.reports;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;

import java.util.ArrayList;

public class ReportViewPageAdapter extends FragmentStateAdapter {

    private final static int HASH = 0;
    private final static int STATIC = 1;
    private final static int COMPLETE = 2;

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
            case HASH:
                fragment = new ReportListFragment(HASH, this.installedApps);
                break;
            case STATIC:
                fragment = new ReportListFragment(STATIC, this.installedApps);
                break;
            case COMPLETE:
                fragment = new ReportListFragment(COMPLETE, this.installedApps);
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

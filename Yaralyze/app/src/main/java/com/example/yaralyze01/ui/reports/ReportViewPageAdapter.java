package com.example.yaralyze01.ui.reports;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReportViewPageAdapter extends FragmentStateAdapter {

    private final static int HASH = 0;
    private final static int STATIC = 1;
    private final static int COMPLETE = 2;

    public ReportViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;

        switch(position) {
            case HASH:
                fragment = new ReportListFragment(HASH);
                break;
            case STATIC:
                fragment = new ReportListFragment(STATIC);
                break;
            case COMPLETE:
                fragment = new ReportListFragment(COMPLETE);
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

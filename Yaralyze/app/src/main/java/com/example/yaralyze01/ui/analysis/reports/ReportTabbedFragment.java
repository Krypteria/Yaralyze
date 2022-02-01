package com.example.yaralyze01.ui.analysis.reports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.common.AnalysisType;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class ReportTabbedFragment extends Fragment {

    private ArrayList<AppDetails> installedApps;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    public ReportTabbedFragment(ArrayList<AppDetails> installedApps){
        this.installedApps = installedApps;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_tabbed, container, false);

        this.tabLayout = view.findViewById(R.id.tabLayout);
        this.viewPager2 = view.findViewById(R.id.view_pager);

        ReportViewPageAdapter adapter = new ReportViewPageAdapter(getActivity());
        adapter.setInstalledApps(this.installedApps);

        this.viewPager2.setAdapter(adapter);

        new TabLayoutMediator(this.tabLayout, this.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case AnalysisType.COMPLETE:
                        tab.setText("Análisis completo");
                        break;
                    case AnalysisType.STATIC:
                        tab.setText("Análisis estático");
                        break;
                    case AnalysisType.HASH:
                        tab.setText("Análisis del hash");
                        break;
                    default:
                        break;
                }
            }
        }).attach();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView toolbarText = getActivity().findViewById(R.id.toolbarText);
        toolbarText.setText("Informes");
    }
}
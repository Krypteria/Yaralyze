package com.example.yaralyze01.ui.reports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ReportTabbedFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

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
        this.viewPager2.setAdapter(adapter);

        new TabLayoutMediator(this.tabLayout, this.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("Análisis del hash");
                        break;
                    case 1:
                        tab.setText("Análisis estático");
                        break;

                    default:
                        tab.setText("Análisis completo");
                        break;
                }
            }
        }).attach();

        return view;
    }
}
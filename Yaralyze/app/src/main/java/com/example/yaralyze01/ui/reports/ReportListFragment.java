package com.example.yaralyze01.ui.reports;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yaralyze01.R;

import java.util.ArrayList;

public class ReportListFragment extends Fragment {

    private final static int HASH = 0;
    private final static int STATIC = 1;
    private final static int COMPLETE = 2;

    private int reportType;
    private ArrayList<Report> reports;

    public ReportListFragment(int reportType){
        this.reportType = reportType;
        this.reports = new ArrayList<>();
        getReports();
    }

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
        return view;
    }
}
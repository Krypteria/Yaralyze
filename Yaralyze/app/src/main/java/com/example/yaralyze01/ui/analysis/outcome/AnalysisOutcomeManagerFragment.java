package com.example.yaralyze01.ui.analysis.outcome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.yaralyze01.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class AnalysisOutcomeManagerFragment extends Fragment implements AnalysisOutcome{

    private final int HASH = 0;
    private final int STATIC = 1;
    private final int COMPLETE = 2;

    private int analysisType;

    private ProgressBar analyzeProgressBar;

    public AnalysisOutcomeManagerFragment(int analysisType){
        this.analysisType = analysisType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis_outcome, container, false);
        this.analyzeProgressBar = view.findViewById(R.id.analyzeProgressBar);

        return view;
    }

    @Override
    public void showAnalysisOutcome(JSONObject analysisOutcome) {
        switch (this.analysisType){
            case(HASH):
                System.out.println("hash");
                break;
            case(STATIC):
                StaticAnalysisOutcomeFragment fragment = new StaticAnalysisOutcomeFragment(analysisOutcome);
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
                break;
            case(COMPLETE):
                System.out.println("Completo");
            default:
                break;
        }
    }
}
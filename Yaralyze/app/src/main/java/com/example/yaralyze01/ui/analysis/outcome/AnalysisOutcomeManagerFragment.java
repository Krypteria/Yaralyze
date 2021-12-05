package com.example.yaralyze01.ui.analysis.outcome;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;

import org.json.JSONArray;
import org.json.JSONObject;


public class AnalysisOutcomeManagerFragment extends Fragment implements AnalysisOutcome{

    private final int HASH = 0;
    private final int STATIC = 1;
    private final int COMPLETE = 2;

    private AppDetails appDetails;

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;
    private int analysisType;

    private ProgressBar analyzeProgressBar;

    public AnalysisOutcomeManagerFragment(AppDetails appDetails, int analysisType){
        this.analysisType = analysisType;
        this.appDetails = appDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis_outcome, container, false);

        this.appIcon = view.findViewById(R.id.appIcon);
        this.appName = view.findViewById(R.id.appName);
        this.appVersion = view.findViewById(R.id.appVersion);
        this.analyzeProgressBar = view.findViewById(R.id.analyzeProgressBar);

        this.appIcon.setImageDrawable(this.appDetails.getAppIcon());
        this.appName.setText(this.appDetails.getAppName());
        this.appVersion.setText(this.appDetails.getAppVersion());

        return view;
    }

    @Override
    public void showAnalysisOutcome(JSONObject analysisOutcome) {
        switch (this.analysisType){
            case(HASH):
                System.out.println("hash");
                break;
            case(STATIC):
                StaticAnalysisOutcomeFragment fragment = new StaticAnalysisOutcomeFragment(this.appDetails, analysisOutcome);
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
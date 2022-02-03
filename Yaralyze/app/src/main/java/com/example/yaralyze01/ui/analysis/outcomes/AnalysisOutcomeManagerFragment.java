package com.example.yaralyze01.ui.analysis.outcomes;

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
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.common.AnalysisType;

public class AnalysisOutcomeManagerFragment extends Fragment implements AnalysisOutcomeManagement {
    private AppDetails appDetails;

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;
    private int analysisType;

    private ProgressBar analyzeProgressBar;
    private FragmentManager manager;

    public AnalysisOutcomeManagerFragment(FragmentManager manager, AppDetails appDetails, int analysisType){
        this.manager = manager;
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
    public void showAnalysisOutcome(AnalysisOutcome analysisOutcome) {
        insertSimpleAnalysisIntoDB(analysisOutcome);
        switch (this.analysisType){
            case(AnalysisType.HASH):
                HashAnalysisOutcomeFragment hashFragment = new HashAnalysisOutcomeFragment(this.appDetails, analysisOutcome);
                this.manager.popBackStack("waiting", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                this.manager.beginTransaction().replace(R.id.fragmentContainer, hashFragment, hashFragment.getTag()).addToBackStack(null).commit();
                break;
            case(AnalysisType.STATIC):
                StaticAnalysisOutcomeFragment staticFragment = new StaticAnalysisOutcomeFragment(this.appDetails, analysisOutcome);
                this.manager.popBackStack("waiting", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                this.manager.beginTransaction().replace(R.id.fragmentContainer, staticFragment, staticFragment.getTag()).addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void showAnalysisOutcome(AnalysisOutcome staticOutcome, AnalysisOutcome hashOutcome) {
         if(this.analysisType == AnalysisType.COMPLETE){
             insertCompleteAnalysisIntoDB(staticOutcome, hashOutcome);
             CompleteAnalysisOutcome completeFragment = new CompleteAnalysisOutcome(this.appDetails, staticOutcome, hashOutcome);
             this.manager.popBackStack("waiting", FragmentManager.POP_BACK_STACK_INCLUSIVE);
             this.manager.beginTransaction().replace(R.id.fragmentContainer, completeFragment, completeFragment.getTag()).addToBackStack(null).commit();
         }
    }

    private void insertSimpleAnalysisIntoDB(AnalysisOutcome analysisOutcome){
        YaralyzeDB db = YaralyzeDB.getInstance(getContext());
        db.insertAnalysisOutcome(analysisOutcome);
    }

    private void insertCompleteAnalysisIntoDB(AnalysisOutcome staticAnalysisOutcome, AnalysisOutcome hashAnalysisOutcome){
        YaralyzeDB db = YaralyzeDB.getInstance(getContext());
        db.insertCompleteAnalysisOutcome(staticAnalysisOutcome, hashAnalysisOutcome);
    }
}
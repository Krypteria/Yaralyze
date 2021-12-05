package com.example.yaralyze01.ui.analysis.outcome;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;

import org.json.JSONException;
import org.json.JSONObject;


public class StaticAnalysisOutcomeFragment extends Fragment{

    private AppDetails appDetails;

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;

    private JSONObject analysisOutcome;
    private TextView malwareDetectionText;
    private TextView ruleCoincidenceText;
    private TextView outcomeLogText;

    public StaticAnalysisOutcomeFragment(AppDetails appDetails, JSONObject analysisOutcome){
        this.analysisOutcome = analysisOutcome;
        this.appDetails = appDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_static_analysis_outcome, container, false);

        this.appIcon = view.findViewById(R.id.appIcon);
        this.appName = view.findViewById(R.id.appName);
        this.appVersion = view.findViewById(R.id.appVersion);

        this.malwareDetectionText = view.findViewById(R.id.malwareDetectionText);
        this.ruleCoincidenceText = view.findViewById(R.id.ruleCoincidenceText);

        this.appIcon.setImageDrawable(this.appDetails.getAppIcon());
        this.appName.setText(this.appDetails.getAppName());
        this.appVersion.setText(this.appDetails.getAppVersion());

        //System.out.println(analysisOutcome.get("matchedRulesCount"));
        //JSONArray matchedRules = (JSONArray) analysisOutcome.get("matchedRules");
        //System.out.println(matchedRules.get(0));

        try {
            if(this.analysisOutcome.get("detected") == "1"){
                this.malwareDetectionText.setText("Malware detectado");
                this.malwareDetectionText.setTextColor(Color.parseColor("#A62424"));
            }
            else{
                this.malwareDetectionText.setText("Malware no detectado");
                this.malwareDetectionText.setTextColor(Color.parseColor("#4CAF50"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
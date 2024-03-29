package com.example.yaralyze01.ui.analysis.outcomes;

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

public class StaticAnalysisOutcomeFragment extends Fragment{

    private AppDetails appDetails;

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;

    private AnalysisOutcome analysisOutcome;
    private TextView malwareDetectionText;
    private TextView ruleCoincidenceText;

    public StaticAnalysisOutcomeFragment(AppDetails appDetails, AnalysisOutcome analysisOutcome){
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

        this.malwareDetectionText = view.findViewById(R.id.staticMalwareDetectionText);
        this.ruleCoincidenceText = view.findViewById(R.id.ruleCoincidenceText);

        this.appIcon.setImageDrawable(this.appDetails.getAppIcon());
        this.appName.setText(this.appDetails.getAppName());
        this.appVersion.setText(this.appDetails.getAppVersion());


        if(this.analysisOutcome.isMalwareDetected()){
            this.malwareDetectionText.setText("Malware detectado.");

            int i = 0;
            String outcomeMatchedRules = "El programa analizado coincide con las siguientes reglas: \n\n";
            for(String rule : this.analysisOutcome.getMatchedRules()){
                outcomeMatchedRules += i + 1 + ". " + rule + "\n";
                i++;
            }
            this.ruleCoincidenceText.setText(outcomeMatchedRules);

            this.malwareDetectionText.setTextColor(Color.parseColor("#A62424"));
            this.ruleCoincidenceText.setTextColor(Color.parseColor("#A62424"));
        }
        else{
            this.malwareDetectionText.setText("Malware no detectado");
            this.ruleCoincidenceText.setText("No hay coincidencias con ninguna regla.");
            this.malwareDetectionText.setTextColor(Color.parseColor("#4CAF50"));
            this.ruleCoincidenceText.setTextColor(Color.parseColor("#4CAF50"));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView toolbarText = getActivity().findViewById(R.id.toolbarText);
        toolbarText.setText("Resultado del análisis");
    }
}
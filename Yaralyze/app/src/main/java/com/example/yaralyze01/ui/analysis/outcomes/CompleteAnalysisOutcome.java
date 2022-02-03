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


public class CompleteAnalysisOutcome extends Fragment {

    private AppDetails appDetails;
    private AnalysisOutcome staticAnalysisOutcome;
    private AnalysisOutcome hashAnalysisOutcome;

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;

    private TextView completeMalwareDetectionText;
    private TextView staticMalwareDetectionText;
    private TextView hashMalwareDetectionText;
    private TextView ruleCoincidenceText;
    private TextView sha256;
    private TextView md5;

    private boolean hashCoincidence;


    public CompleteAnalysisOutcome(AppDetails appDetails, AnalysisOutcome staticOutcome, AnalysisOutcome hashOutcome) {
        this.appDetails = appDetails;
        this.staticAnalysisOutcome = staticOutcome;
        this.hashAnalysisOutcome = hashOutcome;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_analysis_outcome, container, false);

        this.appIcon = view.findViewById(R.id.appIcon);
        this.appName = view.findViewById(R.id.appName);
        this.appVersion = view.findViewById(R.id.appVersion);

        this.sha256 = view.findViewById(R.id.appSha256Hash);
        this.md5 = view.findViewById(R.id.appMd5Hash);

        this.completeMalwareDetectionText = view.findViewById(R.id.completeMalwareDetectionText);
        this.staticMalwareDetectionText = view.findViewById(R.id.staticMalwareDetectionText);
        this.hashMalwareDetectionText = view.findViewById(R.id.hashMalwareDetectionText);
        this.ruleCoincidenceText = view.findViewById(R.id.ruleCoincidenceText);

        this.appIcon.setImageDrawable(this.appDetails.getAppIcon());
        this.appName.setText(this.appDetails.getAppName());
        this.appVersion.setText(this.appDetails.getAppVersion());
        this.sha256.setText(this.appDetails.getSha256hash());
        this.md5.setText(this.appDetails.getMd5hash());

        if(this.staticAnalysisOutcome.isMalwareDetected() || this.hashAnalysisOutcome.isMalwareDetected()){
            this.completeMalwareDetectionText.setText("Malware detectado");
            this.completeMalwareDetectionText.setTextColor(Color.parseColor("#A62424"));
        }
        else{
            this.completeMalwareDetectionText.setText("Malware no detectado");
            this.completeMalwareDetectionText.setTextColor(Color.parseColor("#4CAF50"));
        }

        if(this.staticAnalysisOutcome.isMalwareDetected()){
            this.staticMalwareDetectionText.setText("Malware detectado.");

            int i = 0;
            String outcomeMatchedRules = "El programa analizado coincide con las siguientes reglas: \n\n";
            for(String rule : this.staticAnalysisOutcome.getMatchedRules()){
                outcomeMatchedRules += i + 1 + ". " + rule + "\n";
                i++;
            }
            this.ruleCoincidenceText.setText(outcomeMatchedRules);

            this.staticMalwareDetectionText.setTextColor(Color.parseColor("#A62424"));
            this.ruleCoincidenceText.setTextColor(Color.parseColor("#A62424"));
        }
        else{
            this.staticMalwareDetectionText.setText("Malware no detectado");
            this.ruleCoincidenceText.setText("No hay coincidencias con ninguna regla.");
            this.staticMalwareDetectionText.setTextColor(Color.parseColor("#4CAF50"));
            this.ruleCoincidenceText.setTextColor(Color.parseColor("#4CAF50"));
        }

        if(this.hashCoincidence){
            this.hashMalwareDetectionText.setText("El hash de la aplicación coincide con el hash de una aplicación maliciosa");
            this.hashMalwareDetectionText.setTextColor(Color.parseColor("#A62424"));
        }
        else{
            this.hashMalwareDetectionText.setText("El hash de la aplicación no coincide con el hash de una aplicación maliciosa");
            this.hashMalwareDetectionText.setTextColor(Color.parseColor("#4CAF50"));
        }

        return view;
    }
}
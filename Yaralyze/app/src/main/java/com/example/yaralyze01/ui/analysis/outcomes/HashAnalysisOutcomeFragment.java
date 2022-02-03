package com.example.yaralyze01.ui.analysis.outcomes;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;



public class HashAnalysisOutcomeFragment extends Fragment {

    private AppDetails appDetails;

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;

    private TextView sha256;
    private TextView md5;
    private TextView malwareDetectionText;

    private boolean hashCoincidence;

    public HashAnalysisOutcomeFragment(AppDetails appDetails, AnalysisOutcome analysisOutcome) {
        this.appDetails = appDetails;
        this.hashCoincidence = analysisOutcome.isMalwareDetected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hash_analysis_outcome, container, false);

        this.appIcon = view.findViewById(R.id.appIcon);
        this.appName = view.findViewById(R.id.appName);
        this.appVersion = view.findViewById(R.id.appVersion);

        this.sha256 = view.findViewById(R.id.appSha256Hash);
        this.md5 = view.findViewById(R.id.appMd5Hash);

        this.malwareDetectionText = view.findViewById(R.id.hashMalwareDetectionText);

        this.appIcon.setImageDrawable(this.appDetails.getAppIcon());
        this.appName.setText(this.appDetails.getAppName());
        this.appVersion.setText(this.appDetails.getAppVersion());

        this.sha256.setText(this.appDetails.getSha256hash());
        this.md5.setText(this.appDetails.getMd5hash());

        if(this.hashCoincidence){
            this.malwareDetectionText.setText("El hash de la aplicación coincide con el hash de una aplicación maliciosa");
            this.malwareDetectionText.setTextColor(Color.parseColor("#A62424"));
        }
        else{
            this.malwareDetectionText.setText("El hash de la aplicación no coincide con el hash de una aplicación maliciosa");
            this.malwareDetectionText.setTextColor(Color.parseColor("#4CAF50"));
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
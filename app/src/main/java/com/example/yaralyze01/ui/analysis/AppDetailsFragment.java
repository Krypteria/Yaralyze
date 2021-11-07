package com.example.yaralyze01.ui.analysis;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.client.Client;

public class AppDetailsFragment extends Fragment {

    private AppDetails appDetails;
    private ImageView appIcon;
    private TextView appName;
    private TextView packageName;
    private TextView appVersion;
    private TextView firstTimeInstalled;
    private TextView lastTimeUpdated;
    private Button analyzeButton;

    public AppDetailsFragment(AppDetails appDetails){
        this.appDetails = appDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_details_fragments, container, false);

        this.appIcon = view.findViewById(R.id.appIcon);
        this.appName = view.findViewById(R.id.appName);
        this.packageName = view.findViewById(R.id.packageName);
        this.appVersion = view.findViewById(R.id.appVersion);
        this.firstTimeInstalled = view.findViewById(R.id.firstTimeInstalled);
        this.lastTimeUpdated = view.findViewById(R.id.lastTimeUpdated);
        this.analyzeButton = view.findViewById(R.id.analyzeButton);

        this.analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Client(appDetails.getAppName(), appDetails.getAppSrc())).start();
            }
        });

        this.appIcon.setImageDrawable(this.appDetails.getAppIcon());
        this.appName.setText(this.appDetails.getAppName());
        this.packageName.setText(this.appDetails.getPackageName());
        this.appVersion.setText(this.appDetails.getAppVersion());
        this.firstTimeInstalled.setText(this.appDetails.getFirstTimeInstalledDate());
        this.lastTimeUpdated.setText(this.appDetails.getLastTimeUpdatedDate());

        return view;
    }
}
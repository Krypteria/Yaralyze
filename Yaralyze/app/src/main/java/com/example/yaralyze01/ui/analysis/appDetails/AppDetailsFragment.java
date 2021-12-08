package com.example.yaralyze01.ui.analysis.appDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.client.Client;
import com.example.yaralyze01.ui.analysis.outcome.AnalysisOutcomeManagerFragment;

public class AppDetailsFragment extends Fragment {

    private final int HASH = 0;
    private final int STATIC = 1;
    private final int COMPLETE = 2;

    private AppDetails appDetails;

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;

    private TextView packageName;
    private TextView sourceDir;
    private TextView firstTimeInstalled;
    private TextView lastTimeUpdated;

    private TextView sha256Hash;
    private TextView md5Hash;

    private Button completeAnalysisButton;
    private Button staticAnalysisButton;
    private Button hashAnalysisButton;

    public AppDetailsFragment(AppDetails appDetails){
        this.appDetails = appDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_details, container, false);

        this.appIcon = view.findViewById(R.id.appIcon);
        this.appName = view.findViewById(R.id.appName);
        this.appVersion = view.findViewById(R.id.appVersion);

        this.packageName = view.findViewById(R.id.packageName);
        this.sourceDir = view.findViewById(R.id.sourceDir);
        this.firstTimeInstalled = view.findViewById(R.id.firstTimeInstalled);
        this.lastTimeUpdated = view.findViewById(R.id.lastTimeUpdated);

        this.sha256Hash = view.findViewById(R.id.appSha256Hash);
        this.md5Hash = view.findViewById(R.id.appMd5Hash);


        this.completeAnalysisButton = view.findViewById(R.id.completeAnalysisButton);
        this.staticAnalysisButton = view.findViewById(R.id.staticAnalysisButton);
        this.hashAnalysisButton = view.findViewById(R.id.hashAnalysisButton);

        this.staticAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalysisOutcomeManagerFragment fragment = new AnalysisOutcomeManagerFragment(getParentFragmentManager(), appDetails, STATIC);
                new Thread(new Client(fragment, appDetails.getAppName(), appDetails.getAppSrc())).start();

                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
            }
        });

        this.hashAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YaralyzeDB db = YaralyzeDB.getInstance(getContext());
                boolean coincidence = db.getCoincidence(appDetails.getMd5hash());

                AnalysisOutcomeManagerFragment fragment = new AnalysisOutcomeManagerFragment(getParentFragmentManager(), appDetails, HASH);
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();

                fragment.showAnalysisOutcome(null, coincidence);
            }
        });

        this.appIcon.setImageDrawable(this.appDetails.getAppIcon());
        this.appName.setText(this.appDetails.getAppName());
        this.appVersion.setText(this.appDetails.getAppVersion());

        this.packageName.setText(this.appDetails.getPackageName());
        this.sourceDir.setText(this.appDetails.getAppSrc());
        this.firstTimeInstalled.setText(this.appDetails.getFirstTimeInstalledDate());
        this.lastTimeUpdated.setText(this.appDetails.getLastTimeUpdatedDate());
        this.sha256Hash.setText(this.appDetails.getSha256hash());
        this.md5Hash.setText(this.appDetails.getMd5hash());

        return view;
    }
}
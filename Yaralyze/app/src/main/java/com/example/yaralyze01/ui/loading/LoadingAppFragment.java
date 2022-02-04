package com.example.yaralyze01.ui.loading;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.home.HomeFragment;

import java.util.ArrayList;

public class LoadingAppFragment extends Fragment {

    private ArrayList<AppDetails> installedApps;
    private boolean installedAppsLoaded;
    private boolean malwareHashesLoaded;

    public LoadingAppFragment(){
        this.installedAppsLoaded = false;
        this.malwareHashesLoaded = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading_app, container, false);
        new GetInstalledAppsTask(this, this.getActivity().getPackageManager()).startOnBackground();
        return view;
    }

    public void installedAppsLoaded(ArrayList<AppDetails> installedApps){
        this.installedApps = installedApps;
        this.installedAppsLoaded = true;

        this.loadApplication();
    }

    public void malwareHashesLoaded(){
        this.malwareHashesLoaded = true;
        this.loadApplication();
    }

    public void loadApplication(){
        if(this.malwareHashesLoaded && this.installedAppsLoaded){
            HomeFragment fragment = new HomeFragment(this.installedApps);

            FragmentManager manager = getParentFragmentManager();
            manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commitAllowingStateLoss();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).getSupportActionBar().show();
    }

    public void showClientExceptionDialog(String exceptionText){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_client_exception);

                TextView exceptionTextField = dialog.findViewById(R.id.clientExceptionText);
                exceptionTextField.setText(exceptionText);

                Button okButton = dialog.findViewById(R.id.clientExceptionOkButton);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        System.exit(1);
                    }
                });

                dialog.show();
            }
        });
    }
}
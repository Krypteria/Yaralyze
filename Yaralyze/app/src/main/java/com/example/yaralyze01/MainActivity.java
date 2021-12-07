package com.example.yaralyze01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.yaralyze01.client.Client;
import com.example.yaralyze01.ui.analysis.installedApps.InstalledAppsFragment;
import com.example.yaralyze01.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private boolean onLoadOperation;

    public MainActivity(){
        this.onLoadOperation = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(this.toolbar);

        //Inicializo la base de datos
        YaralyzeDB.getInstance(MainActivity.this);
        new Thread(new Client(MainActivity.this)).start();

        HomeFragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getTag()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed(){
        int numFragments = getSupportFragmentManager().getBackStackEntryCount();

        if(numFragments == 1){
            finishAffinity();
            System.exit(0);
        }
        else{
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if(!(fragment instanceof InstalledAppsFragment) || (fragment instanceof InstalledAppsFragment && !onLoadOperation)) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }

    public void setOnLoadOperationt(boolean onLoad){
        this.onLoadOperation = onLoad;
    }
}
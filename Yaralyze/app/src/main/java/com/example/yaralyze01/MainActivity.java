package com.example.yaralyze01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.yaralyze01.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
package com.example.yaralyze01.ui.analysis.staticAnalysis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;

public class StaticAnalysisActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fragmentManager = getSupportFragmentManager();
        this.addFragment(new StaticAnalysisMainMenuFragment());
    }

    private void addFragment(Fragment fragment){
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        this.fragmentTransaction.add(R.id.fragmentContainer, fragment, "staticAnalyzerMenuFragment");
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = this.fragmentManager.findFragmentById(R.id.fragmentContainer);
        if(fragment != null){
            if(this.fragmentManager.findFragmentByTag("staticAnalyzerMenuFragment").isVisible()){
                startActivity(new Intent(StaticAnalysisActivity.this, MainActivity.class));
            }
        }
        else{
            super.onBackPressed();
        }
    }
}
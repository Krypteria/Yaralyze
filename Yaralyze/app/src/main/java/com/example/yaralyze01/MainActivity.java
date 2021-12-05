package com.example.yaralyze01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.yaralyze01.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(this.toolbar);


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

    public void prueba(){

    }
}
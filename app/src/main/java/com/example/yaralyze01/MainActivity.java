package com.example.yaralyze01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.mainMenu.mainMenuFragment;

public class MainActivity extends AppCompatActivity {

    //Control de Fragmentos
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fragmentManager = getSupportFragmentManager();
        this.addFragment(new mainMenuFragment());
    }

    private void addFragment(Fragment fragment){
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        this.fragmentTransaction.add(R.id.fragmentContainer, fragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }
}
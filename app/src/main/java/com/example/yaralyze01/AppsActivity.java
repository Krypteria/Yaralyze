package com.example.yaralyze01;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AppsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Lo llamo en onStart para que se actualice cuando la iniciemos
        new GetAllAppsTask(AppsActivity.this).doInBackground();
    }

}

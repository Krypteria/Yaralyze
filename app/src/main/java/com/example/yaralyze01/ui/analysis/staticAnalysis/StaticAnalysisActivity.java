package com.example.yaralyze01.ui.analysis.staticAnalysis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.AppsListFragment;

public class StaticAnalysisActivity extends AppCompatActivity {

    //private ArrayList<AppDetails> apps;
    //private RecyclerView recyclerApps;

    //Control de Fragmentos
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fragmentManager = getSupportFragmentManager();
        this.addFragment(new AppsListFragment());

    /*this.apps = new ArrayList<AppDetails>();
    this.recyclerApps = findViewById(R.id.recyclerViewApps);

    //Lleno la lista
    new GetAllAppsTask(AppsActivity.this).doInBackground();*/

        //Al lanzarse en otro thread seguramente me de problemas
    }

    /*@Override
    protected void onStart(){
        super.onStart();

        //Lo llamo en onStart para que se actualice cuando la iniciemos
        new GetAllAppsTask(AppsActivity.this).doInBackground();
    }*/

    /*public void GetAllAppsTaskCallback(ArrayList<AppDetails> apps){
        this.apps = apps;

        //Enlazo el adaptador al recycler
        AppsAdapter appsAdapter = new AppsAdapter(this.apps);
        this.recyclerApps.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerApps.setAdapter(appsAdapter);
    }*/

    private void addFragment(Fragment fragment){
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        this.fragmentTransaction.add(R.id.fragmentContainer, fragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }
}

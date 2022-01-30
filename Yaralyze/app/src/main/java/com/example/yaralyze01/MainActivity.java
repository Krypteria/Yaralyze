package com.example.yaralyze01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yaralyze01.client.Client;
import com.example.yaralyze01.ui.analysis.installedApps.InstalledAppsFragment;
import com.example.yaralyze01.ui.home.HomeFragment;
import com.example.yaralyze01.ui.loading.LoadingAppFragment;

import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        YaralyzeDB db = YaralyzeDB.getInstance(MainActivity.this);
        if(!db.hasMalwareHashes()){
            if(!this.isInternetAvailable()){
                this.showNoInternetAvalaibleDialog();
            }
            new Thread(new Client(MainActivity.this)).start();
        }

        LoadingAppFragment fragment = new LoadingAppFragment();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.out.println("ESTOY PULSANDO");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //No parece que detecte muy bien cuando hay internet y cuando no, hacer pruebas
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void showNoInternetAvalaibleDialog(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_no_internet_avalaible);

        Button retryButton = dialog.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isInternetAvailable()){
                    Toast.makeText(getApplicationContext(), "No hay conexión a internet", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Actualizando la base de datos", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}
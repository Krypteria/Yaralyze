package com.example.yaralyze01.ui.analysis;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public abstract class BackgroundTask{
    protected Fragment fragment;

    public BackgroundTask(Fragment fragment){
        this.fragment = fragment;
    }

    public void startBackground(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doInBackground();
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute();
                    }
                });
            }
        }).start();
    }

    public abstract void doInBackground();
    public abstract void onPostExecute();
}

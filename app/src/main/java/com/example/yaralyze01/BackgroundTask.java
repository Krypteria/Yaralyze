package com.example.yaralyze01;

import android.app.Activity;

public abstract class BackgroundTask{
    protected Activity activity;

    public BackgroundTask(Activity activity){
        this.activity = activity;
    }

    private void startBackground(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doInBackground();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute();
                    }
                });
            }
        }).start();
    }

    public void execute(){
        startBackground();
    }

    public abstract void doInBackground();
    public abstract void onPostExecute();
}

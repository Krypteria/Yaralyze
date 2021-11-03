package com.example.yaralyze01.ui.analysis;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;

public class AppsViewHolder extends RecyclerView.ViewHolder {

    private ImageView appIcon;
    private TextView appName;
    private TextView appVersion;
    private TextView packageName;

    public AppsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.appName = itemView.findViewById(R.id.appName);
        this.appVersion = itemView.findViewById(R.id.appVersion);
        this.packageName = itemView.findViewById(R.id.packageName);
        this.appIcon = itemView.findViewById(R.id.appIcon);
    }

    public TextView getAppName(){
        return this.appName;
    }

    public TextView getAppVersion(){
        return this.appVersion;
    }

    public TextView getPackageName(){
        return this.packageName;
    }

    public ImageView getAppIcon(){
        return this.appIcon;
    }

}

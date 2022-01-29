package com.example.yaralyze01.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;

public class LastAppsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView appIcon;
    private TextView appName;

    private OnAppListener onAppListener;

    public LastAppsViewHolder(@NonNull View itemView, OnAppListener onAppListener) {
        super(itemView);
        this.appIcon = itemView.findViewById(R.id.lastAnalyzedAppIcon);
        this.appName = itemView.findViewById(R.id.lastAnalyzedAppName);

        this.onAppListener = onAppListener;

        itemView.setOnClickListener(this);
    }

    public ImageView getAppIcon(){
        return this.appIcon;
    }

    public TextView getAppName() { return this.appName; }

    @Override
    public void onClick(View v) {
        this.onAppListener.onAppClick(this.getAdapterPosition());
    }
}

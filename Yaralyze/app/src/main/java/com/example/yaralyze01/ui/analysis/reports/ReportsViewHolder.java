package com.example.yaralyze01.ui.analysis.reports;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;

public class ReportsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView appIcon;
    private TextView appName;
    private TextView reportDate;
    private TextView reportDetection;
    private ImageButton deleteButton;

    private OnAppListener onAppListener;

    public ReportsViewHolder(@NonNull View itemView, OnAppListener onAppListener) {
        super(itemView);
        this.appName = itemView.findViewById(R.id.reportAppName);
        this.appIcon = itemView.findViewById(R.id.reportAppIcon);
        this.reportDate = itemView.findViewById(R.id.reportDate);
        this.reportDetection = itemView.findViewById(R.id.reportDetection);
        this.deleteButton = itemView.findViewById(R.id.deleteReport);

        this.onAppListener = onAppListener;

        itemView.setOnClickListener(this);
    }

    public TextView getReportAppName(){
        return this.appName;
    }

    public ImageView getReportAppIcon(){
        return this.appIcon;
    }

    public TextView getReportDate(){
        return this.reportDate;
    }

    public TextView getReportDetection(){
        return this.reportDetection;
    }

    public ImageButton getDeleteButton(){ return this.deleteButton; }

    @Override
    public void onClick(View v) {
        this.onAppListener.onAppClick(this.getAdapterPosition());
    }
}

package com.example.yaralyze01.ui.analysis;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.staticAnalysis.OnAppListener;

public class AppsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView appIcon;
    private TextView appName;
    private OnAppListener onAppListener;

    public AppsViewHolder(@NonNull View itemView, OnAppListener onAppListener) {
        super(itemView);
        this.appName = itemView.findViewById(R.id.appName);
        this.appIcon = itemView.findViewById(R.id.appIcon);
        this.onAppListener = onAppListener;

        itemView.setOnClickListener(this);
    }

    public TextView getAppName(){
        return this.appName;
    }

    public ImageView getAppIcon(){
        return this.appIcon;
    }

    @Override
    public void onClick(View v) {
        this.onAppListener.onAppClick(this.getAdapterPosition());
    }
}

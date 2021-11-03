package com.example.yaralyze01.ui.analysis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;

import java.util.ArrayList;

public class AppsAdapter extends RecyclerView.Adapter<AppsViewHolder> {

    private ArrayList<AppDetails> apps;

    public AppsAdapter(ArrayList<AppDetails> apps){
        this.apps = apps;
    }

    public AppsAdapter(){
        this.apps = new ArrayList<AppDetails>();
    }

    public void updateData(ArrayList<AppDetails> apps){
        this.apps = apps;
    }

    @NonNull
    @Override
    public AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_list, null, false);
        return new AppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsViewHolder holder, int position) {
        holder.getAppName().setText(this.apps.get(position).getAppName());
        holder.getAppVersion().setText(this.apps.get(position).getAppVersion());
        holder.getPackageName().setText(this.apps.get(position).getPackageName());
        holder.getAppIcon().setImageDrawable(this.apps.get(position).getAppIcon());
    }

    @Override
    public int getItemCount() {
        return this.apps.size();
    }
}

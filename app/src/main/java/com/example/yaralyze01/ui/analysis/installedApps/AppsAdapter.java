package com.example.yaralyze01.ui.analysis.installedApps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;

import java.util.ArrayList;

public class AppsAdapter extends RecyclerView.Adapter<AppsViewHolder> {

    private ArrayList<AppDetails> apps;
    private OnAppListener onAppListener;

    public AppsAdapter(OnAppListener onAppListener){
        this.onAppListener = onAppListener;
        this.apps = new ArrayList<>();
    }

    public void updateData(ArrayList<AppDetails> apps){
        this.apps = apps;
    }

    @NonNull
    @Override
    public AppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, null, false);
        return new AppsViewHolder(view, this.onAppListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsViewHolder holder, int position) {
        holder.getAppName().setText(this.apps.get(position).getAppName());
        holder.getAppIcon().setImageDrawable(this.apps.get(position).getAppIcon());
    }

    @Override
    public int getItemCount() {
        return this.apps.size();
    }
}

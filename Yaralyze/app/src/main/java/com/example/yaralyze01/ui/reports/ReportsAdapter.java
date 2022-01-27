package com.example.yaralyze01.ui.reports;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.appDetails.AppDetails;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsViewHolder> {

    private ArrayList<Report> reports;
    private OnAppListener onAppListener;

    public ReportsAdapter(OnAppListener onAppListener){
        this.onAppListener = onAppListener;
        this.reports = new ArrayList<>();
    }

    public void updateData(ArrayList<Report> reports){
        this.reports = reports;
        notifyDataSetChanged();
    }

    public void addItem(Report report){
        this.reports.add(report);
        notifyItemChanged(this.getItemCount() - 1);
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, null, false);
        return new ReportsViewHolder(view, this.onAppListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        holder.getReportAppName().setText(this.reports.get(position).getAppName());
        holder.getReportAppIcon().setImageDrawable(this.reports.get(position).getAppIcon());
        holder.getReportDate().setText(this.reports.get(position).getReportDate());
        holder.getReportDetection().setText((this.reports.get(position).getDetectionText()));
    }

    @Override
    public int getItemCount() {
        if(this.reports == null){
            return 0;
        }

        return this.reports.size();
    }
}

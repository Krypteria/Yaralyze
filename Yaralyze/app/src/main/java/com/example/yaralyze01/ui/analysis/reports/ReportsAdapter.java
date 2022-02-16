package com.example.yaralyze01.ui.analysis.reports;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.MainActivity;
import com.example.yaralyze01.R;
import com.example.yaralyze01.YaralyzeDB;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;
import com.example.yaralyze01.ui.analysis.outcomes.AnalysisOutcome;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsViewHolder> {

    private final static String malwareDetectedString = "Malware detectado";
    private final static String malwareNotDetectedString = "Malware no detectado";

    private ArrayList<AnalysisOutcome> analysisOutcomes;
    private OnAppListener onAppListener;

    public ReportsAdapter(OnAppListener onAppListener){
        this.onAppListener = onAppListener;
        this.analysisOutcomes = new ArrayList<>();
    }

    public void updateData(ArrayList<AnalysisOutcome> analysisOutcomes){
        this.analysisOutcomes = analysisOutcomes;
        notifyDataSetChanged();
    }

    public void addItem(AnalysisOutcome analysisOutcome){
        this.analysisOutcomes.add(analysisOutcome);
        notifyItemChanged(this.getItemCount() - 1);
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, null, false);
        return new ReportsViewHolder(view, this.onAppListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.getReportAppName().setText(this.analysisOutcomes.get(position).getAnalyzedAppName());
        holder.getReportAppIcon().setImageDrawable(this.analysisOutcomes.get(position).getAnalyzedAppIcon());
        holder.getReportDate().setText(this.analysisOutcomes.get(position).getAnalysisDate());

        holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YaralyzeDB db = YaralyzeDB.getInstance(v.getContext());
                db.deleteOutcome(analysisOutcomes.get(position));

                analysisOutcomes.remove(position);
                notifyDataSetChanged();
            }
        });

        if(this.analysisOutcomes.get(position).isMalwareDetected()){
            holder.getReportDetection().setText(malwareDetectedString);
            holder.getReportDetection().setTextColor(Color.parseColor("#A62424"));
        }
        else{
            holder.getReportDetection().setText(malwareNotDetectedString);
            holder.getReportDetection().setTextColor(Color.parseColor("#4CAF50"));
        }
    }

    @Override
    public int getItemCount() {
        if(this.analysisOutcomes == null){
            return 0;
        }

        return this.analysisOutcomes.size();
    }
}

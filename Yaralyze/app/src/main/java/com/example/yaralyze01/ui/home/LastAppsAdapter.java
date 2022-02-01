package com.example.yaralyze01.ui.home;

import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;
import com.example.yaralyze01.ui.analysis.installedApps.OnAppListener;

import java.util.ArrayList;

public class LastAppsAdapter extends RecyclerView.Adapter<LastAppsViewHolder> {

    private ArrayList<Pair<String, Drawable>> lastAnalyzedAppsIcons;
    private OnAppListener onAppListener;

    public LastAppsAdapter(OnAppListener onAppListener){
        this.onAppListener = onAppListener;
        this.lastAnalyzedAppsIcons = new ArrayList<>();
    }

    public void updateData(ArrayList<Pair<String, Drawable>> lastAnalyzedAppsIcons){
        this.lastAnalyzedAppsIcons = lastAnalyzedAppsIcons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LastAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_analyzed_app_item, null, false);
        return new LastAppsViewHolder(view, this.onAppListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LastAppsViewHolder holder, int position) {
        holder.getAppIcon().setImageDrawable(this.lastAnalyzedAppsIcons.get(position).second);
        holder.getAppName().setText(this.lastAnalyzedAppsIcons.get(position).first);
    }

    @Override
    public int getItemCount() {
        if(this.lastAnalyzedAppsIcons == null){
            return 0;
        }

        return this.lastAnalyzedAppsIcons.size();
    }
}

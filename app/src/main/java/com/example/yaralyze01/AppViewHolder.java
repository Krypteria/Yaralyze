package com.example.yaralyze01;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView nameView;

    public AppViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.appImageView);
        nameView = itemView.findViewById(R.id.appNameView);
    }
}

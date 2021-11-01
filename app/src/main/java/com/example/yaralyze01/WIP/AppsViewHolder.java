package com.example.yaralyze01.WIP;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yaralyze01.R;

public class AppsViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView nameView;

    public AppsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.appImageView);
        nameView = itemView.findViewById(R.id.appNameView);
    }

    public ImageView getImageView(){
        return this.imageView;
    }

    public TextView getNameView(){
        return this.nameView;
    }
}

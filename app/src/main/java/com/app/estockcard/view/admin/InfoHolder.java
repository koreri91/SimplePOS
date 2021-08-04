package com.app.estockcard.view.admin;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoHolder extends RecyclerView.ViewHolder {
    public @BindView(R.id.image_info_holder)
    AppCompatImageView icon;
    public @BindView(R.id.title_center_info_holder)
    AppCompatTextView titleCenter;

    public InfoHolder(View view){
        super(view);
        ButterKnife.bind(this,view);
    }
}

package com.app.estockcard.view.admin.shift;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShiftDisplayHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title_display_date)
    AppCompatTextView titleDisplayDate;

    @BindView(R.id.title_number)
    AppCompatTextView titleNumber;

    @BindView(R.id.title_name)
    AppCompatTextView titleName;

    @BindView(R.id.title_time)
    AppCompatTextView titleTime;

    ShiftDisplayHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}

package com.app.estockcard.view.admin.shift;

import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShiftInputHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title_shift_list_item_rv)
    AppCompatTextView title;

    @BindView(R.id.layout_employee_shift_list_rv)
    LinearLayout layout_employee;

    @BindView(R.id.employee_shift_list_rv)
    AppCompatTextView employee;
    @BindView(R.id.time_shift_list_rv)
    AppCompatTextView time;


    ShiftInputHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
package com.app.estockcard.controller.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class CustomNumberPicker extends NumberPicker {


    public CustomNumberPicker(Context context, AttributeSet attrs){
        super(context,attrs);


    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof EditText) {
            EditText editText = ((EditText)child);
            editText.setTextSize(15);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editText.setPadding(0,0,0,0);


        }
    }
}

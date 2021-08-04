package com.app.estockcard.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.app.estockcard.R;

public class ToastDialog extends Toast {
    private AppCompatTextView contentField;
    private AppCompatImageView closeBtn;
    private boolean hide;

    public ToastDialog(Context ctx){
        super(ctx);
        View toastView = LayoutInflater.from(ctx).inflate(R.layout.toast_custom, null);
        contentField = toastView.findViewById(R.id.content_field);
        closeBtn = toastView.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide = true;
                cancel();
            }
        });
        setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 0,ctx. getResources().getDimensionPixelOffset(R.dimen.activity_mini_vertical_margin) );
        setView(toastView);
    }
    public ToastDialog(Context ctx,String message,boolean showCloseBtn){
        super(ctx);
        View toastView = LayoutInflater.from(ctx).inflate(R.layout.toast_custom, null);
        contentField = toastView.findViewById(R.id.content_field);
        contentField.setText(message);
        closeBtn = toastView.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide = true;
                cancel();
            }
        });
        if (!showCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }
        setGravity(Gravity.TOP|Gravity.FILL_HORIZONTAL, 0,ctx. getResources().getDimensionPixelOffset(R.dimen.activity_mini_vertical_margin) );
        setView(toastView);
    }

    public void setMessage(String message){
        contentField.setText(message);
    }

    @Override
    public void cancel() {
        super.cancel();
        hide = true;
    }

    public boolean isHide(){
        return hide;
    }



}

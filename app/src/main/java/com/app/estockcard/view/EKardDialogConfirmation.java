package com.app.estockcard.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.app.estockcard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EKardDialogConfirmation extends Dialog {

    private final String title;
    private final String content;
    private final String negativeButtonName;
    private final String positiveButtonName;

    public EKardDialogConfirmation(Context context, String title, String content, String negativeButtonName, String positiveButtonName){
        super(context);
        this.title = title;
        this.content = content;
        this.negativeButtonName = negativeButtonName;
        this.positiveButtonName = positiveButtonName;


        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @BindView(R.id.dialog_title)
    AppCompatTextView titleDialog;

    @BindView(R.id.dialog_content)
    AppCompatTextView contentDialog;

    @BindView(R.id.dialog_negative_btn)
    AppCompatButton negativeBtn;

    @BindView(R.id.dialog_positive_btn)
    AppCompatButton positiveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirmation);
        ButterKnife.bind(this);

        titleDialog.setText(title);
        contentDialog.setText(content);

        negativeBtn.setText(negativeButtonName);
        positiveBtn.setText(positiveButtonName);

    }

    public void setNegativeButtonListener(View.OnClickListener negativeButtonListener){
        negativeBtn.setOnClickListener(negativeButtonListener);
    }

    public void setPositiveButtonListener(View.OnClickListener positiveButtonListener){
        positiveBtn.setOnClickListener(positiveButtonListener);
    }

}

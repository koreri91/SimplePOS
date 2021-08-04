package com.app.estockcard.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.view.admin.AdminMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.close_btn)
    Toolbar toolbar;

    @BindView(R.id.email_field)
    AppCompatEditText emailField;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.fall, R.anim.fall);
                onBackPressed();
            }
        });
        setDrawablePreLollipop(emailField, ContextCompat.getDrawable(this, R.drawable.ic_email), null, null, null);

        progressBar.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPermissions();
    }

    @OnClick({R.id.submit_btn})
    void onClick(View view) {
        if (R.id.submit_btn == view.getId()) {
            if (emailField.getText().toString().isEmpty()) {
                return;
            }
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.sendPasswordResetEmail(emailField.getText().toString());
            displayErrorLogin(getString(R.string.appstr_link_to_reset_password_already_sent_to)
                    .concat(getString(R.string.appstr_space)).concat(emailField.getText().toString()));
            finish();
        }
    }

    private final String TAG = getClass().getSimpleName();

    Runnable executor = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(ForgotPasswordActivity.this, AdminMainActivity.class));
            finish();
        }
    };


    private void displayErrorLogin(String message){
        ToastDialog toastDialog = new ToastDialog(this,message,false);
        toastDialog.setDuration(ToastDialog.LENGTH_LONG);
        toastDialog.show();
    }


}

package com.app.estockcard.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity {

    @BindView(R.id.username_field)
    AppCompatEditText usernameField;

    @BindView(R.id.email_field)
    AppCompatEditText emailField;

    @BindView(R.id.password_field)
    AppCompatEditText passwordField;

    @BindView(R.id.login_with_account_btn)
    AppCompatButton loginWithAccountBtn;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.close_btn)
    Toolbar toolbar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressBar.setVisibility(View.GONE);

        setDrawablePreLollipop(usernameField, ContextCompat.getDrawable(this, R.drawable.ic_user), null, null, null);
        setDrawablePreLollipop(emailField, ContextCompat.getDrawable(this, R.drawable.ic_email), null, null, null);
        setDrawablePreLollipop(passwordField, ContextCompat.getDrawable(this, R.drawable.ic_lock), null, null, null);
        setDrawablePreLollipop(loginWithAccountBtn, ContextCompat.getDrawable(this, R.drawable.ic_google24x24), null, null, null);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPermissions();
    }

    @OnClick({R.id.sign_up_btn})
    void onClick(View view) {
        if (R.id.sign_up_btn == view.getId()) {
            doSignUp(usernameField.getText().toString(),emailField.getText().toString(),passwordField.getText().toString());
        }
    }

    private final String TAG = getClass().getSimpleName();

    Runnable executor = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    private void doSignUp(String username,String email, String pass) {
        if ( username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            displayMessage(getString(R.string.appstr_please_fill_all_column));
            return;
        }

        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> userObject = new HashMap<>();
        userObject.put(getString(R.string.appstr_param_username), username);
        userObject.put(getString(R.string.appstr_param_id), email);
        userObject.put(getString(R.string.appstr_param_pwd), pass);
        userObject.put(getString(R.string.appstr_param_isactive), false);
        userObject.put(getString(R.string.appstr_param_isgranted), false);
        userObject.put(getString(R.string.appstr_param_level), User.USER_LEVEL);
        userObject.put(getString(R.string.appstr_param_datecreated), new SimpleDateFormat(getString(R.string.appstr_format_date_yyyy_MM_dd_mm_dd),
                new Locale(getString(R.string.appstr_locale_language_code), getString(R.string.appstr_locale_country_code))).format(new Date()));

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                displayMessage(getString(R.string.appstr_verification_email_sent_to).concat(getString(R.string.appstr_space)).concat(user.getEmail()));
                            } else {
                                displayMessage(getString(R.string.appstr_failed_to_send_verification_email));
                            }
                        }
                    });
                    createRegisteredUser(email, userObject);
                } else {
                    // If sign in fails, display a message to the user.
                    displayMessage(" " + task.getException().getMessage());

                }
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void displayMessage(String message) {
        ToastDialog toastDialog = new ToastDialog(this, message, false);
        toastDialog.setDuration(ToastDialog.LENGTH_LONG);
        toastDialog.show();
    }

    private void createRegisteredUser(String email, Map user) {
        CollectionReference colRef = db.collection(getString(R.string.appstr_cs_registered_account));
        colRef.document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new Handler().postDelayed(executor,2000);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                SysLog.getInstance().sendLog(TAG, getString(R.string.appstr_error).concat(e.getMessage()));
            }
        });
    }


}

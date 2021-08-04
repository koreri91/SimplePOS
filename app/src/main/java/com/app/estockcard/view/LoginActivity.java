package com.app.estockcard.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Profile;
import com.app.estockcard.model.User;
import com.app.estockcard.view.admin.AdminMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.email_field)
    AppCompatEditText emailField;


    @BindView(R.id.password_field)
    AppCompatEditText passwordField;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);

        setDrawablePreLollipop(emailField, ContextCompat.getDrawable(this, R.drawable.ic_user), null, null, null);
        setDrawablePreLollipop(passwordField, ContextCompat.getDrawable(this, R.drawable.ic_lock), null, null, null);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initPermissions();
        if (dbManager.isProfileExists()) {
            executor.run();
        }
    }

    @OnClick({R.id.login_btn,R.id.sign_up_btn,R.id.forgot_password_field})
    void onClick(View view) {
        if (R.id.login_btn == view.getId()) {
            doLogin(emailField.getText().toString(),passwordField.getText().toString());
        }else  if (R.id.sign_up_btn == view.getId()) {
            emailField.setText("");
            passwordField.setText("");
            startActivity(new Intent(this,SignUpActivity.class));
        }else   if (R.id.forgot_password_field == view.getId()) {
            emailField.setText("");

            startActivity(new Intent(this,ForgotPasswordActivity.class));
        }
    }

    private final String TAG = getClass().getSimpleName();

    Runnable executor = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
            finish();
        }
    };

    private void doLogin(String email,String pass) {
        if (email.isEmpty() || pass.isEmpty()){
            displayMessage(getString(R.string.appstr_please_fill_username_and_password_column));
            return;
        }
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser userAuth = mAuth.getCurrentUser();
                    if (!userAuth.isEmailVerified()) {
                        displayMessage(getString(R.string.appstr_verify_email_address));
                        progressBar.setIndeterminate(false);
                        progressBar.setVisibility(View.GONE);
                    }else {
                        CollectionReference colRef = db.collection(getString(R.string.appstr_cs_registered_account));
                        colRef.document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    User user = task.getResult().toObject(User.class);
                                    Map<String, Object> userObject = new HashMap<>();
                                    userObject.put(getString(R.string.appstr_param_username), user.getUsername());
                                    userObject.put(getString(R.string.appstr_param_id), email);
                                    userObject.put(getString(R.string.appstr_param_pwd), pass);
                                    userObject.put(getString(R.string.appstr_param_isactive), false);
                                    userObject.put(getString(R.string.appstr_param_isgranted), true);
                                    userObject.put(getString(R.string.appstr_param_level), User.USER_LEVEL);
                                    userObject.put(getString(R.string.appstr_param_datecreated), user.getDateCreated());

                                    Profile profile = new Profile();
                                    profile.setUsername(user.getUsername());
                                    profile.setPwd(user.getPwd());
                                    profile.setDateCreated(user.getDateCreated());
                                    profile.setId(user.getId());
                                    profile.setLevel(user.getLevel());

                                    colRef.document(email).update(userObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                insertUserProfile(profile);
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        executor.run();
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.GONE);
                    if (task.getException().getMessage().contains(getString(R.string.appstr_errormessage_password_invalid))) {
                        displayMessage(getString(R.string.appstr_email_not_match_with_password));
                    } else {
                        displayMessage(getString(R.string.appstr_email_not_registered));
                    }
                }
            }
        });
    }

    private void displayMessage(String message) {
        ToastDialog toastDialog = new ToastDialog(this,message,false);
        toastDialog.setDuration(ToastDialog.LENGTH_LONG);
        toastDialog.show();
    }

    private void insertUserProfile(Profile profile) {
        dbManager.insertUser(profile, new OnDBResultListener() {
            @Override
            public void onSuccess(String success) {
                finish();
            }

            @Override
            public void onError(String error) {
            }
        });
    }


}

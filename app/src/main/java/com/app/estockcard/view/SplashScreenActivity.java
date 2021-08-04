package com.app.estockcard.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends BaseActivity {

    @BindView(R.id.img_splash)
    AppCompatImageView imgSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        ButterKnife.bind(this);

        initDB(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    startLogin();
                    finish();
                }, 2000);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(SplashScreenActivity.this).asDrawable()
                        .override(getResources().getDimensionPixelSize(R.dimen.app_very_large_layout_height))
                        .load(R.drawable.ic_wrong_source).into(imgSplash);
            }
        });
    }

    private void startLogin(){
        startActivity(new Intent(this, LoginActivity.class));
    }
}

package com.app.estockcard.view.cover;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.app.estockcard.R;
import com.app.estockcard.view.LoginActivity;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoverActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

//    @BindView(R.id.next_btn)
//    AppCompatTextView nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);
        ButterKnife.bind(this);

        viewPager.setAdapter(new AdapterDisplayCover(this,getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.next_btn)
    void onClick(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

}

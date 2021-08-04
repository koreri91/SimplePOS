package com.app.estockcard.view.admin.settings;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StructSettingsActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    AppCompatTextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_settings_struct);
        ButterKnife.bind(this);

        title.setText(getString(R.string.appstr_receipt));
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}

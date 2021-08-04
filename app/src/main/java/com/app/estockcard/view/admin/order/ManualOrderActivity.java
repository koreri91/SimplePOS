package com.app.estockcard.view.admin.order;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManualOrderActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_manual_order);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.appstr_manual_order);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

}

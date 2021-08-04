package com.app.estockcard.view.admin.transaction;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionCreditPaymentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_transaction_credit_payment);
        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.appstr_credit_payment));
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

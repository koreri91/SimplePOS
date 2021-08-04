package com.app.estockcard.view.admin.transaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Transaction;
import com.app.estockcard.view.EKardDialogConfirmation;

public class TransactionPaymentCancelledActivity extends BaseActivity {


    private Toolbar toolbar;
    private AppCompatButton cancelTransactionBtn;
    private AppCompatTextView totalPaymentField;

    private Transaction cancellingItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_transaction_cancelled_payment);

        cancellingItem = dbManager.getTransaction(getIntent().getStringExtra(getString(R.string.appstr_param_transactionID)));
        ;
        toolbar = findViewById(R.id.toolbar);
        cancelTransactionBtn = findViewById(R.id.cancel_transaction_btn);
        totalPaymentField = findViewById(R.id.total_payment_field);

        totalPaymentField.setText(new CurrencyUtil().formatIndonesiaCurrency(cancellingItem.getTotal() ));

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        cancelTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EKardDialogConfirmation eKardDialogConfirmation =
                        createDialogConfirmation(getString(R.string.appstr_cancelling_transaction),getString(R.string.appstr_cancelling_transaction_message_display),
                                getString(R.string.appstr_no),getString(R.string.appstr_yes));
                eKardDialogConfirmation.show();
                eKardDialogConfirmation.setNegativeButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eKardDialogConfirmation.dismiss();
                    }
                });
                eKardDialogConfirmation.setPositiveButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doCancellingTransaction();
                    }
                });

            }
        });
    }

    private void doCancellingTransaction(){
            cancellingItem.setStatus(Transaction.TRANSACTION_CANCELLED);
            dbManager.updateTransaction(cancellingItem, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                @Override
                public void onError(String error) {

                }
            });
    }





}

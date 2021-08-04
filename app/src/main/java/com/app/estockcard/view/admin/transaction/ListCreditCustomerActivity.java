package com.app.estockcard.view.admin.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.model.Transaction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListCreditCustomerActivity extends BaseActivity implements TransactionAdapterRV.OnTransactionAdapter{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_field)
    AppCompatEditText searchField;

    @BindView(R.id.customer_rv)
    RecyclerView customerRV;

    private TransactionCreditAdapterRV transactionCreditAdapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_credit_customer);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.appstr_credit_list);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        transactionCreditAdapterRV = new TransactionCreditAdapterRV(this);
        transactionCreditAdapterRV.setData(dbManager.getAllCustomerTransaction(Transaction.TRANSACTION_CREDIT));
        transactionCreditAdapterRV.setListener(this);
        customerRV.setAdapter(transactionCreditAdapterRV);
        customerRV.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        customerRV.addItemDecoration(new DividerItemDecoration(this,RecyclerView.VERTICAL));

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SysLog.getInstance().sendLog("ListCredit",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                transactionCreditAdapterRV.getFilter().filter(s.toString());
            }
        });
    }

    @Override
    public void onMenuClick(int viewId, Transaction transaction, int position) {
        if (viewId == R.id.layout_parent_item_customer_rv) {
            Intent intenTransactionDetail = new Intent(getApplicationContext(),TransactionDetailActivity.class);
            intenTransactionDetail.putExtra("status",transaction.getStatus());
            intenTransactionDetail.putExtra("customerName",(transaction.getBuyerId() == -1 ) ? "" : new DBManager().getEmployee(transaction.getBuyerId()).getName());
            intenTransactionDetail.putExtra("transactionID",transaction.getId());
            intenTransactionDetail.putExtra("dateTime",transaction.getDateTime());
            intenTransactionDetail.putExtra("paymentMethod",transaction.getPaymentMethod());
            intenTransactionDetail.putExtra("dueDatePayment",transaction.getDueDatePayment());
            intenTransactionDetail.putExtra("operator",transaction.getOperator());
            intenTransactionDetail.putExtra("subtotal",transaction.getSubtotal());
            intenTransactionDetail.putExtra("discount",transaction.getDiscount());
            intenTransactionDetail.putExtra("total",transaction.getTotal());
            intenTransactionDetail.putExtra("paymentLog",transaction.getPaymentLog());
            intenTransactionDetail.putExtra("listBill",transaction.getListBill());
            startActivity(intenTransactionDetail);
        }
    }




}

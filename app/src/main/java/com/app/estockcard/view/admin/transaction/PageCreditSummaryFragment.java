package com.app.estockcard.view.admin.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.app.estockcard.R;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.model.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PageCreditSummaryFragment extends Fragment {

    @BindView(R.id.display_number_of_credit_transaction)
    AppCompatTextView displayNumberOfCreditTransaction;

    @BindView(R.id.display_number_of_customer)
    AppCompatTextView displayNumberOfCustomer;

    @BindView(R.id.display_transaction_due_field)
    AppCompatTextView displayTransactionDueDateField;

    @BindView(R.id.display_transaction_not_been_paid_off)
    AppCompatTextView displayTransactionNotBeenPaidOff;

    private DBManager dbManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_transaction_credit_summary, container, false);
        ButterKnife.bind(this, view);

        dbManager = new DBManager();

        List<Transaction> allCustomerTransaction =  dbManager.getAllCustomerTransaction(Transaction.TRANSACTION_CREDIT);
        double totalCredit = 0;
        for (Transaction item : allCustomerTransaction) {
            totalCredit += item.getTotal();
        }
        int numberOfCustomer = allCustomerTransaction.size();
        displayNumberOfCreditTransaction.setText(new CurrencyUtil().formatIndonesiaCurrency(totalCredit));
        displayNumberOfCustomer.setText(numberOfCustomer+"");

        displayTransactionNotBeenPaidOff.setText(numberOfCustomer+"");
        displayTransactionDueDateField.setText(numberOfCustomer+"");

        return view;
    }

    @OnClick(R.id.view_credit_list_btn)
    void onClick(View view){
        if (view.getId() == R.id.view_credit_list_btn) {
            startActivity(new Intent(getActivity(),ListCreditCustomerActivity.class));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

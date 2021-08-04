package com.app.estockcard.view.admin.transaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.model.Transaction;
import com.app.estockcard.view.EKardDialogList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PageAllListTransactionFragment extends Fragment implements TransactionAdapterRV.OnTransactionAdapter {

    @BindView(R.id.search_field)
    AppCompatEditText searchField;

    @BindView(R.id.filter_status_btn)
    AppCompatButton filterStatusTransactionBtn;

    @BindView(R.id.filter_by_date_btn)
    AppCompatButton viewTransactionDateBtn;

    @BindView(R.id.transition_rv)
    RecyclerView transactionRV;

    private TransactionAdapterRV transactionAdapterRV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_transaction_all_list, container, false);
        ButterKnife.bind(this, view);

        ((BaseActivity) getContext()).setDrawablePreLollipop(filterStatusTransactionBtn, ContextCompat.getDrawable(getContext(), R.drawable.ic_filter), null, null, null);
        ((BaseActivity) getContext()).setDrawablePreLollipop(viewTransactionDateBtn, ContextCompat.getDrawable(getContext(), R.drawable.ic_date), null, null, null);

        filterStatusIdx = Transaction.TRANSACTION_ALL;
        transactionAdapterRV = new TransactionAdapterRV(getActivity());
        transactionAdapterRV.setListener(this);
        transactionAdapterRV.setData(new DBManager().getAllTransaction(filterStatusIdx));

        transactionRV.setAdapter(transactionAdapterRV);
        transactionRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        transactionRV.setHasFixedSize(false);
        transactionRV.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData(){
        transactionAdapterRV.setData(new DBManager().getAllTransaction(filterStatusIdx));
        transactionAdapterRV.notifyDataSetChanged();
    }
    private int filterStatusIdx;

    @OnClick({R.id.filter_status_btn, R.id.filter_by_date_btn})
    void onClick(View view) {
        if (view.getId() == R.id.filter_status_btn) {
            ((BaseActivity) getContext()).createDialogList(
                    new String[]{"All", "Credit", "Paid Off", "Cancelled"},
                    new EKardDialogList.OnDialogListListener() {
                        @Override
                        public void onResultItemSelected(int position, String name) {
                            if (name.equals("All")) {
                                filterStatusIdx = Transaction.TRANSACTION_ALL;
                            }else  if (name.equals("Credit")) {
                                filterStatusIdx = Transaction.TRANSACTION_CREDIT;
                            }else  if (name.equals("Paid Off")) {
                                filterStatusIdx = Transaction.TRANSACTION_SUCCESS;
                            }else  if (name.equals("Cancelled")) {
                                filterStatusIdx = Transaction.TRANSACTION_CANCELLED;
                            }
                            updateData();
                        }
                    }).show();
        } else if (view.getId() == R.id.filter_by_date_btn) {

        }
    }

    @Override
    public void onMenuClick(int viewId, Transaction transaction, int position) {
        if (viewId == R.id.parent_layout) {
            Intent intenTransactionDetail = new Intent(getContext(),TransactionDetailActivity.class);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

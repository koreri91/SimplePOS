package com.app.estockcard.view.admin.transaction;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.model.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentLogsDialog extends Dialog {

    private List<Transaction> itemsOrdered;
    public PaymentLogsDialog(Context ctx, List<Transaction> items){
        super(ctx);
        this.itemsOrdered = items;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @BindView(R.id.data_rv)
    RecyclerView dataRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_payment_log);
        ButterKnife.bind(this);

        LogAdapter billingItemsAdapter = new LogAdapter(getContext());
        billingItemsAdapter.setData(itemsOrdered);
        dataRV.setAdapter(billingItemsAdapter);
        dataRV.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }

    @OnClick(R.id.close_btn)
    void onClick(){
        dismiss();
    }


    class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogHolder> {

        private Context context;
        private CurrencyUtil currencyUtil;
        public LogAdapter(Context ctx){
            this.context = ctx;
            currencyUtil = new CurrencyUtil();
        }

        class LogHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.display_name_field)
            AppCompatTextView displayDate;

            @BindView(R.id.quantity_item_field)
            AppCompatTextView displayOperatorName;

            @BindView(R.id.display_total_price_field)
            AppCompatTextView displayTotalPayment;

            public LogHolder(View view){
                super(view);
                ButterKnife.bind(this,view);
            }
        }
        private List<Transaction> itemsOrdered;
        public void setData(List<Transaction> items){
            this.itemsOrdered = items;
        }

        @Override
        public int getItemCount() {
            return itemsOrdered.size();
        }



        @Override
        public void onBindViewHolder(@NonNull LogHolder holder, int position) {
            Transaction product = itemsOrdered.get(position);
            holder.displayDate.setText(product.getDateTime());
            holder.displayOperatorName.setText(product.getOperator());
            holder.displayTotalPayment.setText(currencyUtil.formatIndonesiaCurrency(product.getTotal()));
        }

        @NonNull
        @Override
        public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new LogHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_bill_list,parent,false));
        }

    }


}

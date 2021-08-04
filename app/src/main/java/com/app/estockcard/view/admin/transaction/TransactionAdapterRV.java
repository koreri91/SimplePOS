package com.app.estockcard.view.admin.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Transaction> transactions;
    private Context context;
    private CurrencyUtil currencyUtil;

    TransactionAdapterRV(Context ctx) {
        this.context = ctx;
        this.currencyUtil = new CurrencyUtil();
    }

    class TransactionDataHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent_layout)
        RelativeLayout parentLayout;


        @BindView(R.id.icon)
        AppCompatImageView icon;

        @BindView(R.id.display_status)
        AppCompatTextView status;

        @BindView(R.id.total_payment_field)
        AppCompatTextView totalPaymetField;

        @BindView(R.id.custumer_name_field)
        AppCompatTextView customerNameField;

        @BindView(R.id.transaction_id_field)
        AppCompatTextView transactionIDField;

        TransactionDataHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    class TransactionTitleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.display_date)
        AppCompatTextView displayDate;

        @BindView(R.id.display_total_payment)
        AppCompatTextView displayTotalPayment;

        TransactionTitleHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface OnTransactionAdapter{
        void onMenuClick(int viewId,Transaction transaction,int position);
    }

    public void setData(List<Transaction> data) {
        this.transactions = data;
    }
    private OnTransactionAdapter mListener;
    public void setListener(OnTransactionAdapter listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return (transactions == null) ? 0 : transactions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return transactions.get(position).getViewHolder();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,final int position) {
        final Transaction transaction = transactions.get(position);

        if (getItemViewType(position) == Transaction.viewHolderTitle) {
            TransactionTitleHolder holder = (TransactionTitleHolder) viewHolder;
            SysLog.getInstance().sendLog(getClass().getSimpleName()," "+transaction.getDateTime());
            String []dates = transaction.getDateTime().split(" ")[0].split("/");
            Locale locale = new Locale(context.getString(R.string.appstr_locale_country_code), context.getString(R.string.appstr_locale_country_code));
            Calendar calendar = Calendar.getInstance(locale);
            calendar.set(Integer.parseInt(dates[0]),Integer.parseInt(dates[1])-1,Integer.parseInt(dates[2]));
            holder.displayDate.setText(new SimpleDateFormat(context.getString(R.string.appstr_format_date_EEE_dd_MMM_yyyy), locale).format(calendar.getTime()));
            holder.displayTotalPayment.setText(currencyUtil.formatIndonesiaCurrency(transaction.getTotal()));
        }else {
            TransactionDataHolder holder = (TransactionDataHolder) viewHolder;
            // holder.icon.setText(transaction.getDateTime());
            String trasactionStatus = "";
            //SysLog.getInstance().sendLog(getClass().getSimpleName(),"status : "+transaction.getStatus());
            if (transaction.getStatus() == Transaction.TRANSACTION_CANCELLED ) {
                holder.status.setText(context.getString(R.string.appstr_status_cancelled));
                holder.status.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.selector_red));
            }else  if  (transaction.getStatus() == Transaction.TRANSACTION_CREDIT ) {
                holder.status.setText(context.getString(R.string.appstr_status_credit));
                holder.status.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.selector_orange));
            }else if (transaction.getStatus() == Transaction.TRANSACTION_SUCCESS) {
                holder.status.setText(context.getString(R.string.appstr_status_paid_off));
                holder.status.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.selector_blue));
            }
            holder.totalPaymetField.setText(currencyUtil.formatIndonesiaCurrency(transaction.getTotal()) );
            holder.customerNameField.setText(transaction.getBuyerId() == -1 ? "-"
                    : new DBManager().getEmployee(transaction.getBuyerId()).getName());
            holder.transactionIDField.setText(transaction.getId() + "");
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onMenuClick(holder.parentLayout.getId(),transaction,position);
                    }
                }
            });
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (viewType == Transaction.viewHolderTitle) ?
                new TransactionTitleHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_title_transaction, parent, false))
        :       new TransactionDataHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_view_transaction, parent, false));
    }

}

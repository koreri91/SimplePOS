package com.app.estockcard.view.admin.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionCreditAdapterRV extends RecyclerView.Adapter<TransactionCreditAdapterRV.CreditHolder> implements Filterable {
    private List<Transaction> transactions;
    private List<Transaction> transactionsFiltered;
    private Context context;
    private DBManager dbManager;
    private CurrencyUtil currencyUtil;

    public TransactionCreditAdapterRV(Context ctx) {
        this.context = ctx;
        this.dbManager = new DBManager();
        this.currencyUtil = new CurrencyUtil();
        this.transactions = new ArrayList<>();
        this.transactionsFiltered = new ArrayList<>();
    }

    class CreditHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_parent_item_customer_rv)
        RelativeLayout parent;

        @BindView(R.id.date_of_credit_field)
        AppCompatTextView dateOfCreditField;

        @BindView(R.id.display_credit_amount_field)
        AppCompatTextView displayCreditAmountField;

        @BindView(R.id.fullname_field)
        AppCompatTextView fullnameField;

        CreditHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setData(List<Transaction> credits) {
        this.transactions = credits;
        this.transactionsFiltered = credits;
    }

    private TransactionAdapterRV.OnTransactionAdapter mListener;

    public void setListener(TransactionAdapterRV.OnTransactionAdapter listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return (transactionsFiltered == null) ? 0 : transactionsFiltered.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CreditHolder holder, final int position) {
        final Transaction credit = transactionsFiltered.get(position);

        Locale locale = new Locale(context.getString(R.string.appstr_locale_language_code), context.getString(R.string.appstr_locale_country_code));
        Calendar calendar = Calendar.getInstance(locale);
        String[] arrDate = credit.getDueDatePayment().split("/");
        calendar.set(Integer.parseInt(arrDate[0]), Integer.parseInt(arrDate[1]), Integer.parseInt(arrDate[2]));
        String strDate = new SimpleDateFormat(context.getString(R.string.appstr_format_date_EEE_dd_MMM_yyyy), locale).format(calendar.getTime());

        holder.dateOfCreditField.setText(strDate);
        holder.fullnameField.setText(dbManager.getEmployee(credit.getBuyerId()).getName());
        holder.displayCreditAmountField.setText(currencyUtil.formatIndonesiaCurrency(credit.getTotal()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMenuClick(holder.parent.getId(), credit, position);
                }
            }
        });
    }

    @NonNull
    @Override
    public CreditHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CreditHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_list_credit, parent, false));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    transactionsFiltered = transactions;
                } else {
                    List<Transaction> filteredList = new ArrayList<>();
                    for (Transaction item : transactions) {
                        String customer = dbManager.getEmployee(item.getBuyerId()).getName().toLowerCase();

                        if (customer.equalsIgnoreCase(charString)) {
                            filteredList.add(item);
                        }
                    }
                    transactionsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = transactionsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                transactionsFiltered = (ArrayList<Transaction>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

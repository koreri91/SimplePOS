package com.app.estockcard.view.admin.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.model.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillItemsAdapter extends RecyclerView.Adapter<BillItemsAdapter.Holder> {

    private Context context;
    private CurrencyUtil currencyUtil;
    public BillItemsAdapter(Context ctx){
        this.context = ctx;
        this.currencyUtil = new CurrencyUtil();
    }

    class Holder extends RecyclerView.ViewHolder{
        @BindView(R.id.display_name_field)
        AppCompatTextView displayNameField;

        @BindView(R.id.quantity_item_field)
        AppCompatTextView quantityItemField;

        @BindView(R.id.display_total_price_field)
        AppCompatTextView displayTotalPriceField;

        public Holder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
    private List<Product> itemsOrdered;
    public void setData(List<Product> items){
        this.itemsOrdered = items;
    }

    @Override
    public int getItemCount() {
        return itemsOrdered.size();
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Product product = itemsOrdered.get(position);
        holder.displayNameField.setText(product.getName());
        holder.quantityItemField.setText(product.getCountOrdered()+"x@"+product.getSellingPrice());
        holder.displayTotalPriceField.setText(currencyUtil.formatIndonesiaCurrency(product.getCountOrdered()*product.getSellingPrice()));
    }

    @NonNull
    @Override
    public BillItemsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillItemsAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_bill_list,parent,false));
    }
}
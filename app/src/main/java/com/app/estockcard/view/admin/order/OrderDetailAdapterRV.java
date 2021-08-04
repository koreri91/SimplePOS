package com.app.estockcard.view.admin.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.model.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapterRV extends RecyclerView.Adapter<OrderDetailAdapterRV.OrderDetailHolder> {

    private Context context;
    private CurrencyUtil currencyUtil;

    public OrderDetailAdapterRV(Context ctx){
        this.context = ctx;
        this.currencyUtil = new CurrencyUtil();
    }

    class OrderDetailHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_product_item_rv)
        RelativeLayout parentLayout;

        @BindView(R.id.photo)
        AppCompatImageView photo;

        @BindView(R.id.sell_btn)
        AppCompatButton sellBtn;

        @BindView(R.id.name_field)
        AppCompatTextView nameField;

        @BindView(R.id.price_field)
        AppCompatTextView priceField;

        @BindView(R.id.change_quantity_layout)
        LinearLayout changeQuantityLayout;

        @BindView(R.id.quantity_minus_btn)
        AppCompatTextView quantityMinusBtn;

        @BindView(R.id.quantity_item_count)
        AppCompatEditText quantityItemCount;

        @BindView(R.id.quantity_plus_btn)
        AppCompatTextView quantityPlusBtn;


        OrderDetailHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private List<Product> products;

    public void setData(List<Product> products) {
        this.products = products;
    }

    private OrderAdapterRV.OnOrderRVAdapterClick mListener;
    public void setListener(OrderAdapterRV.OnOrderRVAdapterClick listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return (products == null) ? 0 : products.size();
    }

    @NonNull
    @Override
    public OrderDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderDetailHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_order_detail,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHolder orderHolder, int position) {
        Product product = products.get(position);

        if (product.getPhoto() != null) {
            Glide.with(context).asDrawable().override(
                    context.getResources().getDimensionPixelSize(R.dimen.app_default_icon_size))
                    .load(product.getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.DATA).into(orderHolder.photo);
        }else {
            Glide.with(context).asDrawable().override(
                    context.getResources().getDimensionPixelSize(R.dimen.app_default_icon_size))
                    .load(R.drawable.ic_product).into(orderHolder.photo);
        }
        orderHolder.nameField.setText(product.getName());
        orderHolder.quantityItemCount.setText(product.getCountOrdered() + "");
        orderHolder.priceField.setText(currencyUtil.formatIndonesiaCurrency(product.getSellingPrice()));
        orderHolder.sellBtn.setTextColor(ContextCompat.getColor(context,R.color.red500));
        orderHolder.sellBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.selector_red_text_no_solid));

        orderHolder.quantityMinusBtn.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onMenuClick(orderHolder.quantityMinusBtn, position);
            }
        });
        orderHolder.quantityPlusBtn.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onMenuClick(orderHolder.quantityPlusBtn, position);
            }
        });

    }
}

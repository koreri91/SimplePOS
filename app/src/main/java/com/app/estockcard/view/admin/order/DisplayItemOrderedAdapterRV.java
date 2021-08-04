package com.app.estockcard.view.admin.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.model.Product;
import com.app.estockcard.view.admin.InfoHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayItemOrderedAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final Context context;
    private List<Product> products, productsFiltered;
    private OrderAdapterRV.OnOrderRVAdapterClick mListener;

    public DisplayItemOrderedAdapterRV(Context ctx) {
        this.context = ctx;
        this.products = new ArrayList<>();
        this.productsFiltered = new ArrayList<>();
    }

    class OrderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sell_btn)
        AppCompatButton sellBtn;

        @BindView(R.id.name_field)
        AppCompatTextView nameField;

        @BindView(R.id.price_field)
        AppCompatTextView priceField;

        @BindView(R.id.stock_field)
        AppCompatTextView stockField;

        OrderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }



    public void setListener(OrderAdapterRV.OnOrderRVAdapterClick listener) {
        this.mListener = listener;
    }

    public void setSelectionMode(boolean selectionMode) {
    }

    public void setData(List<Product> products) {
        this.products = products;
        this.productsFiltered = products;
    }


    public List<Product> getProducts() {
        return productsFiltered;
    }

    public void removeItem(int position) {
        productsFiltered.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Product item, int position) {
        productsFiltered.add(position, item);
        notifyItemInserted(position);
    }

    private boolean editMode;

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    @Override
    public int getItemViewType(int position) {
        return productsFiltered.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return (productsFiltered == null) ? 0 : productsFiltered.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if (viewType == Product.ProductType) {
            viewHolder = new OrderHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_display_item_ordered, parent, false));
        } else {
            viewHolder = new InfoHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_no_data, parent, false));
        }
        return viewHolder;
    }

    private final String TAG = DisplayItemOrderedAdapterRV.class.getSimpleName();

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Product item = getProducts().get(position);

        if (getItemViewType(position) == Product.ProductType) {
            OrderHolder orderHolder = (OrderHolder) holder;

            orderHolder.nameField.setClickable(false);
            orderHolder.priceField.setClickable(false);

            orderHolder.stockField.setText("Pcs " + item.getCountOrdered());
            orderHolder.sellBtn.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onMenuClick(orderHolder.sellBtn, orderHolder.getAdapterPosition());
                }
            });
            orderHolder.sellBtn.setText(context.getString(R.string.appstr_delete));
            orderHolder.sellBtn.setTextColor(ContextCompat.getColor(context, R.color.red500));
            orderHolder.sellBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.selector_red_text_no_solid));


            orderHolder.nameField.setText(item.getName());
            try {
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"));
                formatter.applyPattern("#,###,###,###");
                String formatted = "Rp " + formatter.format(item.getSellingPrice());
                orderHolder.priceField.setText(formatted);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            InfoHolder infoHolder = (InfoHolder) holder;
            infoHolder.titleCenter.setText(context.getString(R.string.appstr_product_ordered_info));
            Glide.with(context).asDrawable()
                    .load(R.drawable.ic_product)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(200, 300)
                    .into(infoHolder.icon);
        }

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productsFiltered = products;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product item : products) {
                        if (item.getName().equalsIgnoreCase(charString)) {
                            filteredList.add(item);
                        }
                    }
                    productsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                productsFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}

package com.app.estockcard.view.admin.order;

import android.content.Context;
import android.icu.util.MeasureUnit;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class OrderAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final Context context;
    private List<Product> products, productsFiltered;
    private OnOrderRVAdapterClick mListener;

    public OrderAdapterRV(Context ctx) {
        this.context = ctx;
        this.products = new ArrayList<>();
        this.productsFiltered = new ArrayList<>();
    }

    class OrderHolder extends RecyclerView.ViewHolder {

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

        @BindView(R.id.stock_field)
        AppCompatTextView stockField;

        @BindView(R.id.change_quantity_layout)
        LinearLayout changeQuantityLayout;

        @BindView(R.id.quantity_minus_btn)
        AppCompatTextView quantityMinusBtn;

        @BindView(R.id.quantity_item_count)
        AppCompatEditText quantityItemCount;

        @BindView(R.id.quantity_plus_btn)
        AppCompatTextView quantityPlusBtn;


        OrderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnOrderRVAdapterClick {
        void onMenuClick(View view, int position);
    }

    public void setListener(OnOrderRVAdapterClick listener) {
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
            viewHolder = new OrderHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_order, parent, false));
        } else {
            viewHolder = new InfoHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_no_data, parent, false));
        }
        return viewHolder;
    }

    private final String TAG = OrderAdapterRV.class.getSimpleName();

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Product item = getProducts().get(position);

        if (getItemViewType(position) == Product.ProductType) {
            OrderHolder orderHolder = (OrderHolder) holder;

            orderHolder.photo.setClickable(false);
            orderHolder.nameField.setClickable(false);
            orderHolder.priceField.setClickable(false);
            orderHolder.stockField.setClickable(false);
            if (editMode) {
                if (item.getPhoto() != null && item.getCountOrdered() == 0) {
                    Glide.with(context).asDrawable()
                            .load(item.getPhoto()).override(
                            context.getResources().getDimensionPixelSize(R.dimen.app_default_icon_size))
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(orderHolder.photo);
                } else if (item.getCountOrdered() == 0) {
                    Glide.with(context).asDrawable().override(
                            context.getResources().getDimensionPixelSize(R.dimen.app_default_icon_size))
                            .load(R.drawable.ic_product).into(orderHolder.photo);
                }
                if (item.getCountOrdered() > 0) {
                    orderHolder.sellBtn.setVisibility(View.GONE);
                    orderHolder.quantityItemCount.setText(item.getCountOrdered() + "");
                    orderHolder.changeQuantityLayout.setVisibility(View.VISIBLE);
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
                } else {
                    orderHolder.quantityItemCount.setText("1");
                    orderHolder.sellBtn.setVisibility(View.VISIBLE);
                    if (item.getAvailableStock()  == 0 ) {
                        orderHolder.sellBtn.setEnabled(false);
                    }
                    orderHolder.changeQuantityLayout.setVisibility(View.GONE);
                }
                orderHolder.stockField.setText("Stok: " + item.getAvailableStock());
                orderHolder.sellBtn.setOnClickListener(view -> {
                    if (mListener != null) {
                        mListener.onMenuClick(orderHolder.sellBtn, holder.getAdapterPosition());
                    }
                });
            } else {
                orderHolder.photo.setVisibility(View.GONE);
                orderHolder.stockField.setText("Pcs " + item.getCountOrdered());
                orderHolder.changeQuantityLayout.setVisibility(View.GONE);

                orderHolder.sellBtn.setOnClickListener(view -> {
                    if (mListener != null) {
                        mListener.onMenuClick(orderHolder.sellBtn, orderHolder.getAdapterPosition());
                    }
                });
                orderHolder.sellBtn.setVisibility(View.VISIBLE);
                orderHolder.sellBtn.setText(context.getString(R.string.appstr_delete));
                orderHolder.sellBtn.setTextColor(ContextCompat.getColor(context, R.color.red500));
                orderHolder.sellBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.selector_red_text_no_solid));

            }

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

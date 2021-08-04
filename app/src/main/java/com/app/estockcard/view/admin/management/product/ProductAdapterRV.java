package com.app.estockcard.view.admin.management.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.model.Product;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private final Context context;
    private List<Product> products,productsFiltered;
    private OnProductRVAdapterClick mListener;
    private boolean selectionMode;

    public ProductAdapterRV(Context ctx) {
        this.context = ctx;
        this.products = new ArrayList<>();
        this.productsFiltered = new ArrayList<>();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_category)
        RelativeLayout layoutCategory;

        @BindView(R.id.delete_btn)
        AppCompatImageView deleteBtn;

        @BindView(R.id.name_field)
        AppCompatTextView nameField;

        CategoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_product_item_rv)
        RelativeLayout parentLayout;

        @BindView(R.id.photo)
        AppCompatImageView photo;

        @BindView(R.id.delete_btn)
        AppCompatImageView deleteBtn;

        @BindView(R.id.name_field)
        AppCompatTextView nameField;

        @BindView(R.id.price_field)
        AppCompatTextView priceField;

        @BindView(R.id.stock_field)
        AppCompatTextView stockField;

        ProductHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnProductRVAdapterClick {
        void onMenuClick(View view, int position);
    }

    public void setListener(OnProductRVAdapterClick listner) {
        this.mListener = listner;
    }

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
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
        return (viewType == Product.CategoryType)
                ? new CategoryHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_category, parent, false))
                : new ProductHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_product, parent, false));
    }

    private final String TAG = ProductAdapterRV.class.getSimpleName();

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == Product.CategoryType) {
            CategoryHolder categoryHolder = (CategoryHolder) holder;
            categoryHolder.nameField.setText(getProducts().get(position).getName());
            categoryHolder.nameField.setClickable(false);
            categoryHolder.deleteBtn.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onMenuClick(categoryHolder.deleteBtn, holder.getAdapterPosition());
                }
            });
            categoryHolder.layoutCategory.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onMenuClick(categoryHolder.layoutCategory, holder.getAdapterPosition());
                }
            });

            if (selectionMode) {
                categoryHolder.deleteBtn.setVisibility(View.INVISIBLE);
                categoryHolder.deleteBtn.setClickable(false);
                categoryHolder.nameField.setClickable(false);
                categoryHolder.layoutCategory.setClickable(true);
                categoryHolder.layoutCategory.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onMenuClick(categoryHolder.layoutCategory, holder.getAdapterPosition());
                    }
                });
            }

        } else {
            ProductHolder productHolder = (ProductHolder) holder;
            Product item = getProducts().get(position);
            if (item.getPhoto() != null) {
                Glide.with(context).asDrawable().load(item.getPhoto()).into(productHolder.photo);
            }else {
                Glide.with(context).asDrawable().load(R.drawable.ic_product).into(productHolder.photo);
            }
            productHolder.photo.setClickable(false);
            productHolder.nameField.setClickable(false);
            productHolder.priceField.setClickable(false);
            productHolder.stockField.setClickable(false);
            productHolder.parentLayout.setClickable(true);
            productHolder.parentLayout.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onMenuClick(productHolder.parentLayout, holder.getAdapterPosition());
                }
            });
            productHolder.nameField.setText(item.getName());
            try {
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"));
                formatter.applyPattern("#,###,###,###");
                String formatted = "Rp " + formatter.format(item.getSellingPrice());
                productHolder.priceField.setText(formatted);
            } catch (Exception e) {
                e.printStackTrace();
            }
            productHolder.stockField.setText("Stok: " + item.getAvailableStock());
            productHolder.deleteBtn.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onMenuClick(productHolder.deleteBtn, holder.getAdapterPosition());
                }
            });


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

package com.app.estockcard.view.admin.management.discount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.util.TextFormatterUtil;
import com.app.estockcard.model.Discount;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscountAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;

    public DiscountAdapterRV(Context ctx) {
        this.context = ctx;
    }

    class DiscountHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_discount)
        RelativeLayout layoutDiscount;

        @BindView(R.id.delete_btn)
        AppCompatImageView deleteBtn;

        @BindView(R.id.name_field)
        AppCompatTextView nameField;

        @BindView(R.id.display_name_field)
        AppCompatTextView displayNameField;

        DiscountHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnDiscountRVAdapterClick {
        void onMenuClick(View view, int position);
    }


    private List<Discount> allDiscount;
    private OnDiscountRVAdapterClick mListener;
    private boolean selectionMode;

    public void setListener(OnDiscountRVAdapterClick listner) {
        this.mListener = listner;
    }

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
    }

    public void setData(List<Discount> allDiscount) {
        this.allDiscount = allDiscount;
    }

    public List<Discount> getAllDiscount() {
        return allDiscount;
    }

    public void removeItem(int position) {
        allDiscount.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Discount item, int position) {
        allDiscount.add(position, item);
        notifyItemInserted(position);
    }

    @Override
    public int getItemViewType(int position) {
        return allDiscount.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return (allDiscount == null) ? 0 : allDiscount.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiscountHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_discount, parent, false));
    }

    private final String TAG = DiscountAdapterRV.class.getSimpleName();

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        DiscountHolder discountHolder = (DiscountHolder) holder;
        Discount discount = allDiscount.get(position);
        if (selectionMode) {
            discountHolder.deleteBtn.setVisibility(View.INVISIBLE);
            discountHolder.deleteBtn.setClickable(false);
        }else {
            discountHolder.deleteBtn.setVisibility(View.VISIBLE);
            discountHolder.deleteBtn.setClickable(true);
        }
        String valueDisplayed = (discount.getType() == Discount.nominalType ? "Rp " + TextFormatterUtil.moneyFormat(context, discount.getValue()) : ((int) discount.getValue()) + "%");
        discountHolder.displayNameField.setText("Potongan Harga "+valueDisplayed  );
        discountHolder.nameField.setText(discount.getName());

        discountHolder.displayNameField.setClickable(false);
        discountHolder.nameField.setClickable(false);
        discountHolder.layoutDiscount.setClickable(true);

        discountHolder.deleteBtn.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onMenuClick(discountHolder.deleteBtn, holder.getAdapterPosition());
            }
        });
        discountHolder.layoutDiscount.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onMenuClick(discountHolder.layoutDiscount, holder.getAdapterPosition());
            }
        });

    }

}

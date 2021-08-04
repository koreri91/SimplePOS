package com.app.estockcard.view.admin.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListBottomSheet extends BottomSheetDialogFragment implements OrderAdapterRV.OnOrderRVAdapterClick {

    @BindView(R.id.icon_item_ordered)
    AppCompatImageView iconItemOrdered;

    @BindView(R.id.price_display_field)
    AppCompatTextView priceDisplayField;

    @BindView(R.id.pay_btn)
    AppCompatButton payBtn;

    @BindView(R.id.order_list_rv)
    RecyclerView orderListRV;

    private OrderAdapterRV orderAdapterRV;

    private final String TAG = OrderListBottomSheet.class.getSimpleName();

    private BaseActivity parent;
    private List<Product> products;

    public interface OnOrderListBottomSheetListener {
        void onDelete(Product itemRemoved, int position);
    }

    private OnOrderListBottomSheetListener mListener;

    public void setListener(OnOrderListBottomSheetListener listener) {
        this.mListener = listener;
    }

    private int countOfProductOrdered;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_bottom_sheet_order_list, container, false);
        parent = (BaseActivity) getActivity();
        ButterKnife.bind(this, view);

        if (getArguments()!= null ) {
            countOfProductOrdered = Integer.parseInt(Objects.requireNonNull( getArguments().getString("countOfProductOrdered") ));
            setBadgeCount(getActivity(), (LayerDrawable) iconItemOrdered.getDrawable(), getArguments().getString("countOfProductOrdered"));
            priceDisplayField.setText(getArguments().getString("totalPriceOfProductOrdered"));
        }

        orderAdapterRV = new OrderAdapterRV(getActivity());
        orderAdapterRV.setListener(this);
        products = new ArrayList<>();
        if (countOfProductOrdered == 0) {
            products = new ArrayList<>();
            products.add(new Product());
            orderAdapterRV.setData(products);
        } else {
            products = parent.dbManager.getProductsOrdered(Product.ProductType);
            orderAdapterRV.setData(products);
            orderListRV.setItemAnimator(null);
        }

        orderListRV.setAdapter(orderAdapterRV);
        orderListRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        return view;
    }


    @OnClick({R.id.pay_btn})
    void onClick(View view) {
        if (view.getId() == R.id.pay_btn) {
            dismiss();
            startActivity(new Intent(parent, OrderDetailActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        payBtn.setEnabled(countOfProductOrdered > 0);
    }

    public void loadData(int countOfProductOrdered, String totalPriceOfProductOrdered, int position) {

        setBadgeCount(getActivity(), (LayerDrawable) iconItemOrdered.getDrawable(), countOfProductOrdered + "");
        priceDisplayField.setText(totalPriceOfProductOrdered);

        orderAdapterRV.removeItem(position);

        if (orderAdapterRV.getItemCount() == 0) {
            products = new ArrayList<>();
            products.add(new Product());
            orderListRV.setItemAnimator(new DefaultItemAnimator());
            orderAdapterRV.setData(products);
            payBtn.setEnabled(false);
        } else {
            payBtn.setEnabled(true);
        }

    }

    @Override
    public void onMenuClick(View view, final int position) {
        if (view.getId() == R.id.sell_btn) {
            if (mListener != null) {
                mListener.onDelete(products.get(position), position);
            }
        }
    }

    public void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}

package com.app.estockcard.view.admin.order;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Product;
import com.app.estockcard.view.admin.management.product.ProductAddActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderFragment extends Fragment implements OrderAdapterRV.OnOrderRVAdapterClick {

    @BindView(R.id.product_rv)
    RecyclerView productRV;

    @BindView(R.id.product_category_dropdown)
    AppCompatTextView productCategoryDropDown;
    @BindView(R.id.category_name_title)
    AppCompatTextView categoryName;

    @BindView(R.id.add_product_btn)
    AppCompatButton addProductBtn;

    @BindView(R.id.icon_item_ordered)
    AppCompatImageView iconItemOrdered;

    @BindView(R.id.price_display_field)
    AppCompatTextView priceDisplayField;


    private OrderAdapterRV orderAdapterRV;
    private List<Product> products;
    private BaseActivity parent;

    private int categoryIdx = 0;

    private OnFragmentInteractionListener mListener;
    private LayerDrawable badgeDrawable;

    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_order, container, false);
        parent = (BaseActivity) getActivity();
        ButterKnife.bind(this, view);

        initComponent();

        return view;
    }

    private void initComponent(){
        parent.setDrawablePreLollipop(addProductBtn, ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_add), null, null, null);
        parent.setDrawablePreLollipop(productCategoryDropDown, null, null, ContextCompat.getDrawable(parent, R.drawable.ic_arrow_drop_down), null);
        this.products = parent.dbManager.getProduct(Product.ProductType);

        orderAdapterRV = new OrderAdapterRV(parent);
        orderAdapterRV.setListener(this);
        orderAdapterRV.setEditMode(true);
        productRV.setAdapter(orderAdapterRV);
        productRV.setItemAnimator(null);
        productRV.setHasFixedSize(false);
        productRV.setLayoutManager(new LinearLayoutManager(parent, RecyclerView.VERTICAL, false));

        categoryName.setText(getString(R.string.appstr_all_product));
        this.categoryIdx = 0;

        badgeDrawable = (LayerDrawable) iconItemOrdered.getDrawable();
        setBadgeCount(getActivity(), badgeDrawable, "0");
        updateTotalPriceOfItemOrdered();
    }

    public synchronized void setBadgeCount(Context context, LayerDrawable icon, String count) {
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

    public synchronized void cancelAllTransaction(){
        for (Product itemTransaction : parent.dbManager.getProductsOrdered(Product.ProductType)) {
            itemTransaction.setAvailableStock(itemTransaction.getCountOrdered() + itemTransaction.getAvailableStock());
            itemTransaction.setCountOrdered(0);
            parent.dbManager.updateProduct(itemTransaction, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {

                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    public boolean isDoTransaction(){
        return parent.dbManager.getProductsOrdered(Product.ProductType).size() > 0;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        updateTotalPriceOfItemOrdered();
        loadData(categoryIdx, categoryName.getText().toString());
    }

    private int countOfProductOrdered;

    @Override
    public void onMenuClick(View view, int position) {
        Product item = products.get(position);
        if (view.getId() == R.id.sell_btn) {
            products.get(position).setQuantityState(Product.ADD_QUANTITY);
            products.get(position).setCountOrdered(products.get(position).getCountOrdered() + 1);
            products.get(position).setAvailableStock(products.get(position).getAvailableStock() - 1);
            parent.dbManager.updateProduct(item, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    countOfProductOrdered = parent.dbManager.getCountOfProductOrdered();
                    setBadgeCount(getActivity(), badgeDrawable, countOfProductOrdered + "");
                    updateTotalPriceOfItemOrdered();
                }

                @Override
                public void onError(String error) {

                }
            });

            orderAdapterRV.notifyItemChanged(position);

        } else if (view.getId() == R.id.quantity_minus_btn) {
            if (item.getCountOrdered() > 0) {
                item.setAvailableStock(item.getAvailableStock() + 1);
                item.setCountOrdered(item.getCountOrdered() - 1);
                parent.dbManager.updateProduct(item, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        countOfProductOrdered = parent.dbManager.getCountOfProductOrdered();
                        setBadgeCount(getActivity(), badgeDrawable, countOfProductOrdered + "");
                        updateTotalPriceOfItemOrdered();
                    }

                    @Override
                    public void onError(String error) {
                    }
                });

                orderAdapterRV.notifyItemChanged(position);

            }
        } else if (view.getId() == R.id.quantity_plus_btn) {
            if (item.getAvailableStock() > 0) {
                item.setCountOrdered(item.getCountOrdered() + 1);
                item.setAvailableStock(item.getAvailableStock() - 1);

                parent.dbManager.updateProduct(item, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        countOfProductOrdered = parent.dbManager.getCountOfProductOrdered();
                        setBadgeCount(getActivity(), badgeDrawable, countOfProductOrdered + "");
                        updateTotalPriceOfItemOrdered();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

                orderAdapterRV.notifyItemChanged(position);

            }
        }
    }

    private synchronized void updateTotalPriceOfItemOrdered() {
        countOfProductOrdered = parent.dbManager.getCountOfProductOrdered();
        int totalPrice = 0;
        for (Product product : parent.dbManager.getProductsOrdered(Product.ProductType)) {
            totalPrice += product.getCountOrdered() * product.getSellingPrice();
        }
        if (totalPrice == 0) {

            priceDisplayField.setText("Belum ada produk");
            return;
        }

        try {
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"));
            formatter.applyPattern("#,###,###,###");
            String formatted = "Rp " + formatter.format(totalPrice);
            priceDisplayField.setText(formatted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String TAG = getClass().getSimpleName();

    private void loadData(int position, String name) {
        products = position == 0 ? parent.dbManager.getProduct(Product.ProductType) :
                parent.dbManager.getProductsByCategoryId(parent.dbManager.getProductByName(name, Product.CategoryType).getId());
        categoryName.setText(name);
        orderAdapterRV.setData(products);
        orderAdapterRV.notifyDataSetChanged();
        categoryIdx = position;
        setBadgeCount(getActivity(), badgeDrawable, countOfProductOrdered + "");
    }

    private String[] getCategories() {
        List<Product> catItems = parent.dbManager.getProduct(Product.CategoryType);
        String[] items = new String[catItems.size() + 1];
        int idx = 0;
        items[idx++] = getString(R.string.appstr_all_product);
        for (Product category : catItems) {
            items[idx++] = category.getName();
        }
        return items;
    }

    //    private OrderListBottomSheet orderListBottomSheet;
    private BottomSheetDialog sheetDialog;
    private View orderListView;
    private AppCompatImageView iconItemOrderedBottomSheet;
    private AppCompatTextView priceDisplayFieldBottomSheet;
    private RecyclerView itemOrderedRVBottomSheet;
    private DisplayItemOrderedAdapterRV itemOrderedAdapterRVBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private List<Product> itemsOrdered;
    private AppCompatButton payBtn;

    private void bindDataItemOrdered(){
        if (countOfProductOrdered == 0) {
            itemsOrdered = new ArrayList<>();
            itemsOrdered.add(new Product());
            itemOrderedAdapterRVBottomSheet.setData(itemsOrdered);
            payBtn.setEnabled(false);
        } else {
            itemsOrdered = parent.dbManager.getProductsOrdered(Product.ProductType);
            itemOrderedAdapterRVBottomSheet.setData(itemsOrdered);
            payBtn.setEnabled(true);
        }
    }

    private void displayOrderList() {

        sheetDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        orderListView = (LayoutInflater.from(getContext())
                .inflate(R.layout.admin_bottom_sheet_order_list, null));

        sheetDialog.setContentView(orderListView);
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
        sheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                bottomSheetBehavior = BottomSheetBehavior.from((View) orderListView.getParent());
                bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                            // ... do whatever is required on 'expanded'
                        } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            sheetDialog.dismiss();
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            if (Math.abs(slideOffset) > 0) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
                        }
                    }
                });

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        sheetDialog.show();

        payBtn = orderListView.findViewById(R.id.pay_btn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
                startActivity(new Intent(parent, OrderDetailActivity.class));
            }
        });

        iconItemOrderedBottomSheet = orderListView.findViewById(R.id.icon_item_ordered);
        setBadgeCount(getContext(), (LayerDrawable) iconItemOrderedBottomSheet.getDrawable(), countOfProductOrdered + "");
        priceDisplayFieldBottomSheet = orderListView.findViewById(R.id.price_display_field);
        priceDisplayFieldBottomSheet.setText(priceDisplayField.getText());
        itemOrderedRVBottomSheet = orderListView.findViewById(R.id.order_list_rv);

        itemOrderedAdapterRVBottomSheet = new DisplayItemOrderedAdapterRV(getContext());
        itemOrderedRVBottomSheet.setItemAnimator(null);

        bindDataItemOrdered();

        itemOrderedAdapterRVBottomSheet.setListener(new OrderAdapterRV.OnOrderRVAdapterClick() {
            @Override
            public void onMenuClick(View view, final int posItemRemoved) {
                Product itemRemoved = itemsOrdered.get(posItemRemoved);
                final int itemStock = itemRemoved.getCountOrdered() + itemRemoved.getAvailableStock();
                itemRemoved.setAvailableStock(itemStock);
                itemRemoved.setCountOrdered(0);
                parent.dbManager.updateProduct(itemRemoved, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        updateTotalPriceOfItemOrdered();
                        loadData(categoryIdx, categoryName.getText().toString());
                        itemOrderedAdapterRVBottomSheet.removeItem(posItemRemoved);
                        bindDataItemOrdered();
                        setBadgeCount(getContext(), (LayerDrawable) iconItemOrderedBottomSheet.getDrawable(), countOfProductOrdered + "");
                        priceDisplayFieldBottomSheet.setText(priceDisplayField.getText());
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
            }
        });

        itemOrderedRVBottomSheet.setAdapter(itemOrderedAdapterRVBottomSheet);
        itemOrderedRVBottomSheet.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    @OnClick({R.id.add_product_btn, R.id.product_category_dropdown, R.id.manual_order_field, R.id.layout_item_ordered})
    void onClick(View view) {
        if (R.id.add_product_btn == view.getId()) {
            startActivity(new Intent(getActivity(), ProductAddActivity.class));
        } else if (R.id.product_category_dropdown == view.getId()) {
            if (parent.dbManager.getProduct(Product.ProductType).size() == 0) return;
            parent.createDialogList(getCategories(), this::loadData).show();
        } else if (R.id.manual_order_field == view.getId()) {
            startActivity(new Intent(getActivity(), ManualOrderActivity.class));
        } else if (R.id.layout_item_ordered == view.getId()) {
            displayOrderList();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrderFragment.OnFragmentInteractionListener) {
            mListener = (OrderFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sheetDialog != null) {
            sheetDialog.dismiss();
            sheetDialog = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onOrderFragmentInteraction(int orderedItemCount);
    }
}

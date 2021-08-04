package com.app.estockcard.view.admin.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.AppExecutors;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.model.Discount;
import com.app.estockcard.model.Product;
import com.app.estockcard.model.Transaction;
import com.app.estockcard.view.CustomTextView;
import com.app.estockcard.view.ToastDialog;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity implements OrderAdapterRV.OnOrderRVAdapterClick {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrool_view_layout)
    ScrollView scrollView;

    @BindView(R.id.product_list_layout)
    LinearLayout productListLayout;

    @BindView(R.id.cash_payment_layout)
    LinearLayout cashPaymentLayout;

    @BindView(R.id.credit_payment_layout)
    LinearLayout creditPaymentLayout;

    @BindView(R.id.cash_payment_indicator)
    View cashPaymentIndicator;

    @BindView(R.id.credit_payment_indicator)
    View creditPaymentIndicator;

    @BindView(R.id.order_list_rv)
    RecyclerView orderListRV;

    @BindView(R.id.select_customer_layout)
    LinearLayout selectCustomerLayout;

    @BindView(R.id.add_more_item_btn)
    AppCompatTextView addMoreItem;


    @BindView(R.id.select_customer_field)
    CustomTextView selectCustomerField;


    @BindView(R.id.select_discount_field)
    CustomTextView selectDiscountField;

    @BindView(R.id.display_subtotal_field)
    AppCompatTextView displaySubtotalField;

    @BindView(R.id.display_total_field)
    AppCompatTextView displayTotalField;

    @BindView(R.id.display_discount_layout)
    RelativeLayout displayDiscountLayout;

    @BindView(R.id.display_discount_field)
    AppCompatTextView displayDiscountField;

    private OrderDetailAdapterRV orderDetailAdapterRV;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_order_detail);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.appstr_order_detail);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        displayDiscountLayout.setVisibility(View.GONE);

        this.products = dbManager.getProductsOrdered(Product.ProductType);

        orderDetailAdapterRV = new OrderDetailAdapterRV(this);
        orderDetailAdapterRV.setData(products);
        orderDetailAdapterRV.setListener(this);
        orderListRV.setAdapter(orderDetailAdapterRV);
        orderListRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //orderListRV.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        cashPaymentMethodIsSelected = paymentMethodSelected(true);

        selectCustomerField.setDrawableClickListener(new CustomTextView.DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                if (target == DrawablePosition.RIGHT) {
                    customerId = -1;
                    selectCustomerField.setText("");
                    setDrawablePreLollipop(selectCustomerField, null, null, null, null);
                }
            }
        });
        selectDiscountField.setDrawableClickListener(new CustomTextView.DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                if (target == DrawablePosition.RIGHT) {
                    discountId = -1;
                    displayDiscountLayout.setVisibility(View.GONE);
                    selectDiscountField.setText("");
                    setDrawablePreLollipop(selectDiscountField, null, null, null, null);
                    updateTotalPriceOfItemOrdered();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateTotalPriceOfItemOrdered();
    }

    @Override
    public void onMenuClick(View view, final int position) {
        Product item = products.get(position);
        if (view.getId() == R.id.quantity_minus_btn) {
            if (item.getCountOrdered() > 0) {
                boolean removeItem = item.getCountOrdered() == 1;
                SysLog.getInstance().sendLog(TAG, "remove item : " + removeItem + " , position :" + position);
                item.setAvailableStock(item.getAvailableStock() + 1);
                item.setCountOrdered(item.getCountOrdered() - 1);
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        dbManager.updateProduct(item, new OnDBResultListener() {
                            @Override
                            public void onSuccess(String success) {
                                updateTotalPriceOfItemOrdered();
                                if (removeItem) {
                                    products.remove(position);
                                    if (products.size() == 0) {
                                        finish();
                                    }
                                    orderDetailAdapterRV.setData(products);
                                    orderDetailAdapterRV.notifyDataSetChanged();
                                } else {
                                    orderDetailAdapterRV.notifyItemChanged(position);
                                }
                            }

                            @Override
                            public void onError(String error) {
                            }
                        });
                    }
                });

            }
        } else if (view.getId() == R.id.quantity_plus_btn) {
            if (item.getAvailableStock() > 0) {
                item.setCountOrdered(item.getCountOrdered() + 1);
                item.setAvailableStock(item.getAvailableStock() - 1);
                dbManager.updateProduct(item, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        updateTotalPriceOfItemOrdered();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                orderDetailAdapterRV.notifyItemChanged(position);
            }
        }
    }

    private final String TAG = getClass().getSimpleName();
    private double totalPrice;

    private synchronized void updateTotalPriceOfItemOrdered() {

        totalPrice = 0;
        for (Product product : dbManager.getProductsOrdered(Product.ProductType)) {
            totalPrice += product.getCountOrdered() * product.getSellingPrice();
        }
        try {
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"));
            formatter.applyPattern("#,###,###,###");
            String formatted = "Rp " + formatter.format(totalPrice);
            String discountContent = "";
            displaySubtotalField.setText(formatted);
            if (discountId > -1) {
                Discount discItem = dbManager.getDiscount(discountId);
                double discValue;
                if (discItem.getType() == Discount.percentType) {
                    discValue = totalPrice * (discItem.getValue() / 100.f);
                    totalPrice = totalPrice - discValue;
                    discountContent = "(-" + ((int) discItem.getValue()) + "%) Rp " + formatter.format(discValue);
                } else if (discItem.getType() == Discount.nominalType) {
                    discValue = discItem.getValue();
                    totalPrice = totalPrice - discItem.getValue();
                    discountContent = "- Rp " + formatter.format(discValue);
                }
                displayDiscountField.setText(discountContent);
                displayDiscountLayout.setVisibility(View.VISIBLE);
            } else {
                displayDiscountLayout.setVisibility(View.GONE);
            }

            displayTotalField.setText("Rp. " + formatter.format(totalPrice));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private int discountId = -1, customerId = -1;

    private void createBottomSheetDialog(int id) {
        ListBottomSheet listBottomSheet = new ListBottomSheet();
        listBottomSheet.setLayoutID(id);
        listBottomSheet.setListener(new ListBottomSheet.OnListBottomSheetListener() {
            @Override
            public void onItemSelected(String name, int id, int position) {
                if (listBottomSheet.getLayoutID() == R.id.select_customer_layout) {
                    customerId = id;
                    selectCustomerField.setText(name);
                    setDrawablePreLollipop(selectCustomerField, null, null, ContextCompat.getDrawable(OrderDetailActivity.this, R.drawable.ic_close_circle), null);
                } else if (listBottomSheet.getLayoutID() == R.id.select_discount_layout) {
                    discountId = id;
                    updateTotalPriceOfItemOrdered();
                    selectDiscountField.setText(name);
                    setDrawablePreLollipop(selectDiscountField, null, null, ContextCompat.getDrawable(OrderDetailActivity.this, R.drawable.ic_close_circle), null);
                }
            }
            @Override
            public void onTrigerred() {
                if (listBottomSheet.getLayoutID() == R.id.select_customer_layout) {
                    selectCustomerLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.selector_white_btn));
                }
            }
        });
        listBottomSheet.show(getSupportFragmentManager(), "");
    }

    private ToastDialog toastDialog;
    private boolean setErrorCustomerForm;

    @OnClick({R.id.add_more_item_btn, R.id.pay_btn, R.id.select_discount_layout, R.id.select_customer_layout, R.id.cash_payment_layout, R.id.credit_payment_layout})
    void onClick(View view) {
        if (view.getId() == R.id.add_more_item_btn) {
            finish();
        } else if (view.getId() == R.id.select_discount_layout || view.getId() == R.id.select_customer_layout) {
            createBottomSheetDialog(view.getId());
        } else if (view.getId() == R.id.cash_payment_layout) {
            cashPaymentMethodIsSelected = paymentMethodSelected(true);
        } else if (view.getId() == R.id.credit_payment_layout) {
            cashPaymentMethodIsSelected = paymentMethodSelected(false);
        } else if (view.getId() == R.id.pay_btn) {

            if (!cashPaymentMethodIsSelected && customerId == -1) {
                setErrorCustomerForm = true;
                scrollView.setSmoothScrollingEnabled(true);
                scrollView.smoothScrollTo((int) selectCustomerLayout.getX(), (int) selectCustomerLayout.getY());

                if (toastDialog == null) {
                    toastDialog = new ToastDialog(getApplicationContext());
                    toastDialog.setMessage(getString(R.string.appstr_error_customer_not_selected));
                    toastDialog.show();
                } else {
                    toastDialog.show();
                }
                selectCustomerLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.selector_error_white_btn));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Animation shake = AnimationUtils.loadAnimation(OrderDetailActivity.this, R.anim.shake);
                        selectCustomerLayout.startAnimation(shake);
                    }
                }, 500);
                return;
            }
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(getString(R.string.appstr_param_id_customer), customerId);
            intent.putExtra(getString(R.string.appstr_param_id_discount), discountId);
            intent.putExtra(getString(R.string.appstr_param_payment_method), cashPaymentMethodIsSelected ? Transaction.CashPaymentMethod : Transaction.CreditPaymentMethod);
            startActivityForResult(intent, requestCodePayment);
        }
    }

    private final int requestCodePayment = 123;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == requestCodePayment && resultCode == Activity.RESULT_OK) {
            Intent intentPayment = new Intent(this, PaymentResultActivity.class);
            intentPayment.putExtra("transactionID", data.getStringExtra("transactionID"));
            intentPayment.putExtra("moneyPaid", data.getDoubleExtra("moneyPaid", -1));
            intentPayment.putExtra("leftoverMoney", data.getDoubleExtra("leftoverMoney", -1));
            intentPayment.putExtra("transactionDate", data.getStringExtra("transactionDate"));
            startActivity(intentPayment);
            finish();
        }
    }

    private boolean cashPaymentMethodIsSelected;

    private boolean paymentMethodSelected(boolean cashPaymentMethodSelected) {
        cashPaymentIndicator.setBackgroundResource(cashPaymentMethodSelected ? R.drawable.selector_orange_text_field : R.drawable.selector_white_text_field);
        creditPaymentIndicator.setBackgroundResource(!cashPaymentMethodSelected ? R.drawable.selector_orange_text_field : R.drawable.selector_white_text_field);
        return cashPaymentMethodSelected;
    }

}

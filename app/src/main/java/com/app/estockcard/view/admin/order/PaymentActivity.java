package com.app.estockcard.view.admin.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.model.Discount;
import com.app.estockcard.model.Product;
import com.app.estockcard.model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.due_date_payment_layout)
    LinearLayout dueDatePaymentLayout;

    @BindView(R.id.due_date_payment_title)
    AppCompatTextView dueDatePaymentTitle;

    @BindView(R.id.due_date_payment_field)
    AppCompatTextView dueDatePaymentField;

    @BindView(R.id.display_total_billing_field)
    AppCompatTextView displayTotalBillingField;

    @BindView(R.id.recomended_nominal_rv)
    RecyclerView recomendedNominalRV;

    @BindView(R.id.display_info_recomended_nominal)
    AppCompatTextView displayRecomendedNominal;

    @BindView(R.id.leftover_money_field)
    AppCompatTextView leftoverMoneyField;

    @BindView(R.id.money_paid_field)
    AppCompatEditText moneyPaidField;

    @BindView(R.id.paid_money_title)
    AppCompatTextView displaMoneyPaidTitle;

    @BindView(R.id.pay_btn)
    AppCompatButton payBtn;

    private int discountId, customerId;

    private double moneyPaid;
    private int paymentMethod;

    private CurrencyUtil currencyUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_payment);
        ButterKnife.bind(this);
        currencyUtil = new CurrencyUtil();

        paymentMethod = getIntent().getIntExtra(getString(R.string.appstr_param_payment_method), -1);
        toolbar.setTitle(paymentMethod == Transaction.CashPaymentMethod ? getString(R.string.appstr_cash_method) : getString(R.string.appstr_credit_method));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        if (paymentMethod == Transaction.CashPaymentMethod) {
            displaMoneyPaidTitle.setText(Html.fromHtml(getString(R.string.appstr_paid_money) + " <font color='red'>*</font>"));
            findViewById(R.id.due_date_payment_component).setVisibility(View.GONE);
        } else if (paymentMethod == Transaction.CreditPaymentMethod) {
            displaMoneyPaidTitle.setText(Html.fromHtml(getString(R.string.appstr_money_received_in_the_beginning)));
            dueDatePaymentTitle.setText(Html.fromHtml(getString(R.string.appstr_due_date_payment) + " <font color='red'>*</font>"));
            setDrawablePreLollipop(dueDatePaymentField, null, null, ContextCompat.getDrawable(this, R.drawable.ic_date), null);
            dueDatePaymentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putString("countOfProductOrdered", "0");
                    arguments.putString("totalPriceOfProductOrdered", "0");

                    SelectDateBottomSheet selectDateBottomSheet = new SelectDateBottomSheet();
                    selectDateBottomSheet.setListener(new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int yearOfCal, int monthOfYear, int dayOfMonth) {
                            year = yearOfCal;
                            month = monthOfYear;
                            day = dayOfMonth;
                            Calendar calendar = Calendar.getInstance(new Locale("in", "ID"));
                            calendar.set(yearOfCal, monthOfYear - 1, dayOfMonth);
                            dueDatePaymentField.setText(new SimpleDateFormat("EEE, dd MMM yyyy", new Locale("in", "ID")).format(calendar.getTime()));
                            payBtn.setEnabled(true);
                        }
                    });
                    selectDateBottomSheet.show(getSupportFragmentManager(), "");
                }
            });
        }

        recomendedNominalRV.setVisibility(View.GONE);
        displayRecomendedNominal.setVisibility(View.GONE);
        leftoverMoneyField.setVisibility(View.GONE);
        payBtn.setEnabled(false);

        discountId = getIntent().getIntExtra(getString(R.string.appstr_param_id_discount), -1);
        customerId = getIntent().getIntExtra(getString(R.string.appstr_param_id_customer), -1);
        SysLog.getInstance().sendLog(getClass().getSimpleName(),"customer id : "+customerId);

        updateTotalPriceOfItemOrdered();
        moneyPaidField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    moneyPaidField.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,. ]", "");

                    if (cleanString.length() == 0) {
                        moneyPaidField.setText("");
                        moneyPaidField.addTextChangedListener(this);
                        return;
                    }

                    try {
                        moneyPaid = Double.parseDouble(cleanString);
                        String formatted = currencyUtil.formatIndonesiaCurrency(moneyPaid);
                        SysLog.getInstance().sendLog(getClass().getSimpleName()," formatted : "+formatted+" , moneyPaid : "+moneyPaid);
                        current = formatted;
                        moneyPaidField.setText(formatted);
                        moneyPaidField.setSelection(formatted.length());
                        moneyPaidField.addTextChangedListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (moneyPaid < totalPrice) {
                    leftoverMoneyField.setVisibility(View.GONE);
                    payBtn.setEnabled(false);
                } else {
                    if (moneyPaid > totalPrice) {
                        leftoverMoneyField.setVisibility(View.VISIBLE);
                        leftoverMoneyField.setText("Kembalian : "+currencyUtil.formatIndonesiaCurrency(moneyPaid - totalPrice));
                    }
                    payBtn.setEnabled(true);
                }
            }
        });
    }

    private int year, month, day;
    private double subtotal, totalPrice, discountValue;
    private String discountContent;

    private synchronized void updateTotalPriceOfItemOrdered() {

        totalPrice = 0;
        for (Product product : dbManager.getProductsOrdered(Product.ProductType)) {
            totalPrice += product.getCountOrdered() * product.getSellingPrice();
        }
        subtotal = totalPrice;
        try {
            if (discountId > -1) {
                Discount discItem = dbManager.getDiscount(discountId);
                if (discItem.getType() == Discount.percentType) {
                    discountValue = totalPrice * (discItem.getValue() / 100.f);
                    totalPrice = totalPrice - discountValue;
                    discountContent = "(-" + ((int) discItem.getValue()) + "%) " +currencyUtil.formatIndonesiaCurrency(discountValue);
                } else if (discItem.getType() == Discount.nominalType) {
                    discountValue = discItem.getValue();
                    totalPrice = totalPrice - discItem.getValue();
                    discountContent = "- " + currencyUtil.formatIndonesiaCurrency(discountValue);
                }
            }
            SysLog.getInstance().sendLog(getClass().getSimpleName(), "Rp " + currencyUtil.formatIndonesiaCurrency(totalPrice));
            displayTotalBillingField.setText(currencyUtil.formatIndonesiaCurrency(totalPrice));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.billing_details_field, R.id.pay_btn})
    void onClick(View view) {
        if (view.getId() == R.id.billing_details_field) {
            new BilingDetailsDialog(this, dbManager.getProductsOrdered(Product.ProductType), subtotal, discountContent, totalPrice).show();
        } else if (view.getId() == R.id.pay_btn) {

            final Transaction transaction = new Transaction();
            synchronized (this) {
                JSONArray listBill = new JSONArray();
                try {
                    for (Product item : dbManager.getProductsOrdered(Product.ProductType)) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(getString(R.string.appstr_param_name), item.getName());
                        jsonObject.put(getString(R.string.appstr_param_count_ordered), item.getCountOrdered());
                        jsonObject.put(getString(R.string.appstr_param_total_price), item.getSellingPrice());
                        listBill.put(jsonObject);
                    }
                    transaction.setListBill(listBill.toString());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmhh");
            String strDate = sdf.format(new Date());
            SysLog.getInstance().sendLog(getClass().getSimpleName(), "strDate : " + strDate);
            String transactionID = "TRX-" + strDate.substring(0, 2) + strDate.substring(2, strDate.length()) + "-" + getSaltString(4);
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            SysLog.getInstance().sendLog(getClass().getSimpleName(), "date : " + date + " , transactionID : " + transactionID);
            transaction.setId(transactionID);
            transaction.setStatus(Transaction.TRANSACTION_SUCCESS);
            if (paymentMethod == Transaction.CreditPaymentMethod) {
                JSONArray paymentLogs = new JSONArray();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(getString(R.string.appstr_param_date), date);
                    jsonObject.put(getString(R.string.appstr_param_operator), "");
                    jsonObject.put(getString(R.string.appstr_param_paid_money), moneyPaid);
                    paymentLogs.put(jsonObject);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
                transaction.setPaymentLog(paymentLogs.toString());
                transaction.setDownPayment(moneyPaid);
                transaction.setDueDatePayment(year + "/" + month + "/" + day);
                transaction.setStatus(Transaction.TRANSACTION_CREDIT);
            }
            transaction.setDateTime(date);
            transaction.setOperator("");
            transaction.setPaymentMethod(paymentMethod);
            transaction.setBuyerId(customerId);
            transaction.setSubtotal(subtotal);
            transaction.setDiscount(discountId > -1 ? discountValue : 0);
            transaction.setTotal(totalPrice);
            transaction.setLeftoverMoney(moneyPaid - totalPrice);

            dbManager.insertTransaction(transaction, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    updateAvaliableStockProductsOrdered();
                    displayResultTransaction(transaction);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    private synchronized void updateAvaliableStockProductsOrdered() {
        List<Product> productsOrdered = dbManager.getProductsOrdered(Product.ProductType);
        for (Product product : productsOrdered) {
            product.setCountOrdered(0);
            dbManager.updateProduct(product, null);
        }
    }

    private void displayResultTransaction(Transaction transaction) {

        SysLog.getInstance().sendLog(getClass().getSimpleName(), "ID : " + transaction.getId());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Date Time : " + transaction.getDateTime());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Operator : " + transaction.getOperator());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Payment Method : " + transaction.getPaymentMethod());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Down Payment : " + transaction.getDownPayment());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Due Date Payment : " + transaction.getDueDatePayment());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Buyer Id : " + transaction.getBuyerId());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Subtotal : " + transaction.getSubtotal());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Discount : " + transaction.getDiscount());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Total : " + transaction.getTotal());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Leftover Money : " + transaction.getLeftoverMoney());
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "Status : " + transaction.getStatus());

        Intent intent = new Intent();
        intent.putExtra("transactionID", transaction.getId());
        intent.putExtra("paymentMethod", transaction.getPaymentMethod());
        intent.putExtra("moneyPaid", transaction.getTotal());
        intent.putExtra("leftoverMoney", transaction.getLeftoverMoney());
        intent.putExtra("transactionDate", transaction.getDateTime());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}

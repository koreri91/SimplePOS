package com.app.estockcard.view.admin.transaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.model.Product;
import com.app.estockcard.model.Transaction;
import com.app.estockcard.view.admin.order.BillItemsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionDetailActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.total_payment_field)
    AppCompatTextView displayTotalPaymentField;

    @BindView(R.id.id_transaction_field)
    AppCompatTextView displayTransactionIDField;

    @BindView(R.id.display_date_transaction_field)
    AppCompatTextView displayDateTransactionField;

    @BindView(R.id.display_payment_method_field)
    AppCompatTextView displayPaymentMethodField;

    @BindView(R.id.customer_name_layout)
    LinearLayout customerNameLayout;

    @BindView(R.id.display_customer_name_field)
    AppCompatTextView displayCustomerNameField;

    @BindView(R.id.display_operator_name_field)
    AppCompatTextView displayOperatorNameField;

    @BindView(R.id.credit_payment_layout)
    LinearLayout creditPaymentLayout;

    @BindView(R.id.display_credit_amount_field)
    AppCompatTextView displayCreditAmountField;

    @BindView(R.id.display_payment_down_field)
    AppCompatTextView displayPaymentDownField;

    @BindView(R.id.display_due_date_payment_field)
    AppCompatTextView displayDueDatePaymentField;

    @BindView(R.id.display_subtotal_field)
    AppCompatTextView displaySubtotalField;

    @BindView(R.id.discount_layout)
    LinearLayout discountLayout;

    @BindView(R.id.display_discount_field)
    AppCompatTextView displayDiscountField;

    @BindView(R.id.transaction_cancellation_layout)
    LinearLayout transactionCancellationLayout;

    @BindView(R.id.refund_transaction_field)
    AppCompatTextView displayRefundTransactionField;

    @BindView(R.id.total_price_field)
    AppCompatTextView displayTotalPriceField;

    @BindView(R.id.transaction_detail_rv)
    RecyclerView transactionRV;

    @BindView(R.id.transaction_cancel_btn)
    AppCompatTextView transactionCancelBtn;

    @BindView(R.id.remind_payment_btn)
    AppCompatButton remindPaymenBtn;

    @BindView(R.id.print_struct_btn)
    AppCompatButton printStructBtn;

    private List<Transaction> paymentLogs;

    private  Intent data;
    private int paymentMethod;
    private CurrencyUtil currencyUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_transaction_detail);
        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.appstr_transaction_detail));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        currencyUtil = new CurrencyUtil();
        data = getIntent();
        initComponents();
    }

    private double totalPayment;
    private void initComponents(){
        totalPayment = data.getDoubleExtra(getString(R.string.appstr_param_total), -1);

        displayTotalPaymentField.setText(currencyUtil.formatIndonesiaCurrency(totalPayment));
        displayTransactionIDField.setText(getString(R.string.appstr_transaction_id) + ": " + data.getStringExtra(getString(R.string.appstr_param_transactionID)));
        displayDateTransactionField.setText(data.getStringExtra(getString(R.string.appstr_param_dateTime)));
        paymentMethod = data.getIntExtra(getString(R.string.appstr_param_payment_method), -1);


        displayPaymentMethodField.setText(paymentMethod == Transaction.CashPaymentMethod
                ? getString(R.string.appstr_cash_method):getString(R.string.appstr_credit_method));
        if (paymentMethod == Transaction.CreditPaymentMethod) {

            printStructBtn.setText(getString(R.string.appstr_do_payment));
            displayCreditAmountField.setText(currencyUtil.formatIndonesiaCurrency(totalPayment));
            displayPaymentDownField.setText(currencyUtil.formatIndonesiaCurrency(0));

            Locale locale = new Locale(getString(R.string.appstr_locale_language_code), getString(R.string.appstr_locale_country_code));
            String[] dates = data.getStringExtra(getString(R.string.appstr_param_dueDatePayment)).split("/");
            Calendar cal = Calendar.getInstance(locale);
            cal.set(Integer.parseInt(dates[0]),Integer.parseInt(dates[1]),Integer.parseInt(dates[2]));
            displayDueDatePaymentField.setText(new SimpleDateFormat(getString(R.string.appstr_format_date_EEE_dd_MMM_yyyy), locale).format(cal.getTime()));
            paymentLogs = new ArrayList<>();
            try {
                JSONArray listBill = new JSONArray(data.getStringExtra("paymentLog"));
                for (int idx=0; idx < listBill.length(); idx++){
                    JSONObject obj = listBill.getJSONObject(idx);
                    Transaction item = new Transaction();
                    item.setDateTime(obj.getString(getString(R.string.appstr_param_date)));
                    item.setOperator(obj.getString(getString(R.string.appstr_param_operator)));
                    item.setTotal(Integer.parseInt(obj.getString(getString(R.string.appstr_param_paid_money))));
                    paymentLogs.add(item);
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }else {
            printStructBtn.setText(getString(R.string.appstr_print_struct));
            creditPaymentLayout.setVisibility(View.GONE);
        }
        int statusPayment = getIntent().getIntExtra(getString(R.string.appstr_param_status), -1);



        if (statusPayment == Transaction.TRANSACTION_CANCELLED) {
            printStructBtn.setText(getString(R.string.appstr_print_struct));
            displayDueDatePaymentField.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.blue_grey400));
            displayRefundTransactionField.setText("- "+currencyUtil.formatIndonesiaCurrency(totalPayment));
            displayTotalPriceField.setText(currencyUtil.formatIndonesiaCurrency(0));
            remindPaymenBtn.setEnabled(false);
            transactionCancelBtn.setVisibility(View.INVISIBLE);
        }else {
            transactionCancellationLayout.setVisibility(View.GONE);
            displayTotalPriceField.setText(currencyUtil.formatIndonesiaCurrency(totalPayment));
        }

        String customerName = data.getStringExtra(getString(R.string.appstr_param_customerName));
        if (customerName.isEmpty()) {
            customerNameLayout.setVisibility(View.GONE);
        }else {
            displayCustomerNameField.setText(customerName);
        }

        displayOperatorNameField.setText(data.getStringExtra(getString(R.string.appstr_param_operator)));
        displaySubtotalField.setText(currencyUtil.formatIndonesiaCurrency(data.getDoubleExtra(getString(R.string.appstr_subtotal), -1)));
        double discount = data.getDoubleExtra(getString(R.string.appstr_param_discount), 0);
        if (discount > 0) {
            displayDiscountField.setText("- "+currencyUtil.formatIndonesiaCurrency(discount));
        }else {
            discountLayout.setVisibility(View.GONE);
        }


        BillItemsAdapter billItemsAdapter = new BillItemsAdapter(this);
        List<Product> itemsOrdered = new ArrayList<>();
        try {
            JSONArray listBill = new JSONArray(data.getStringExtra("listBill"));
            for (int idx=0; idx < listBill.length(); idx++){
                JSONObject obj = listBill.getJSONObject(idx);
                Product item = new Product();
                item.setName(obj.getString(getString(R.string.appstr_param_name)));
                item.setCountOrdered(Integer.parseInt(obj.getString(getString(R.string.appstr_param_count_ordered))));
                item.setSellingPrice(Integer.parseInt(obj.getString(getString(R.string.appstr_param_total_price))));
                itemsOrdered.add(item);
            }

        }catch (JSONException ex){
            ex.printStackTrace();
        }
        billItemsAdapter.setData(itemsOrdered);
        transactionRV.setAdapter(billItemsAdapter);
        transactionRV.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        transactionRV.addItemDecoration(new DividerItemDecoration(this,RecyclerView.VERTICAL));
    }

    private final int CANCELLING_TRANSACTION = 111;
    @OnClick({R.id.transaction_cancel_btn, R.id.log_payment_btn,R.id.remind_payment_btn,R.id.print_struct_btn})
    void onClick(View view){
        if (view.getId() == R.id.transaction_cancel_btn) {
            Intent intent = new Intent(getApplicationContext(), TransactionPaymentCancelledActivity.class);
            intent.putExtra(getString(R.string.appstr_param_transactionID), data.getStringExtra(getString(R.string.appstr_param_transactionID)));
            startActivityForResult(intent,CANCELLING_TRANSACTION);
        }else if (view.getId() == R.id.log_payment_btn) {
            new PaymentLogsDialog(this,paymentLogs).show();
        }else if (view.getId() == R.id.remind_payment_btn) {

            double totalPaidPayment = 0;
            for (Transaction item : paymentLogs){
                totalPaidPayment+= item.getTotal();
            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            String message = "Utang kamu di Toko "+", dengan kode transaksi "+displayTransactionIDField.getText().toString()+ ": "+
                    displayTotalPaymentField.getText().toString()+", yang sudah dibayar "+", yang belum dibayar ";
            intent.putExtra(Intent.EXTRA_TEXT,message);
            intent.setType("text/plain");
            startActivity(intent);
        }else if (view.getId() == R.id.print_struct_btn ) {
            if (paymentMethod == Transaction.CreditPaymentMethod ) {
                Intent intent = new Intent(getApplicationContext(), TransactionCreditPaymentActivity.class);
                startActivity(intent);
            }else {
                //do print struct
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CANCELLING_TRANSACTION && resultCode == Activity.RESULT_OK) {
            displayRefundTransactionField.setText(totalPayment+"");
            remindPaymenBtn.setEnabled(false);
            transactionCancelBtn.setVisibility(View.INVISIBLE);
        }
    }
}

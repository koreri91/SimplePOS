package com.app.estockcard.view.admin.management.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Switch;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class ProductAddStockPrice extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.enable_stock)
    Switch enableStockFeature;

    @BindView(R.id.available_stock_field)
    AppCompatEditText availableStock;

    @BindView(R.id.minimum_stock_field)
    AppCompatEditText minimumStock;

    @BindView(R.id.purchase_price_field)
    AppCompatEditText purchasePrice;

    @BindView(R.id.selling_price_field)
    AppCompatEditText sellingPrice;

    @BindView(R.id.item_save_btn)
    AppCompatButton saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_add_product_stock_price);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.appstr_add_stock_product);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        sellingPrice.addTextChangedListener(new MoneyFormatter(sellingPrice));
        purchasePrice.addTextChangedListener(new MoneyFormatter(purchasePrice));
    }

    public class MoneyFormatter implements TextWatcher {
        private final AppCompatEditText comp;
        public MoneyFormatter(AppCompatEditText comp){
            this.comp = comp;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        private String current = "";

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                this.comp.removeTextChangedListener(this);

                String cleanString = s.toString().replaceAll("[Rp,. ]", "");

                if (cleanString.length() == 0) {
                    comp.setText("");
                    comp.addTextChangedListener(this);
                    return;
                }


                Locale locale = new Locale("in","ID");
                try {
                    double parsed = Double.parseDouble(cleanString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(locale);
                    formatter.applyPattern("#,###,###,###");
                    String formatted = "Rp " + formatter.format(parsed);

                    current = formatted;
                    comp.setText(formatted);
                    comp.setSelection(formatted.length());
                    comp.addTextChangedListener(this);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            enablingSaveBtn();
        }
    }

    private void  enablingSaveBtn(){
        if ( Objects.requireNonNull(purchasePrice.getText()).length() > 0 && Objects.requireNonNull(sellingPrice.getText()).length() > 0 ){
            saveBtn.setEnabled(true);
        }else {
            saveBtn.setEnabled(false);
        }
    }

    private final String TAG = ProductAddStockPrice.class.getSimpleName();

    @OnClick(R.id.item_save_btn)
    void onClick(){
        int purchaseVal = Integer.parseInt(Objects.requireNonNull(purchasePrice.getText()).toString().replaceAll("[Rp,. ]", ""));
        int sellingVal = Integer.parseInt(Objects.requireNonNull(sellingPrice.getText()).toString().replaceAll("[Rp,. ]", ""));
        Intent data = new Intent();
        data.putExtra("purchasePrice",purchaseVal);
        data.putExtra("sellingPrice",sellingVal);
        data.putExtra("availableStock",enableStockFeature.isChecked() ?  Integer.parseInt(Objects.requireNonNull(availableStock.getText()).toString()) : -1 );
        data.putExtra("minimumStock",enableStockFeature.isChecked() ?  Integer.parseInt(Objects.requireNonNull(minimumStock.getText()).toString()) : -1);
        setResult(Activity.RESULT_OK,data);
        finish();
    }

    @OnCheckedChanged(R.id.enable_stock)
    void onCheck(){
        if (enableStockFeature.isChecked()) {
            availableStock.setEnabled(true);
            minimumStock.setEnabled(true);
        }else {
            availableStock.clearFocus();
            minimumStock.clearFocus();
            availableStock.setEnabled(false);
            minimumStock.setEnabled(false);
        }
    }




}

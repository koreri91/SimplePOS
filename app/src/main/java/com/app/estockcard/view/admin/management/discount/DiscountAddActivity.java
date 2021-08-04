package com.app.estockcard.view.admin.management.discount;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.model.Discount;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscountAddActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.name_field)
    AppCompatEditText nameField;

    @BindView(R.id.percent_toggle_btn)
    AppCompatTextView percentToggleBtn;

    @BindView(R.id.currency_toggle_btn)
    AppCompatTextView currencyToggleBtn;

    @BindView(R.id.discount_description_field)
    AppCompatEditText discountDescriptionField;

    @BindView(R.id.discount_value_field)
    AppCompatEditText discountValueField;

    @BindView(R.id.item_save_btn)
    AppCompatButton saveBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_add_discount);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        idUpdate = intent.getIntExtra("idUpdate",-1) ;

        if (idUpdate > -1) {
            String formattedValue =intent.getFloatExtra("discountValue",-1)+"";
            discountValueField.setText(formattedValue);
            nameField.setText(intent.getStringExtra("name"));
            discountDescriptionField.setText(intent.getStringExtra("description"));

            SysLog.getInstance().sendLog(TAG,"discount value : "+formattedValue);
        }

        if (idUpdate > -1 && intent.getIntExtra("discountType",-1) == Discount.percentType) {
            setSelectRightBtn();
        }else {
            setSelectLeftBtn();
        }

        toolbar.setTitle(  idUpdate > -1 ?  R.string.appstr_edit_discount : R.string.appstr_add_discount );
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSaveBtn();
            }
        });
        discountValueField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current) && currencyValueSelected) {
                    discountValueField.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,. ]", "");

                    if (cleanString.length() == 0) {
                        discountValueField.setText("");
                        discountValueField.addTextChangedListener(this);
                        return;
                    }
                    Locale locale = new Locale("in","ID");
                    try {
                        double parsed = Double.parseDouble(cleanString);

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(locale);
                        formatter.applyPattern("#,###,###,###");
                        String formatted = formatter.format(parsed);

                        current = formatted;
                        discountValueField.setText(formatted);
                        discountValueField.setSelection(formatted.length());
                        discountValueField.addTextChangedListener(this);
                    }catch (Exception e){
                       e.printStackTrace();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSaveBtn();
            }
        });
    }

    private final String TAG = DiscountAddActivity.class.getSimpleName();

    private int idUpdate= -1;

    private void setSelectLeftBtn(){
        percentToggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.toggle_right_btn));
        currencyToggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.toggle_left_btn_selected));
        discountValueField.setText("");
        percentValueSelected = false;
        currencyValueSelected = true;

    }
    private void setSelectRightBtn(){
        percentToggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.toggle_right_btn_selected));
        currencyToggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.toggle_left_btn));
        discountValueField.setText("");
        percentValueSelected = true;
        currencyValueSelected = false;
    }

    private boolean currencyValueSelected;
    private boolean percentValueSelected;

    @OnClick({R.id.currency_toggle_btn,R.id.percent_toggle_btn,R.id.item_save_btn})
    void onClick(View view){
        if (view.getId() == R.id.percent_toggle_btn){
            setSelectRightBtn();
        }else if (view.getId() == R.id.currency_toggle_btn) {
            setSelectLeftBtn();
        }else if (view.getId() == R.id.item_save_btn) {
            Discount discount = new Discount();
            discount.setName(Objects.requireNonNull(nameField.getText()).toString());
            discount.setDescription(Objects.requireNonNull(discountDescriptionField.getText()).toString());
            discount.setValue(Integer.parseInt(Objects.requireNonNull(discountValueField.getText()).toString().replaceAll("[,.]","")));
            discount.setType( (currencyValueSelected) ? Discount.nominalType : Discount.percentType);

            if (idUpdate > 0) {
                discount.setId(idUpdate);
                dbManager.updateDiscount(discount, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        finish();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else {
                dbManager.insertDiscount(discount, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        nameField.setText("");
                        discountDescriptionField.setText("");
                        discountValueField.setText("");
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }

    private void enableSaveBtn(){
        if (Objects.requireNonNull(nameField.getText()).toString().length() > 0 && Objects.requireNonNull(discountValueField.getText()).toString().length() > 0 ) {
            saveBtn.setEnabled(true);
        }else {
            saveBtn.setEnabled(false);
        }
    }


}

package com.app.estockcard.view.admin.management.customer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Employee;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerAddActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.name_field)
    AppCompatEditText nameField;

    @BindView(R.id.cellular_number_field)
    AppCompatEditText cellularNumberField;

    @BindView(R.id.email_field)
    AppCompatEditText emailField;

    @BindView(R.id.city_field)
    AppCompatEditText cityField;


    @BindView(R.id.subdistrict_field)
    AppCompatEditText subdistrictField;


    @BindView(R.id.village_field)
    AppCompatEditText villageField;


    @BindView(R.id.postal_code_field)
    AppCompatEditText postalCodeField;


    @BindView(R.id.address_field)
    AppCompatEditText addressField;

    @BindView(R.id.item_save_btn)
    AppCompatButton saveBtn;

    private int idUpdate=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_add_customer);
        ButterKnife.bind(this);

        idUpdate = getIntent().getIntExtra("idUpdate",-1);
        toolbar.setTitle( (idUpdate > 0) ? R.string.appstr_edit_customer : R.string.appstr_add_customer);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (idUpdate > -1) {
            nameField.setText(getIntent().getStringExtra("fullname"));
            cellularNumberField.setText(getIntent().getStringExtra("phoneNumber"));
            emailField.setText(getIntent().getStringExtra("email"));
            cityField.setText(getIntent().getStringExtra("city"));
            subdistrictField.setText(getIntent().getStringExtra("subdistrict"));
            villageField.setText(getIntent().getStringExtra("village"));
            postalCodeField.setText(getIntent().getStringExtra("postalCode"));
            addressField.setText(getIntent().getStringExtra("address"));
        }

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
        cellularNumberField.addTextChangedListener(new TextWatcher() {
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
        enableSaveBtn();
    }

    private void enableSaveBtn(){
        if (Objects.requireNonNull(nameField.getText()).toString().length() > 0 && Objects.requireNonNull(cellularNumberField.getText()).toString().length() > 0 ) {
            saveBtn.setEnabled(true);
        }else {
            saveBtn.setEnabled(false);
        }
    }

    private void clearFields(){
        nameField.setText("");
        cellularNumberField.setText("");
        emailField.setText("");
        cityField.setText("");
        subdistrictField.setText("");
        villageField.setText("");
        postalCodeField.setText("");
        addressField.setText("");
    }

    @OnClick(R.id.item_save_btn)
    void onClick(View view){
        Employee customer = new Employee();
        customer.setType(Employee.CustomerType);

        customer.setName(Objects.requireNonNull(nameField.getText()).toString());
        customer.setCellularNumber(Objects.requireNonNull(cellularNumberField.getText()).toString());
        customer.setEmail(Objects.requireNonNull(emailField.getText()).toString());
        customer.setCity(Objects.requireNonNull(cityField.getText()).toString());
        customer.setSubDistrict(Objects.requireNonNull(subdistrictField.getText()).toString());
        customer.setVillage(Objects.requireNonNull(villageField.getText()).toString());
        customer.setPostalCode(Objects.requireNonNull(postalCodeField.getText()).toString());
        customer.setAddress(Objects.requireNonNull(addressField.getText()).toString());

        if (idUpdate > -1 ) {
//            SysLog.getInstance().sendLog("update customer data","idUpdate : "+idUpdate);
            customer.setId(idUpdate);
            dbManager.updateEmployee(customer, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    finish();
                }
                @Override
                public void onError(String error) {

                }
            });
        } else {
            dbManager.insertEmployee(customer, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {

                    clearFields();
                }

                @Override
                public void onError(String error) {

                }
            });
        }

    }

}

package com.app.estockcard.view.admin.management.category;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Product;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryAddActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.name_field)
    AppCompatEditText nameField;

    @BindView(R.id.error_message_field)
    AppCompatTextView errorMessageField;

    @BindView(R.id.item_save_btn)
    AppCompatButton saveBtn;

    private int idUpdate = -1;
    private int navigationDrawableId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_add_category);
        ButterKnife.bind(this);

        if (getIntent().getIntExtra("idUpdate",-1) > 0) {
            idUpdate = getIntent().getIntExtra("idUpdate",-1);
            toolbar.setTitle(R.string.appstr_edit_category);
            nameField.setText(getIntent().getStringExtra("categoryName"));
        } else {
            toolbar.setTitle(R.string.appstr_add_category);
        }
        toolbar.setContentInsetStartWithNavigation(0);
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
        enableSaveBtn();
    }

    private void enableSaveBtn(){
        saveBtn.setEnabled(Objects.requireNonNull(nameField.getText()).toString().length() > 0);
    }

    private final String TAG = CategoryAddActivity.class.getSimpleName();

    @OnClick(R.id.item_save_btn)
    void onClick() {
        Product category = new Product();
        category.setName(Objects.requireNonNull(nameField.getText()).toString());
        category.setType(Product.CategoryType);

        if (idUpdate > -1 ) {
            category.setId(idUpdate);

            dbManager.updateProduct(category, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    nameField.setText("");
                    nameField.clearFocus();
                }

                @Override
                public void onError(String error) {
                    errorMessageField.setVisibility(View.VISIBLE);
                    errorMessageField.setText(error);
                }
            });
        }else {
            dbManager.insertProduct(category, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    nameField.setText("");
                    nameField.clearFocus();
                }

                @Override
                public void onError(String error) {
                    errorMessageField.setVisibility(View.VISIBLE);
                    errorMessageField.setText(error);
                }
            });
        }
    }




}

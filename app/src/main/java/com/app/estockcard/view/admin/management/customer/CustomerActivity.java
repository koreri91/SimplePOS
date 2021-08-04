package com.app.estockcard.view.admin.management.customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Employee;
import com.app.estockcard.view.EKardDialogConfirmation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerActivity extends BaseActivity implements CustomerAdapterRV.OnCustomerRVAdapterClick {
    @BindView(R.id.customer_rv)
    RecyclerView customerRV;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_field)
    AppCompatEditText searchField;


    private CustomerAdapterRV customerAdapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_customer);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.appstr_customer);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setDrawablePreLollipop(searchField, null, null, ContextCompat.getDrawable(this, R.drawable.ic_search), null);

        customerAdapterRV = new CustomerAdapterRV(this);
        customerAdapterRV.setListener(this);
        customerRV.setAdapter(customerAdapterRV);
        customerRV.setHasFixedSize(false);
        customerRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        customerRV.addItemDecoration(new DividerItemDecoration(this,RecyclerView.VERTICAL));

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customerAdapterRV.getFilter().filter(s.toString());
            }
        });
    }

    private final String TAG = CustomerActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        this.loadData();
    }

    @OnClick({R.id.search_field, R.id.fab})
    void onClick(View view) {
        if (R.id.fab == view.getId()) {
            startActivity(new Intent(this, CustomerAddActivity.class));
        }
    }


    private void loadData() {
        customerAdapterRV.setData(dbManager.getEmployees(Employee.CustomerType));
        customerAdapterRV.notifyDataSetChanged();
    }

    @Override
    public void onMenuClick(View view, final int position) {

        if (view.getId() == R.id.layout_parent_item_customer_rv) {
            Employee customer = customerAdapterRV.getCustomers().get(position);
            Intent intent = new Intent(this, CustomerAddActivity.class);
            intent.putExtra("fullname", customer.getName());
            intent.putExtra("phoneNumber", customer.getCellularNumber());
            intent.putExtra("email", customer.getEmail());
            intent.putExtra("city", customer.getCity());
            intent.putExtra("subdistrict", customer.getSubDistrict());
            intent.putExtra("village", customer.getVillage());
            intent.putExtra("postalCode", customer.getPostalCode());
            intent.putExtra("address", customer.getAddress());
            intent.putExtra("idUpdate", customer.getId());
            startActivity(intent);
        } else if (view.getId() == R.id.delete_btn) {
            Employee customer = customerAdapterRV.getCustomers().get(position);
            EKardDialogConfirmation confirmation = createDialogConfirmation("Hapus Data Pelanggan?", "Pelanggan '" + customer.getName() + "' akan dihapus dari daftar",
                    "Batal", "Hapus");
            confirmation.show();
            confirmation.setPositiveButtonListener(v -> dbManager.deleteEmployee(customer, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    loadData();
                    confirmation.dismiss();
                }

                @Override
                public void onError(String error) {

                }
            }));
            confirmation.setNegativeButtonListener(v -> confirmation.dismiss());

        }
    }
}

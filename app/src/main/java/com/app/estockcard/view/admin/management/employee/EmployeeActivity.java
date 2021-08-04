package com.app.estockcard.view.admin.management.employee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.model.Employee;
import com.app.estockcard.model.Product;
import com.app.estockcard.view.admin.management.category.CategoryAddActivity;
import com.app.estockcard.view.admin.management.product.ProductAddActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeActivity extends BaseActivity {
    @BindView(R.id.employee_rv)
    RecyclerView employeeRV;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_field)
    AppCompatEditText searchField;

    private EmployeeAdapterRV employeeAdapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_employee);
        ButterKnife.bind(this);

        toolbar.setTitle("Karyawan");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setDrawablePreLollipop(searchField,null,null , ContextCompat.getDrawable(this,R.drawable.ic_search),null);

        employeeAdapterRV = new EmployeeAdapterRV(this);
        employeeRV.setAdapter(employeeAdapterRV);
        employeeRV.setHasFixedSize(false);
        employeeRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadData();
    }

    @OnClick({R.id.search_field,R.id.fab})
    void onClick(View view){
        if (R.id.search_field == view.getId()) {

        }else if (R.id.fab == view.getId()){
            startActivity(new Intent(this, CategoryAddActivity.class));
        }
    }


    private void loadData() {
        employeeAdapterRV.setData(new DBManager().getEmployees(Employee.EmployeeType));
        employeeAdapterRV.notifyDataSetChanged();
    }


    public void displayAddGoodsItem(boolean update, Product item) {
        Intent intent = new Intent(this, ProductAddActivity.class);
        if (update) {
            intent.putExtra("name", item.getName());
            intent.putExtra("dateCreated", item.getDateCreated());
            intent.putExtra("barcode", item.getBarcode());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("quantity", item.getAvailableStock());
            intent.putExtra("photo", item.getPhoto());
            intent.putExtra("idUpdate", item.getId());
        }
        startActivity(intent);
    }

    public void displayAddGoodsGroup(boolean update, Product group) {
        Intent intent = new Intent(this, CategoryAddActivity.class);
        if (update) {
            intent.putExtra("name", group.getName());
            intent.putExtra("dateCreated", group.getDateCreated());
            intent.putExtra("description", group.getDescription());
            intent.putExtra("photo", group.getPhoto());
            intent.putExtra("idUpdate", group.getId());
        }
        startActivity(intent);
    }
}

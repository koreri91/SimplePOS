package com.app.estockcard.view.admin.management.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.app.estockcard.model.Product;
import com.app.estockcard.view.admin.management.product.ProductAdapterRV;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends BaseActivity implements ProductAdapterRV.OnProductRVAdapterClick {
    @BindView(R.id.category_rv)
    RecyclerView categoryRV;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_field)
    AppCompatEditText searchField;

    private ProductAdapterRV categoryAdapterRV;

    public static final int SELECT_TYPE = 11;
    public static final int UNSELECT_TYPE = 10;
    private int FORM_TYPE = UNSELECT_TYPE;

    private final String TAG = CategoryActivity.class.getSimpleName();
    private List<Product> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_category);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.appstr_category));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setDrawablePreLollipop(searchField, null, null, ContextCompat.getDrawable(this, R.drawable.ic_search), null);
        categoryAdapterRV = new ProductAdapterRV(this);

        categoryRV.setAdapter(categoryAdapterRV);
        categoryAdapterRV.setListener(this);
        categoryRV.setHasFixedSize(false);
        categoryRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        categoryRV.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

       if (getIntent().getIntExtra("formType", UNSELECT_TYPE) == SELECT_TYPE) {
            FORM_TYPE = SELECT_TYPE;

            categoryAdapterRV.setSelectionMode(true);
        }
    }


    private void loadData() {
        categories = dbManager.getProduct(Product.CategoryType);
        categoryAdapterRV.setData(categories);
        categoryAdapterRV.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadData();
    }

    @OnClick({R.id.search_field, R.id.fab})
    void onClick(View view) {
        if (R.id.search_field == view.getId()) {

        } else if (R.id.fab == view.getId()) {
            startActivity(new Intent(this, CategoryAddActivity.class));
        }
    }

    @Override
    public void onMenuClick(View view,final int position) {
        if (FORM_TYPE == SELECT_TYPE && view.getId() == R.id.layout_category) {
            Intent data = new Intent();
            data.putExtra("categoryId", categories.get(position).getId());
            data.putExtra("categoryName", categories.get(position).getName());
            setResult(Activity.RESULT_OK, data);
            finish();
        } else if (FORM_TYPE == UNSELECT_TYPE && view.getId() == R.id.layout_category) {
            Product item = categories.get(position);
            Intent intent = new Intent(this, CategoryAddActivity.class);
            intent.putExtra("categoryName", item.getName());
            intent.putExtra("idUpdate", item.getId());
            startActivity(intent);
        }else if (FORM_TYPE == UNSELECT_TYPE && view.getId() == R.id.delete_btn) {
            Product productDelete = categories.get(position);
            dbManager.deleteProduct(productDelete, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    categoryAdapterRV.removeItem(position);
                }

                @Override
                public void onError(String error) {

                }
            });

        }
    }

}

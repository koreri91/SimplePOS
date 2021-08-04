package com.app.estockcard.view.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.model.Product;
import com.app.estockcard.view.admin.management.product.ProductAdapterRV;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestConstraintActivity extends BaseActivity {


    @BindView(R.id.product_rv)
    RecyclerView productRV;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_field)
    AppCompatEditText searchField;
    @BindView(R.id.product_category_dropdown)
    AppCompatTextView productCategoryDropDown;
    @BindView(R.id.category_name_title)
    AppCompatTextView categoryName;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    private ProductAdapterRV productAdapterRV;
    private List<Product> products;


    private int categoryIdx = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_test_constraint);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.appstr_product);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

       setDrawablePreLollipop(searchField, null, null, ContextCompat.getDrawable(this, R.drawable.ic_search), null);
       setDrawablePreLollipop(productCategoryDropDown, null, null, ContextCompat.getDrawable(this, R.drawable.ic_arrow_drop_down), null);

        this.products = dbManager.getProduct(Product.ProductType);

        productAdapterRV = new ProductAdapterRV(this);
        productRV.setAdapter(productAdapterRV);
        productRV.setHasFixedSize(false);
        productRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        productRV.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));

        categoryName.setText(getString(R.string.appstr_all_product));

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                productAdapterRV.getFilter().filter(s.toString());
            }
        });

        productRV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == newState ) {
                    fab.show();
                }else  if (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == newState ) {
                    fab.hide();
                } if (AbsListView.OnScrollListener.SCROLL_STATE_FLING == newState) {
                }
            }

        });

    }

    private final String TAG = TestConstraintActivity.class.getSimpleName();
    @Override
    protected void onResume() {
        super.onResume();
        this.loadData(categoryIdx, categoryName.getText().toString());
    }

    private void loadData(int position, String name) {
        products = position == 0 ? dbManager.getProduct(Product.ProductType) :
                dbManager.getProductsByCategoryId(dbManager.getProductByName(name, Product.CategoryType).getId());

        categoryName.setText(name);
        productAdapterRV.setData(products);
        productAdapterRV.notifyDataSetChanged();
        categoryIdx = position;
    }

    private String[] getCategories() {
        List<Product> catItems = dbManager.getProduct(Product.CategoryType);
        String[] items = new String[catItems.size() + 1];
        int idx = 0;
        items[idx++] = getString(R.string.appstr_all_product);
        for (Product category : catItems) {
            items[idx++] = category.getName();
        }
        return items;
    }

}

package com.app.estockcard.view.admin.management.product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Product;
import com.app.estockcard.view.EKardDialogConfirmation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductActivity extends BaseActivity implements ProductAdapterRV.OnProductRVAdapterClick {

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

    private ProductAdapterRV productAdapterRV;
    private List<Product> products;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_product);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.appstr_product);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setDrawablePreLollipop(searchField, null, null, ContextCompat.getDrawable(this, R.drawable.ic_search), null);
        setDrawablePreLollipop(productCategoryDropDown, null, null, ContextCompat.getDrawable(this, R.drawable.ic_arrow_drop_down), null);

        this.products = dbManager.getProduct(Product.ProductType);

        productAdapterRV = new ProductAdapterRV(this);
        productAdapterRV.setListener(this);
        productRV.setAdapter(productAdapterRV);
        productRV.setHasFixedSize(false);
        productRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
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

    }

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

    private int categoryIdx = 0;


    @OnClick({R.id.search_field, R.id.fab, R.id.product_category_dropdown})
    void onClick(View view) {
        if (R.id.fab == view.getId()) {
            startActivity(new Intent(this, ProductAddActivity.class));
        } else if (R.id.product_category_dropdown == view.getId()) {
            if (dbManager.getProduct(Product.ProductType).size() == 0) return;

            createDialogList(getCategories(), this::loadData).show();

        }
    }

    private final String TAG = ProductActivity.class.getSimpleName();

    @Override
    public void onMenuClick(View view, int position) {
        if (view.getId() == R.id.delete_btn) {
            Product item = productAdapterRV.getProducts().get(position);
            final EKardDialogConfirmation dialogConfirmation = createDialogConfirmation("Hapus Produk",
                    "Produk '" + item.getName() + "' akan dihapus dari inventori.", "Batal", "Ya");
            dialogConfirmation.show();
            dialogConfirmation.setNegativeButtonListener(v -> dialogConfirmation.dismiss());
            dialogConfirmation.setPositiveButtonListener(v -> dbManager.deleteProduct(item, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    productAdapterRV.removeItem(position);
                    dialogConfirmation.dismiss();
                }

                @Override
                public void onError(String error) {
                }
            }));
        } else if (view.getId() == R.id.layout_product_item_rv) {
            Product item = productAdapterRV.getProducts().get(position);
            Intent intent = new Intent(this, ProductAddActivity.class);

            intent.putExtra("photo", item.getPhoto());
            intent.putExtra("name", item.getName());
            intent.putExtra("barcode", item.getBarcode());
            intent.putExtra("sellingPrice", item.getSellingPrice());
            intent.putExtra("purchasePrice", item.getPurchasePrice());
            intent.putExtra("availableStock", item.getAvailableStock());
            intent.putExtra("minimumStock", item.getMinimumStock());
            intent.putExtra("categoryId", item.getCategoryId());
            intent.putExtra("idUpdate", item.getId());
            startActivity(intent);
        }
    }


}






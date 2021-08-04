package com.app.estockcard.view.admin.management.discount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Discount;
import com.app.estockcard.view.EKardDialogConfirmation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscountActivity extends BaseActivity implements DiscountAdapterRV.OnDiscountRVAdapterClick {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.discount_rv)
    RecyclerView discountRV;

    private DiscountAdapterRV discountAdapterRV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_discount);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.appstr_discount);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        discountAdapterRV = new DiscountAdapterRV(this);
        discountAdapterRV.setListener(this);
        discountRV.setAdapter(discountAdapterRV);
        discountRV.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        discountRV.addItemDecoration(new DividerItemDecoration(this,RecyclerView.VERTICAL));
    }

    @OnClick({R.id.fab})
    void onClick(View view){
        if (R.id.fab == view.getId()) {
            startActivity(new Intent(this,DiscountAddActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        discountAdapterRV.setData(dbManager.getAllDiscount());
        discountAdapterRV.notifyDataSetChanged();
    }

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onMenuClick(View view,final int position) {
        Discount discount = discountAdapterRV.getAllDiscount().get(position);
        if (view.getId() == R.id.layout_discount) {
            Intent intent = new Intent(this,DiscountAddActivity.class);
            intent.putExtra("name",discount.getName());
            intent.putExtra("description",discount.getDescription());
            intent.putExtra("discountValue",discount.getValue());
            intent.putExtra("discountType",discount.getType());
            intent.putExtra("idUpdate",discount.getId());
            startActivity(intent);
        }else if (view.getId() == R.id.delete_btn) {
            Discount item = discountAdapterRV.getAllDiscount().get(position);
            final EKardDialogConfirmation dialogConfirmation = createDialogConfirmation("Hapus Diskon",
                    "Diskon '" + item.getName() + "' akan dihapus dari inventori.", "Batal", "Ya");
            dialogConfirmation.show();
            dialogConfirmation.setNegativeButtonListener(v -> dialogConfirmation.dismiss());
            dialogConfirmation.setPositiveButtonListener(v -> dbManager.deleteDiscount(item, new OnDBResultListener() {
                @Override
                public void onSuccess(String success) {
                    discountAdapterRV.removeItem(position);
                    dialogConfirmation.dismiss();
                }

                @Override
                public void onError(String error) {
                }
            }));


        }
    }
}

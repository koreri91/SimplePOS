package com.app.estockcard.view.admin.order;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.model.Product;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BilingDetailsDialog extends Dialog {

    private List<Product> itemsOrdered;
    private double subtotal;
    private String discount;
    private double total;
    public BilingDetailsDialog(Context ctx,List<Product> items,double subtotal,String discount,double total){
        super(ctx);
        this.itemsOrdered = items;
        this.subtotal = subtotal;
        this.discount = discount;
        this.total = total;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @BindView(R.id.data_rv)
    RecyclerView dataRV;

    @BindView(R.id.display_subtotal_field)
    AppCompatTextView displaySubtotal;

    @BindView(R.id.display_discount_layout)
    LinearLayout displayDiscountLayout;

    @BindView(R.id.display_discount_field)
    AppCompatTextView displayDiscount;
    @BindView(R.id.display_total_billing_field)
    AppCompatTextView displayTotal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_billing_details);
        ButterKnife.bind(this);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"));
        formatter.applyPattern("#,###,###,###");

        displaySubtotal.setText("Rp "+formatter.format(subtotal));
        if (discount != null) {
            displayDiscountLayout.setVisibility(View.VISIBLE);
            displayDiscount.setText(discount);
        }
        displayTotal.setText("Rp "+formatter.format(total));
        BillItemsAdapter billingItemsAdapter = new BillItemsAdapter(getContext());
        billingItemsAdapter.setData(itemsOrdered);
        dataRV.setAdapter(billingItemsAdapter);
        dataRV.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }

    @OnClick(R.id.close_btn)
    void onClick(){
        dismiss();
    }


}

package com.app.estockcard.view.admin.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.model.Discount;
import com.app.estockcard.model.Employee;
import com.app.estockcard.view.admin.management.customer.CustomerAdapterRV;
import com.app.estockcard.view.admin.management.customer.CustomerAddActivity;
import com.app.estockcard.view.admin.management.discount.DiscountAdapterRV;
import com.app.estockcard.view.admin.management.discount.DiscountAddActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListBottomSheet extends BottomSheetDialogFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.add_btn)
    AppCompatTextView addBtn;

    @BindView(R.id.title_field)
    AppCompatTextView title;

    @BindView(R.id.display_no_item)
    AppCompatImageView displayNoItem;

    @BindView(R.id.select_item_rv)
    RecyclerView selectItemRV;

    private final String TAG = getClass().getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.admin_bottom_sheet_select_item, container, false);
        ButterKnife.bind(this, view);

        if (layoutID == R.id.select_customer_layout) {
            title.setText(R.string.appstr_customer);
            ((BaseActivity)getContext()).setDrawablePreLollipop(title,ContextCompat.getDrawable(getContext(),R.drawable.ic_customer),null,null,null);
            CustomerAdapterRV customerAdapterRV = new CustomerAdapterRV(getContext());
            final List<Employee> customers = new DBManager().getEmployees(Employee.CustomerType);

            if (mListener != null){
                mListener.onTrigerred();
            }
            if (customers.size() == 0 ) {
                displayNoItem.setVisibility(View.VISIBLE);
            }else {
                customerAdapterRV.setData(customers);
                customerAdapterRV.setSelectionMode(true);
                customerAdapterRV.setListener(new CustomerAdapterRV.OnCustomerRVAdapterClick() {
                    @Override
                    public void onMenuClick(View view, int position) {
                        if (mListener != null) {
                            Employee customer = customers.get(position);
                            mListener.onItemSelected(customer.getName(),customer.getId(),position);
                            dismiss();
                        }
                    }
                });
                selectItemRV.setAdapter(customerAdapterRV);
            }

        }else {
            title.setText(R.string.appstr_discount);
            ((BaseActivity)getContext()).setDrawablePreLollipop(title,ContextCompat.getDrawable(getContext(),R.drawable.ic_discount),null,null,null);
            final List<Discount> allDiscount = new DBManager().getAllDiscount();

            DiscountAdapterRV discountAdapterRV = new DiscountAdapterRV(getContext());

            if (allDiscount.size() == 0 ) {
                displayNoItem.setVisibility(View.VISIBLE);
            }else {
                discountAdapterRV.setData(allDiscount);
                discountAdapterRV.setSelectionMode(true);
                discountAdapterRV.setListener(new DiscountAdapterRV.OnDiscountRVAdapterClick() {
                    @Override
                    public void onMenuClick(View view, int position) {
                        if (mListener != null) {
                            Discount discount = allDiscount.get(position);
                            mListener.onItemSelected(discount.getName(),discount.getId(),position);
                            dismiss();
                        }
                    }
                });
                selectItemRV.setAdapter(discountAdapterRV);
            }
        }
        selectItemRV.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        selectItemRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        return view;
    }
    private int layoutID;
    public void setLayoutID(int id){
        this.layoutID = id;
    }

    public int getLayoutID() {
        return layoutID;
    }

    private OnListBottomSheetListener mListener;
    public void setListener(OnListBottomSheetListener listener){
        this.mListener = listener;
    }

    @OnClick(R.id.add_btn)
    void onClick(View view) {
        if (view.getId() == R.id.add_btn) {
            startActivity(new Intent(getContext(), (layoutID == R.id.select_customer_layout) ? CustomerAddActivity.class : DiscountAddActivity.class));
        }
    }

    public interface OnListBottomSheetListener {
        void onItemSelected(String name,int id, int position);
        void onTrigerred();
    }

}

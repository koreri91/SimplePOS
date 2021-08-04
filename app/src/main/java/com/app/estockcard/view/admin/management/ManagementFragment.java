package com.app.estockcard.view.admin.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.estockcard.R;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.view.admin.management.category.CategoryActivity;
import com.app.estockcard.view.admin.management.customer.CustomerActivity;
import com.app.estockcard.view.admin.management.device.DeviceActivity;
import com.app.estockcard.view.admin.management.discount.DiscountActivity;
import com.app.estockcard.view.admin.management.employee.EmployeeActivity;
import com.app.estockcard.view.admin.management.product.ProductActivity;
import com.app.estockcard.view.admin.management.tax.TaxActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagementFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_management, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

            if (mListener != null) {
                SysLog.getInstance().sendLog(getClass().getSimpleName(),"cancel all transaction");
                mListener.onManagementFragmentInteraction();
            }

    }

    @OnClick({R.id.layout_product,R.id.layout_category,R.id.layout_discount,R.id.layout_customer,
            R.id.layout_employee,R.id.layout_device,R.id.layout_tax})
    void onClick(View view){
        if (view.getId() == R.id.layout_product) {
            startActivity(new Intent(getActivity(), ProductActivity.class));
        }else   if (view.getId() == R.id.layout_category) {
            startActivity(new Intent(getActivity(), CategoryActivity.class));
        }
        else   if (view.getId() ==R.id.layout_discount) {
            startActivity(new Intent(getActivity(), DiscountActivity.class));
        }
        else   if (view.getId() == R.id.layout_customer) {
            startActivity(new Intent(getActivity(), CustomerActivity.class));
        }
        else   if (view.getId() == R.id.layout_employee) {
            startActivity(new Intent(getActivity(), EmployeeActivity.class));
        }
        else   if (view.getId() == R.id.layout_device) {
            startActivity(new Intent(getActivity(), DeviceActivity.class));
        }
        else   if (view.getId() ==R.id.layout_tax) {
            startActivity(new Intent(getActivity(), TaxActivity.class));
        }
    }

    private ManagementFragment.OnFragmentInteractionListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ManagementFragment.OnFragmentInteractionListener) {
            mListener = (ManagementFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onManagementFragmentInteraction();
    }
}

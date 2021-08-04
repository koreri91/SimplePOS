package com.app.estockcard.view.admin.management.employee;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Employee;
import com.app.estockcard.view.admin.AdminMainActivity;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EmployeeFragment extends Fragment implements EmployeeAdapterRV.OnEmployeeRVAdapterClick {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    public EmployeeFragment() {
    }

    public static EmployeeFragment newInstance(String param1, String param2) {
        EmployeeFragment fragment = new EmployeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @BindView(R.id.employee_rv)
    RecyclerView employeeRV;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_employees, container, false);
        ButterKnife.bind(this, view);
        AdminMainActivity parent = (AdminMainActivity) getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        employeeAdapterRV = new EmployeeAdapterRV(getActivity());
        employeeAdapterRV.setListener(this);
    }

    private EmployeeAdapterRV employeeAdapterRV;

    private void loadData(){
        List<Employee> employees = new DBManager().getEmployees(Employee.EmployeeType);
        if (employees.size() == 0) {
            Employee noData = new Employee();
            noData.setDrawableID(R.drawable.ic_employee);
            noData.setName("No Data Employee");
            noData.setViewType(EmployeeAdapterRV.TYPE_HOLDER_NO_DATA);
            employees.add(noData);
        }
        employeeAdapterRV.setData(employees);
        employeeAdapterRV.notifyDataSetChanged();
        employeeRV.setAdapter(employeeAdapterRV);
        employeeRV.setHasFixedSize(false);
        employeeRV.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
    }

    public void displayAddEmployee(boolean update,Employee employee){
        Intent intent = new Intent(getActivity(), EmployeeAddActivity.class);
        if (update) {
            intent.putExtra("photo",employee.getPhoto());
            intent.putExtra("fullname",employee.getName());
            intent.putExtra("residence",employee.getResidenceNumber());
            intent.putExtra("placeBirth",employee.getPlaceBirthday());
            intent.putExtra("dateBirth",employee.getDateBirthday());
            intent.putExtra("gender",employee.getGender());
            intent.putExtra("address",employee.getAddress());
            intent.putExtra("cellularPhoneNumber",employee.getCellularNumber());
            intent.putExtra("whatsAppNumber",employee.getWhatsAppNumber());
            intent.putExtra("idUpdate",employee.getId());
        }
        startActivity(intent);
    }

    @Override
    public void onMenuClick(View view,int position) {
        if (view.getId() == R.id.img_menu) {
            PopupMenu popup = new PopupMenu(Objects.requireNonNull(getActivity()), view);
            popup.getMenuInflater()
                    .inflate(R.menu.admin_popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.popup_action_delete) {
                    new DBManager().deleteEmployee(employeeAdapterRV.getEmployees().get(position), new OnDBResultListener() {
                        @Override
                        public void onSuccess(String success) {
                            employeeAdapterRV.removeItem(position);
                        }
                        @Override
                        public void onError(String error) {
                        }
                    });
                } else if (item.getItemId() == R.id.popup_action_update) {
                    displayAddEmployee(true, employeeAdapterRV.getEmployees().get(position));
                }
                return true;
            });
            popup.show();
        }else if (view.getId() == R.id.img_employee){
            EmployeePhotoDialog employeePhotoDialog = new EmployeePhotoDialog(getActivity());
            employeePhotoDialog.setData(employeeAdapterRV.getEmployees().get(position));
            employeePhotoDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadData();
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onClickEmployeeFab();
    }
}

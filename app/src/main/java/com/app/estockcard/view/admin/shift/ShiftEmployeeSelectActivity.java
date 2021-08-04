package com.app.estockcard.view.admin.shift;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.model.Employee;
import com.app.estockcard.view.admin.management.employee.EmployeeAdapterRV;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShiftEmployeeSelectActivity extends BaseActivity implements EmployeeAdapterRV.OnEmployeeRVAdapterClick {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.start_hour_field)
    AppCompatEditText startHour;

    @BindView(R.id.end_hour_field)
    AppCompatEditText endHour;

    @BindView(R.id.employee_rv)
    RecyclerView employeeRV;

    @BindView(R.id.select_employee_btn)
    AppCompatButton saveBtn;


    private EmployeeAdapterRV employeeAdapterRV;

    private final String TAG = ShiftEmployeeSelectActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_select_employee);
        ButterKnife.bind(this);

        toolbar.setTitle("Pilih Karyawan");
        setSupportActionBar(toolbar);

        employeeAdapterRV = new EmployeeAdapterRV(this);
        List<Employee> employees = new DBManager().getEmployees(Employee.EmployeeType);
        if (employees.size() == 0 ) {
            Employee noData = new Employee();
            noData.setDrawableID(R.drawable.ic_employee);
            noData.setName("No Data Employee");
            noData.setViewType(EmployeeAdapterRV.TYPE_HOLDER_NO_DATA);
            employees.add(noData);
        }
        employeeAdapterRV.setData(employees);
        employeeAdapterRV.setSelectionMode(true);
        employeeAdapterRV.setListener(this);
        employeeRV.setHasFixedSize(false);
        employeeRV.setAdapter(employeeAdapterRV);
        employeeRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        employeeRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //For finding first visible item position
                int firstPosition = Objects.requireNonNull(linearLayoutManager).findFirstCompletelyVisibleItemPosition();
                //For finding last visible item position
                int lastPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (firstPosition == lastPosition && firstPosition > -1) {
                    employeeSelectionIndex = lastPosition;
                }

            }
        });
    }

    private int employeeSelectionIndex = -1;

    @OnClick({R.id.select_employee_btn})
    void onClick(){
        Intent intent = new Intent();
        intent.putExtra("employeeID", employeeAdapterRV.getEmployees().get(employeeSelectionIndex).getId());
        intent.putExtra("employeeName", employeeAdapterRV.getEmployees().get(employeeSelectionIndex).getName());
       intent.putExtra("startHour", Objects.requireNonNull(startHour.getText()).toString());
        intent.putExtra("endHour", Objects.requireNonNull(endHour.getText()).toString());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMenuClick(View view, int position) {
        if (view.getId() == R.id.layout_parent_item_employee_rv) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem itemClose = menu.add(-1,101,200,"");
        itemClose.setIcon(R.drawable.ic_close);
        itemClose.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 101) {
            finish();
        }
        return true;
    }
}

package com.app.estockcard.view.admin.shift;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.model.Shift;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShiftAddScheduleActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.shift_schedule_rv)
    RecyclerView shiftScheduleRV;

    private ShiftAddScheduleBottomSheet shiftAddScheduleBottomSheet;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_add_shift_schedule);
        ButterKnife.bind(this);

        toolbar.setTitle("Jadwal Shift");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        shiftAddScheduleBottomSheet = new ShiftAddScheduleBottomSheet();

        this.dbManager = new DBManager();

        this.loadSchedule();
    }

    private void loadSchedule(){
        ShiftAdapterRV shiftAdapter = new ShiftAdapterRV(this);

        List<Shift> shifts = dbManager.getShifterWithGroup();
        if (shifts.size() == 0 ){
            Shift noData = new Shift();
            noData.setDrawableID(R.drawable.ic_event_not_available);
            noData.setName("No Data Schedule");
            noData.setHolderType(ShiftAdapterRV.TYPE_HOLDER_NO_DATA);
            shifts.add(noData);
        }
        shiftAdapter.setData(shifts);
        shiftScheduleRV.setAdapter(shiftAdapter);
        shiftScheduleRV.setHasFixedSize(false);
        shiftScheduleRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        shiftScheduleRV.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem itemClose = menu.add(-1,101,200,"");
        itemClose.setIcon(R.drawable.ic_event_note);
        itemClose.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 101) {
            shiftAddScheduleBottomSheet.show(getSupportFragmentManager(),"AddShiftTime");
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        shiftAddScheduleBottomSheet.onActivityResult(requestCode,resultCode,data);
    }
}

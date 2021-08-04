package com.app.estockcard.view.admin.shift;

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

public class ShiftHistoryActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.history_rv)RecyclerView historyRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_shift_history);
        ButterKnife.bind(this);

        toolbar.setTitle("Riwayat Shift");
        setSupportActionBar(toolbar);

        loadSchedule();
    }

    private void loadSchedule(){
        ShiftAdapterRV shiftAdapter = new ShiftAdapterRV(this);

        List<Shift> shifts = new DBManager().getShifterWithGroup();
        if (shifts.size() == 0 ){
            Shift noData = new Shift();
            noData.setDrawableID(R.drawable.ic_history);
            noData.setName("No Data History");
            noData.setHolderType(ShiftAdapterRV.TYPE_HOLDER_NO_DATA);
            shifts.add(noData);
        }
        shiftAdapter.setData(shifts);
        historyRV.setAdapter(shiftAdapter);
        historyRV.setHasFixedSize(false);
        historyRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        historyRV.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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

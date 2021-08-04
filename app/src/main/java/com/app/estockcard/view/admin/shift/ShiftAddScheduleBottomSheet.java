package com.app.estockcard.view.admin.shift;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Shift;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShiftAddScheduleBottomSheet extends BottomSheetDialogFragment implements NumberPicker.OnValueChangeListener,ShiftAdapterRV.OnShiftRVAdapterClick {

    @BindView(R.id.title_bottom_sheet)
    AppCompatTextView title;

    @BindView(R.id.year_number_picker)
    NumberPicker yearNumberPicker;
    @BindView(R.id.month_number_picker)
    NumberPicker monthNumberPicker;
    @BindView(R.id.day_number_picker)
    NumberPicker dayNumberPicker;

    @BindView(R.id.shifter_count_field)
    AppCompatEditText shifterCountField;

    @BindView(R.id.message_field)
    AppCompatTextView messageField;

    @BindView(R.id.shift_list_rv)
    RecyclerView shiftListRV;

    private ShiftAdapterRV shiftAdapterRV;

    private final String TAG = ShiftAddScheduleBottomSheet.class.getSimpleName();

    private final String []days = {"Minggu","Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
    private final String []months = {"Januari","Februari","Maret","April","Mei","Juni",
            "Juli","Agustus","September","Oktober","November","Desember"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_bottom_sheet_add_shift_time_, container,false);
        ButterKnife.bind(this,view);

        Calendar calendar = Calendar.getInstance();

        int iYear = calendar.get(Calendar.YEAR);
        int iMonth = calendar.get(Calendar.MONTH); // 1 (months begin with 0)
        int iDay = calendar.get(Calendar.DAY_OF_MONTH);



// Create a calendar object and set year and month
        Calendar myCal = new GregorianCalendar(iYear, iMonth, iDay);

// Get the number of days in that month
        int daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28


        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(30);
        dayNumberPicker.setValue(iDay);

        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        monthNumberPicker.setValue(iMonth+1);

        yearNumberPicker.setMinValue(iYear-2);
        yearNumberPicker.setMaxValue(iYear+3);
        yearNumberPicker.setValue(iYear);

        title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(),monthNumberPicker.getValue()-1,yearNumberPicker.getValue())+", "+dayNumberPicker.getValue()+" "+months[monthNumberPicker.getValue()-1]+" "+yearNumberPicker.getValue());


        dayNumberPicker.setOnValueChangedListener(this);
        monthNumberPicker.setOnValueChangedListener(this);
        yearNumberPicker.setOnValueChangedListener(this);

        messageField.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        shiftAdapterRV = new ShiftAdapterRV(getActivity());
        shiftAdapterRV.setListener(this);
        shiftListRV.setAdapter(shiftAdapterRV);
        shiftListRV.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        shiftListRV.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()),LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onStart() {
        if (shiftAdapterRV.getItemCount() == 0 ) {
            shifterCountField.setText("0");
        }
        super.onStart();
    }

    private String getDayNameInIndonesia(int day, int month, int year){
        Calendar cal = Calendar.getInstance(new Locale("en-us"));
        cal.set(year,month,day);

        String dayName = new SimpleDateFormat("EE").format(cal.getTime());
        if (dayName.equalsIgnoreCase("sun")) {
            dayName = days[0];
        }else  if (dayName.equalsIgnoreCase("mon")) {
            dayName = days[1];
        }else  if (dayName.equalsIgnoreCase("tue")) {
            dayName = days[2];
        }else  if (dayName.equalsIgnoreCase("wed")) {
            dayName = days[3];
        }else  if (dayName.equalsIgnoreCase("thu")) {
            dayName = days[4];
        }else  if (dayName.equalsIgnoreCase("fri")) {
            dayName = days[5];
        }else if (dayName.equalsIgnoreCase("sat")) {
            dayName = days[6];
        }

        return dayName;
    }

    private int lastShiftSelection = -1;

    @Override
    public void onMenuClick(View view,final int position) {

        if (view.getId() == R.id.layout_employee_shift_list_rv) {
            lastShiftSelection = position;
            Objects.requireNonNull(getActivity()).startActivityForResult(new Intent(getActivity(), ShiftEmployeeSelectActivity.class),requestCodeSelectEmployee);
        }
    }

    private static final int requestCodeSelectEmployee = 101;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCodeSelectEmployee && resultCode == Activity.RESULT_OK) {

//            Shift shift = shiftAdapterRV.getShifter().get(lastShiftSelection);
//            shift.setEmployeeID(data.getIntExtra("employeeID",-1));
//            shift.setName(data.getStringExtra("employeeName"));
            //shiftAdapterRV.setItem(shift,lastShiftSelection);

            shiftAdapterRV.getShifter().get(lastShiftSelection).setEmployeeID(data.getIntExtra("employeeID",-1));
            shiftAdapterRV.getShifter().get(lastShiftSelection).setName(data.getStringExtra("employeeName"));
            shiftAdapterRV.getShifter().get(lastShiftSelection).setStartHour(data.getStringExtra("startHour"));
            shiftAdapterRV.getShifter().get(lastShiftSelection).setEndHour(data.getStringExtra("endHour"));
            shiftAdapterRV.notifyDataSetChanged();
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker.getId() == dayNumberPicker.getId()) {
            title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(),monthNumberPicker.getValue()-1,yearNumberPicker.getValue())+", "+dayNumberPicker.getValue()+" "+months[monthNumberPicker.getValue()-1]+" "+yearNumberPicker.getValue());
        }else if (picker.getId() == monthNumberPicker.getId()) {
            int daysInMonth =  new GregorianCalendar(yearNumberPicker.getValue(), monthNumberPicker.getValue()-1, 1).getActualMaximum(Calendar.DAY_OF_MONTH);

            dayNumberPicker.setMinValue(1);
            dayNumberPicker.setMaxValue(daysInMonth);
            title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(),monthNumberPicker.getValue()-1,yearNumberPicker.getValue())+", "+dayNumberPicker.getValue()+" "+months[monthNumberPicker.getValue()-1]+" "+yearNumberPicker.getValue());
        }else  if (picker.getId() == yearNumberPicker.getId()) {
            int daysInMonth =  new GregorianCalendar(yearNumberPicker.getValue(),monthNumberPicker.getValue()-1, 1).getActualMaximum(Calendar.DAY_OF_MONTH);

            dayNumberPicker.setMinValue(1);
            dayNumberPicker.setMaxValue(daysInMonth);
            title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(),monthNumberPicker.getValue()-1,yearNumberPicker.getValue())+", "+dayNumberPicker.getValue()+" "+months[monthNumberPicker.getValue()-1]+" "+yearNumberPicker.getValue());
        }
    }

    @OnClick({R.id.plus_btn,R.id.minus_btn,R.id.cancel_btn,R.id.add_schedule_btn})
    void onClick(View view){
        if (view.getId() == R.id.minus_btn && shiftAdapterRV.getItemCount() > 0) {
            shiftAdapterRV.removeItem(shiftAdapterRV.getShifter().size()-1);
            shifterCountField.setText(shiftAdapterRV.getItemCount()+"");
        }else if (view.getId() == R.id.plus_btn) {
            Shift shift = new Shift();
            shift.setName("Pilih Karyawan");
            shift.setStartHour("00:00");
            shift.setEndHour("24:00");
            shift.setHolderType(ShiftAdapterRV.TYPE_HOLDER_INPUT);
            shiftAdapterRV.add(shift,shiftListRV);
            shifterCountField.setText(shiftAdapterRV.getItemCount()+"");
        }else if (view.getId() == R.id.cancel_btn) {
            dismiss();
        }else if (view.getId() == R.id.add_schedule_btn) {
            saveData();
        }
    }

    private synchronized void saveData(){

        boolean formIsFilled=true;
        //check input form
        for (Shift shift : shiftAdapterRV.getShifter()) {
            if (shift.getEmployeeID() == -1 ) {
                formIsFilled = false;
            }
        }

        if (formIsFilled) {
            String groupId = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            for (Shift shift : shiftAdapterRV.getShifter() ) {

                shift.setGroupID(groupId);
                shift.setDateWork(dayNumberPicker.getValue()+"/"+monthNumberPicker.getValue()+"/"+yearNumberPicker.getValue());
                new DBManager().insertShift(shift, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }else {
            messageField.setVisibility(View.VISIBLE);
            messageField.setText("Data form belum dilengkapi");
        }

    }

}

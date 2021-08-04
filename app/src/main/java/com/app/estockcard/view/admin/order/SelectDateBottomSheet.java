package com.app.estockcard.view.admin.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.app.estockcard.R;
import com.app.estockcard.controller.SysLog;
import com.app.estockcard.view.ToastDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectDateBottomSheet extends BottomSheetDialogFragment implements NumberPicker.OnValueChangeListener {

    @BindView(R.id.title_bottom_sheet)
    AppCompatTextView title;


    @BindView(R.id.year_number_picker)
    NumberPicker yearNumberPicker;
    @BindView(R.id.month_number_picker)
    NumberPicker monthNumberPicker;
    @BindView(R.id.day_number_picker)
    NumberPicker dayNumberPicker;


    private final String TAG = SelectDateBottomSheet.class.getSimpleName();

    private final String[] days = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
    private final String[] months = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DatePicker.OnDateChangedListener mListener;

    public void setListener(DatePicker.OnDateChangedListener listener) {
        mListener = listener;
    }

    private int iYear, iMonth, iDay;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_bottom_sheet_select_date, container, false);
        ButterKnife.bind(this, view);

        Calendar calendar = Calendar.getInstance(new Locale("in", "ID"));

        iYear = calendar.get(Calendar.YEAR);
        iMonth = calendar.get(Calendar.MONTH) + 1; // 1 (months begin with 0)
        iDay = calendar.get(Calendar.DAY_OF_MONTH);
        // Create a calendar object and set year and month
        Calendar myCal = new GregorianCalendar(iYear, iMonth, iDay);

        // Get the number of days in that month
        int daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28

        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(30);
        dayNumberPicker.setValue(iDay);

        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        monthNumberPicker.setValue(iMonth);

        yearNumberPicker.setMinValue(iYear - 2);
        yearNumberPicker.setMaxValue(iYear + 3);
        yearNumberPicker.setValue(iYear);

        title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(), monthNumberPicker.getValue() - 1, yearNumberPicker.getValue()) + ", " + dayNumberPicker.getValue() + " " + months[monthNumberPicker.getValue() - 1] + " " + yearNumberPicker.getValue());

        dayNumberPicker.setOnValueChangedListener(this);
        monthNumberPicker.setOnValueChangedListener(this);
        yearNumberPicker.setOnValueChangedListener(this);


        onValueChange(monthNumberPicker,0,0);

        return view;
    }

    private String getDayNameInIndonesia(int day, int month, int year) {
        Calendar cal = Calendar.getInstance(new Locale("en-us"));
        cal.set(year, month, day);

        String dayName = new SimpleDateFormat("EE").format(cal.getTime());
        if (dayName.equalsIgnoreCase("sun")) {
            dayName = days[0];
        } else if (dayName.equalsIgnoreCase("mon")) {
            dayName = days[1];
        } else if (dayName.equalsIgnoreCase("tue")) {
            dayName = days[2];
        } else if (dayName.equalsIgnoreCase("wed")) {
            dayName = days[3];
        } else if (dayName.equalsIgnoreCase("thu")) {
            dayName = days[4];
        } else if (dayName.equalsIgnoreCase("fri")) {
            dayName = days[5];
        } else if (dayName.equalsIgnoreCase("sat")) {
            dayName = days[6];
        }

        return dayName;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker.getId() == dayNumberPicker.getId()) {
            title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(), monthNumberPicker.getValue() - 1, yearNumberPicker.getValue()) + ", " + dayNumberPicker.getValue() + " " + months[monthNumberPicker.getValue() - 1] + " " + yearNumberPicker.getValue());
        } else if (picker.getId() == monthNumberPicker.getId()) {
            int daysInMonth = new GregorianCalendar(yearNumberPicker.getValue(), monthNumberPicker.getValue() - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH);

            dayNumberPicker.setMinValue(1);
            dayNumberPicker.setMaxValue(daysInMonth);
            title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(), monthNumberPicker.getValue() - 1, yearNumberPicker.getValue()) + ", " + dayNumberPicker.getValue() + " " + months[monthNumberPicker.getValue() - 1] + " " + yearNumberPicker.getValue());
        } else if (picker.getId() == yearNumberPicker.getId()) {
            int daysInMonth = new GregorianCalendar(yearNumberPicker.getValue(), monthNumberPicker.getValue() - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH);

            dayNumberPicker.setMinValue(1);
            dayNumberPicker.setMaxValue(daysInMonth);
            title.setText(getDayNameInIndonesia(dayNumberPicker.getValue(), monthNumberPicker.getValue() - 1, yearNumberPicker.getValue()) + ", " + dayNumberPicker.getValue() + " " + months[monthNumberPicker.getValue() - 1] + " " + yearNumberPicker.getValue());
        }
    }

    @OnClick({R.id.select_btn})
    void onClick(View view) {
        if (iYear == yearNumberPicker.getValue() && iMonth == monthNumberPicker.getValue() && iDay == dayNumberPicker.getValue()) {
            ToastDialog toastDialog = new ToastDialog(getContext(),getString(R.string.appstr_error_due_date_payment_input),false);
            toastDialog.show();
        }else if (mListener != null) {
            SysLog.getInstance().sendLog(TAG, "year : " + yearNumberPicker.getValue() + ", month : " + monthNumberPicker.getValue() + ", day : " + dayNumberPicker.getValue());
            mListener.onDateChanged(null, yearNumberPicker.getValue(), monthNumberPicker.getValue(), dayNumberPicker.getValue());
            dismiss();
        }

    }


}

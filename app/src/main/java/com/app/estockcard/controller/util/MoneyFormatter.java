package com.app.estockcard.controller.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatEditText;

import com.app.estockcard.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatter implements TextWatcher {

    private final AppCompatEditText comp;
    private Context context;

    public MoneyFormatter(Context context, AppCompatEditText comp) {
        this.context = context;
        this.comp = comp;
    }

    private final String TAG = getClass().getSimpleName();


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    private String current = "";

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(current)) {
            comp.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll(context.getString(R.string.appstr_regex), context.getString(R.string.appstr_zero_space));

            if (cleanString.length() == 0) {
                comp.setText("");
                comp.addTextChangedListener(this);
                return;
            }
            Locale locale = new Locale(context.getString(R.string.appstr_locale_language_code), context.getString(R.string.appstr_locale_country_code));
            try {
                double parsed = Double.parseDouble(cleanString);

                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(locale);
                formatter.applyPattern(context.getString(R.string.appstr_format_currency));
                String formatted = context.getString(R.string.appstr_currency_symbol)
                        .concat(context.getString(R.string.appstr_space)) + formatter.format(parsed);

                current = formatted;
                comp.setText(formatted);
                comp.setSelection(formatted.length());
                comp.addTextChangedListener(this);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}

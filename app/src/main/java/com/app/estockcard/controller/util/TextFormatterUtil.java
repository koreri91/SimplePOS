package com.app.estockcard.controller.util;

import android.content.Context;

import com.app.estockcard.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class TextFormatterUtil {

    public final static String moneyFormat(Context context, double value) {
        Locale locale = new Locale(context.getString(R.string.appstr_locale_language_code), context.getString(R.string.appstr_locale_country_code));
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(locale);
        formatter.applyPattern(context.getString(R.string.appstr_format_currency));
        return formatter.format(value);
    }
}

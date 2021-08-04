package com.app.estockcard.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {
    private  DecimalFormat indoNumFormat;

    public CurrencyUtil(){
        indoNumFormat = (DecimalFormat) NumberFormat.getInstance(new Locale("in", "ID"));
        indoNumFormat.applyPattern("#,###,###,###");
    }

    public String formatIndonesiaCurrency(double value){
        return "Rp "+ indoNumFormat.format(value);
    }
}

package com.app.estockcard.controller;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatDelegate;

import com.app.estockcard.R;

import java.lang.reflect.Field;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppConfig extends Application {


    public static final int SELECT_CATEGORY = 801;
    public static final int SET_STOCK_PRICE = 701;

    public static final int PICK_IMAGE = 101;
    public static final int SCAN_BARCODE = 901;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name(getResources().getString(R.string.appstr_db_name))
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
       // TypefaceUtil.overrideFont(getApplicationContext(),"Light","roboto_light.ttf");
        setDefaultFont(this, "DEFAULT", "roboto.ttf");
    }

    private static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    private static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

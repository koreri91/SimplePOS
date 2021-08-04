package com.app.estockcard.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.estockcard.R;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.appstr_param_shared_preferences), MODE_PRIVATE);
    }

    public void firstTimeAsking(String permission, boolean isFirstTime) {
        doEdit();
        editor.putBoolean(permission, isFirstTime);
        doCommit();
    }

    public boolean isFirstTimeAsking(String permission) {
        return sharedPreferences.getBoolean(permission, true);
    }

    private void doEdit() {
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
    }

    private void doCommit() {
        if (editor != null) {
            editor.commit();
            editor = null;
        }
    }

}

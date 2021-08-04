package com.app.estockcard.controller;

import android.content.Context;
import android.content.pm.PackageManager;

import com.app.estockcard.R;

public class PackMan {

    private PackageManager pm;
    private Context ctx;
    public PackMan(Context ctx){
        this.pm = ctx.getPackageManager();
        this.ctx = ctx;
    }


    public String getSource(String id){
        String rst = pm.getInstallerPackageName(id);
        return rst;
    }

    public boolean testingSource(String source){
        String src = ctx.getString(R.string.appstr_param_playstore);

        boolean rst =  src.equals(source);
        return rst;
    }

}

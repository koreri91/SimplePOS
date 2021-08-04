package com.app.estockcard.controller;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.estockcard.BuildConfig;
import com.app.estockcard.R;
import com.app.estockcard.view.EKardDialogConfirmation;
import com.app.estockcard.view.EKardDialogList;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class BaseActivity extends AppCompatActivity implements NetworkImpl {

    public DBManager dbManager;
    private PermissionManager permissionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        permissionManager = new PermissionManager(this);
        dbManager = new DBManager();
    }

    protected void initDB(View.OnClickListener click, View.OnClickListener errorHandler){
        PackMan packMan = new PackMan(this);
        if (!BuildConfig.DEBUG && !packMan.testingSource(packMan.getSource(getPackageName()))) {
            errorHandler.onClick(null);
        }else if (BuildConfig.DEBUG) {
            click.onClick(null);
        }else {
            click.onClick(null);
        }
    }

    protected static final int REQUEST_ALL_PERMISSIONS = 123;
    protected final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
    };
    protected boolean hasPermission(String permission) {
        return  ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }
    protected void initPermissions(){
        if (permissionManager.shouldAskPermission()) {
            permissionManager.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionManager.PermissionAskListener() {
                @Override
                public void onNeedPermission() {
                    ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                @Override
                public void onPermissionPreviouslyDenied() {
                    showRational(getString(R.string.appstr_permission_denied), "", Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCodeWriteExternalStorage);
                }

                @Override
                public void onPermissionPreviouslyDeniedWithNeverAskAgain() {
                    dialogForSettings(getString(R.string.appstr_permission_denied), "Now you must allow write external storage from settings.");
                }

                @Override
                public void onPermissionGranted() {

                }
            });
            permissionManager.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionManager.PermissionAskListener() {
                @Override
                public void onNeedPermission() {
                     ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
                }

                @Override
                public void onPermissionPreviouslyDenied() {
                    showRational(getString(R.string.appstr_permission_denied), "", Manifest.permission.READ_EXTERNAL_STORAGE, requestCodeReadExternalStorage);
                }

                @Override
                public void onPermissionPreviouslyDeniedWithNeverAskAgain() {
                    dialogForSettings(getString(R.string.appstr_permission_denied), "Now you must allow read external storage from settings.");
                }

                @Override
                public void onPermissionGranted() {
                }
            });
        }
    }

    private final int requestCodeReadExternalStorage=110;
    private final int requestCodeWriteExternalStorage=120;

    private void goToSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse(getString(R.string.appstr_param_package) + getPackageName());
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    private void showRational(String title,String msg,String permissionName,int requestCode) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.appstr_im_sure).toUpperCase(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.appstr_retry).toUpperCase(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BaseActivity.this, new String[]{permissionName}, requestCode);
                        dialog.dismiss();
                    }
                }).show();

    }


    private void dialogForSettings(String title, String msg) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.appstr_not_now).toUpperCase(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.appstr_settings).toUpperCase(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToSettings();
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public boolean isOnline(View.OnClickListener onClickListener) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkRequest.Builder request = new NetworkRequest.Builder();
                request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

                connectivityManager.requestNetwork(request.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            if ( ConnectivityManager.setProcessDefaultNetwork(network) ) {

                                SysLog.getInstance().sendLog(TAG, "ConnectivityManager.setProcessDefaultNetwork(network) ");
                                onClickListener.onClick(null);
                            }else {
                                SysLog.getInstance().sendLog(TAG, getString(R.string.appstr_network_no_longer_valid));
                            }
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            connectivityManager.bindProcessToNetwork(network);
                            SysLog.getInstance().sendLog(TAG, "connectivityManager.bindProcessToNetwork(network) ");
                            onClickListener.onClick(null);
                        }


                    }
                });


            }
            return true;
        } else
            return false;
    }

    public void setDrawablePreLollipop(final TextView comp, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        comp.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }

    public EKardDialogList createDialogList(String[] items, EKardDialogList.OnDialogListListener onDialogListListener) {
        return new EKardDialogList(this, items, onDialogListListener);
    }

    protected EKardDialogConfirmation createDialogConfirmation(String title, String message, String negativeBtnName, String positiveBtnName) {
        return new EKardDialogConfirmation(this, title, message, negativeBtnName, positiveBtnName);
    }

    protected Snackbar displaySnackbar(View view, String message, int duration, String titleAction, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        snackbar.setAction(titleAction, listener);
        snackbar.show();
        return snackbar;
    }



    private final String TAG = BaseActivity.class.getSimpleName();

    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected String getSaltString(int length) {
        String SALTCHARS = getString(R.string.appstr_alphabetnumeric);
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        int counter = 0;
        while (counter < length) {
            ++counter;
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

}

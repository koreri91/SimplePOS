package com.app.estockcard.view.admin.settings;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.SysLog;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackupDataActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    AppCompatTextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.lastdate_local_backup)
    AppCompatTextView lastdateLocalBackup;
    @BindView(R.id.lastdate_google_backup)
    AppCompatTextView lastdateGoogleBackup;

    @BindView(R.id.schedule_display)
    AppCompatTextView scheduleDisplay;

    @BindView(R.id.account_display)
    AppCompatTextView accountDisplay;

    @BindView(R.id.over_media_type_display)
    AppCompatTextView overMediaTypeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_settings_backup_data);
        ButterKnife.bind(this);

        title.setText(getString(R.string.appstr_backup_data));
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @OnClick({R.id.backup_btn, R.id.schedule_layout, R.id.account_layout, R.id.backup_over_media_layout})
    void onClick(View view) {
        if (view.getId() == R.id.backup_btn) {

        } else if (view.getId() == R.id.schedule_layout) {
            ChooseItemDialog itemDialog = new ChooseItemDialog(this, getResources().getStringArray(R.array.schedule),
                    getString(R.string.appstr_backup_schedule_title));
            itemDialog.setListener(new ChooseItemAdapter.OnChooseItemAdapter() {
                @Override
                public void onClick(String name) {
                    scheduleDisplay.setText(name);
                }
            });
            itemDialog.show();
        } else if (view.getId() == R.id.account_layout) {
            AccountManager accountManager = AccountManager.get(this);
            Account []accounts = accountManager.getAccountsByType(getString(R.string.appstr_gmail_type_account));
            String[] data = new String[accounts.length + 1];
            int idx = 0;
            for (Account account : accounts) {
                SysLog.getInstance().sendLog(getClass().getSimpleName(), " " + account.name + " , " + account.type);
                data[idx++] = account.name;
            }
            data[idx] = getString(R.string.appstr_add_account);
            ChooseItemDialog itemDialog = new ChooseItemDialog(this, data,
                    getString(R.string.appstr_backup_account_title));
            itemDialog.setListener(new ChooseItemAdapter.OnChooseItemAdapter() {
                @Override
                public void onClick(String name) {
                    if  (name.equals(getString(R.string.appstr_add_account))) {
                        accountManager.addAccount(getString(R.string.appstr_gmail_type_account),null,null,null,BackupDataActivity.this,
                                null,null);
                    }else {
                        accountDisplay.setText(name);
                    }
                }
            });
            itemDialog.show();
        } else if (view.getId() == R.id.backup_over_media_layout) {
            ChooseItemDialog itemDialog = new ChooseItemDialog(this, getResources().getStringArray(R.array.comm_media),
                    getString(R.string.appstr_backup_over_media_title));
            itemDialog.setListener(new ChooseItemAdapter.OnChooseItemAdapter() {
                @Override
                public void onClick(String name) {
                    overMediaTypeDisplay.setText(name);
                }
            });
            itemDialog.show();
        }
    }
}

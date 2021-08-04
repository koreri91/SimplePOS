package com.app.estockcard.view.admin.settings;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    AppCompatTextView title;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.web_content)
    AppCompatTextView webContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_settings_privacypolicy);
        ButterKnife.bind(this);

        title.setText(getString(R.string.appstr_privacy_policy));
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        char []arrContent = null;
        try {
            InputStream inputStream = getAssets().open("privacy");
            InputStreamReader isr = new InputStreamReader(inputStream);
            arrContent = new char[inputStream.available()];
            isr.read(arrContent,0,arrContent.length);
            isr.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            webContent.setText(Html.fromHtml(new String(arrContent), Html.FROM_HTML_MODE_COMPACT));
        }else {
            webContent.setText(Html.fromHtml(new String(arrContent)));
        }
    }
}

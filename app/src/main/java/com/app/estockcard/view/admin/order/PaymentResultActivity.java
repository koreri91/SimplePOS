package com.app.estockcard.view.admin.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.CurrencyUtil;
import com.app.estockcard.controller.SysLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentResultActivity extends BaseActivity {

    @BindView(R.id.print_struct_btn)
    LinearLayout printStructBtn;

    @BindView(R.id.send_email_btn)
    LinearLayout sendEmailBtn;

    @BindView(R.id.display_paid_money_field)
    AppCompatTextView displayPaidMoneyField;

    @BindView(R.id.display_leftover_money_field)
    AppCompatTextView displayLeftoverMoneyField;

    @BindView(R.id.display_date_transaction_field)
    AppCompatTextView displayDateTransactionField;

    @BindView(R.id.display_id_transaction_field)
    AppCompatTextView displayIDTransactionField;

    private CurrencyUtil currencyUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_result_payment);
        ButterKnife.bind(this);
        currencyUtil = new CurrencyUtil();

        SysLog.getInstance().sendLog(getClass().getSimpleName(), "date intent : " + getIntent().getStringExtra("transactionDate"));
        String date = getIntent().getStringExtra("transactionDate").split(" ")[0];
        int year = Integer.parseInt(date.split("/")[0]);
        int month = Integer.parseInt(date.split("/")[1]);
        int day = Integer.parseInt(date.split("/")[2]);
        SysLog.getInstance().sendLog(getClass().getSimpleName(), "date : " + date);

        Calendar calendar = Calendar.getInstance(new Locale("in", "ID"));
        calendar.set(year, month - 1, day);

        SimpleDateFormat sdf = new SimpleDateFormat("EE, dd MMM yyyy", new Locale("in", "ID"));

        displayDateTransactionField.setText(sdf.format(calendar.getTime()));
        displayIDTransactionField.setText(getIntent().getStringExtra("transactionID"));
        displayPaidMoneyField.setText(currencyUtil.formatIndonesiaCurrency(getIntent().getDoubleExtra("moneyPaid", -1)));
        double leftoverMoney = getIntent().getDoubleExtra("leftoverMoney", -1);
        if (leftoverMoney > 0) {
            displayLeftoverMoneyField.setVisibility(View.VISIBLE);
            displayLeftoverMoneyField.setText("Kembalian " + currencyUtil.formatIndonesiaCurrency(leftoverMoney));
        }


    }

    @OnClick({R.id.send_email_btn, R.id.print_struct_btn, R.id.close_btn, R.id.done_btn})
    void onClick(View view) {
        if (view.getId() == R.id.send_email_btn) {
            if (isOnline(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMailStruct();
                }
            })) {
                Toast.makeText(this, "Successfully sending email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to network", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.print_struct_btn) {

        } else if (view.getId() == R.id.close_btn || view.getId() == R.id.done_btn) {
            finish();
        }
    }

    String accountName = "";

    private void sendMailStruct() {
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                new SendEmailTLS().sendMail();
//            }
//        });

//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        emailIntent.setType("message/rfc822");
//        emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//        emailIntent.setData(Uri.parse("mailto:nico.exiglosi@gmail.com"));
//        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"nicokoibur@gmail.com"});
//        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SUBJECT TEXT");
//        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "body text");
//        startActivity(emailIntent);

//        ShareCompat.IntentBuilder.from(this)
//                .setType("message/rfc822")
//
//                .addEmailTo("nico.exiglosi@gmail.com")
//                .setSubject("Test Email Struct")
//                .setText("This is email form android app")
//                //.setHtmlText(body) //If you are using HTML in your body text
//                .setChooserTitle("Send Email")
//                .startChooser();

//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setType("text/html");
//        intent.setData(Uri.parse("mailto:nico.exiglosi@gmail.com" ));
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
//
//        String content = "<html><body><center><H1>This is the image</H1><br>"+
//                "<H4>From nico</H2></center></body></html>";
//
//        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content));
//
//        startActivity(Intent.createChooser(intent, "Send Email"));

        Intent intent=new Intent(Intent.ACTION_SEND);
        String[] recipients={"nico.exiglosi@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.setPackage("com.google.android.gm");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
        intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_TEXT,"" );

        startActivityForResult(Intent.createChooser(intent, "Send mail"),101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}

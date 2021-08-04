package com.app.estockcard.view.admin.barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    private final String TAG = ScannerActivity.class.getSimpleName();

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
        Intent data = new Intent();
        data.putExtra("barcodeDigits",rawResult.getText());
        data.putExtra("barcodeFormat",rawResult.getBarcodeFormat().toString());
        setResult(Activity.RESULT_OK,data);
        finish();
    }
}

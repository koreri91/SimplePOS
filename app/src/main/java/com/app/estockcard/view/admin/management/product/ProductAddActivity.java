package com.app.estockcard.view.admin.management.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.app.estockcard.R;
import com.app.estockcard.controller.AppConfig;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Product;
import com.app.estockcard.view.admin.barcode.ScannerActivity;
import com.app.estockcard.view.admin.management.category.CategoryActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductAddActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_item)
    AppCompatImageView photo;

    @BindView(R.id.barcode_field)
    AppCompatEditText barcodeField;

    @BindView(R.id.name_field)
    AppCompatEditText nameField;

    @BindView(R.id.category_product_content)
    AppCompatTextView categoryContent;

    @BindView(R.id.quantity_price_content)
    AppCompatTextView stockPriceContent;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.item_save_btn)
    AppCompatButton saveBtn;

    private int idUpdate = -1;

    private int availableStock = -1;
    private int minimumStock = -1;
    private int sellingPrice = -1;
    private int purchasePrice = -1;

    private File currPhotoFile;
    private int categoryId = -1;

    private boolean doInsert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_add_product);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        toolbar.setTitle(intent.getIntExtra("idUpdate", -1) > 0 ? R.string.appstr_edit_product : R.string.appstr_add_product);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> {
            setResult( doInsert ? Activity.RESULT_OK  : Activity.RESULT_CANCELED);
            finish();
        });

        setDrawablePreLollipop(barcodeField, ContextCompat.getDrawable(this, R.drawable.ic_menu_camera), null, null, null);


        idUpdate = intent.getIntExtra("idUpdate", -1);
        if (idUpdate > -1) {
            byte[] streamPhoto = intent.getByteArrayExtra("photo");
            if (streamPhoto != null) {
                photo.setImageBitmap(BitmapFactory.decodeByteArray(streamPhoto, 0, streamPhoto.length));
            }
            nameField.setText(intent.getStringExtra("name"));
            barcodeField.setText(intent.getStringExtra("barcode"));

            categoryId = intent.getIntExtra("categoryId", -1);

            purchasePrice = intent.getIntExtra("purchasePrice", 0);
            sellingPrice = intent.getIntExtra("sellingPrice", 0);

            availableStock = intent.getIntExtra("availableStock", 0);
            minimumStock = intent.getIntExtra("minimumStock", 0);

            Product category = dbManager.getProduct(categoryId, Product.CategoryType);
            if (category != null) {
                categoryContent.setText(category.getName());
            }
            categoryId = -1;

            stockPriceContent.setText(availableStock + " pcs, Rp. " + sellingPrice);
        }
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSaveBtn();
            }
        });
    }



    @OnClick({R.id.item_save_btn, R.id.image_upload_btn, R.id.barcode_btn, R.id.layout_product_category, R.id.layout_stock_price})
    void onClick(View view) {
        if (view.getId() == R.id.item_save_btn) {
            Product goods = new Product();
            goods.setPhoto(getIntent().getByteArrayExtra("photo"));
            if (currPhotoFile != null) {
                ByteArrayOutputStream streamPhoto = new ByteArrayOutputStream();
                BitmapFactory.decodeFile(currPhotoFile.getAbsolutePath()).compress(Bitmap.CompressFormat.PNG, 100, streamPhoto);
                goods.setPhoto(streamPhoto.toByteArray());
            }
            goods.setName(Objects.requireNonNull(nameField.getText()).toString());
            goods.setDateCreated(new SimpleDateFormat().format(new Date()));
            goods.setBarcode(Objects.requireNonNull(barcodeField.getText()).toString());
            goods.setPurchasePrice(purchasePrice);
            goods.setSellingPrice(sellingPrice);
            goods.setAvailableStock(availableStock);
            goods.setMinimumStock(minimumStock);
            goods.setCategoryId(categoryId);
            goods.setType(Product.ProductType);

            if (idUpdate > -1) {
                goods.setId(idUpdate);
                dbManager.updateProduct(goods, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        clearFields();
                        enableSaveBtn();
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
            } else {
                dbManager.insertProduct(goods, new OnDBResultListener() {
                    @Override
                    public void onSuccess(String success) {
                        clearFields();
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
            }
        } else if (view.getId() == R.id.image_upload_btn) {
            pickFromGallery();
        } else if (view.getId() == R.id.barcode_btn) {
            startActivityForResult(new Intent(this, ScannerActivity.class), AppConfig.SCAN_BARCODE);
        } else if (view.getId() == R.id.layout_product_category) {
            Intent selectCategoryIntent = new Intent(this, CategoryActivity.class);
            selectCategoryIntent.putExtra("formType", CategoryActivity.SELECT_TYPE);
            startActivityForResult(selectCategoryIntent, AppConfig.SELECT_CATEGORY);
        } else if (view.getId() == R.id.layout_stock_price) {
            startActivityForResult(new Intent(this, ProductAddStockPrice.class), AppConfig.SET_STOCK_PRICE);
        }
    }

    private void enableSaveBtn() {
        if (Objects.requireNonNull(nameField.getText()).length() > 0 && categoryId > -1 && sellingPrice > 0) {
            saveBtn.setEnabled(true);
        } else {
            saveBtn.setEnabled(false);
        }
    }

    private void clearFields() {
        barcodeField.setText("");
        nameField.setText("");
        currPhotoFile =null;
        photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_camera));
        categoryId = -1;
        categoryContent.setText("Kategori belum dipilih");
        sellingPrice = -1;
        purchasePrice = -1;
        availableStock = 0;
        minimumStock = 0;
        stockPriceContent.setText("Belum ada stok, -");
        doInsert = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode ==AppConfig.PICK_IMAGE && resultCode == RESULT_OK) {
            currPhotoFile = new File(generatePath(data.getData(), this));
            photo.setImageBitmap(BitmapFactory.decodeFile(currPhotoFile.getAbsolutePath()));
        } else if (requestCode == AppConfig.SCAN_BARCODE && resultCode == RESULT_OK) {
            barcodeField.setText(data.getStringExtra("barcodeDigits"));
        } else if (requestCode == AppConfig.SET_STOCK_PRICE && resultCode == RESULT_OK) {
            availableStock = data.getIntExtra("availableStock", -1);
            minimumStock = data.getIntExtra("minimumStock", -1);
            sellingPrice = data.getIntExtra("sellingPrice", -1);
            purchasePrice = data.getIntExtra("purchasePrice", -1);

            String displayStockPrice = availableStock > 0 ? availableStock + " pcs " : "Tidak ada stok ";
            displayStockPrice += sellingPrice > 0 ? ", Rp " + sellingPrice : ", ";
            stockPriceContent.setText(displayStockPrice);
            enableSaveBtn();
        } else if (requestCode == AppConfig.SELECT_CATEGORY && resultCode == RESULT_OK) {
            this.categoryId = data.getIntExtra("categoryId", -1);
            categoryContent.setText(data.getStringExtra("categoryName"));
            enableSaveBtn();
        }
    }


    private void pickFromGallery() {
        Intent intent;
        String[] mimeTypes = {"image/*"};

        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else {
            StringBuilder mimeTypesStr = new StringBuilder();
            for (String mimeType : mimeTypes) {
                mimeTypesStr.append(mimeType).append("|");
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "Select Image"), AppConfig.PICK_IMAGE);
    }


    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }


    private String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String id = DocumentsContract.getDocumentId(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                File file = new File(getCacheDir().getAbsolutePath() + "/" + id);
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
            int columnIndex = Objects.requireNonNull(cursor).getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath;
    }

    private final String TAG = ProductAddActivity.class.getSimpleName();

    private File getFile(Context ctx, String fileName, Bitmap bitmap) {
        File filesDir = ctx.getFilesDir();
        File file = new File(filesDir, fileName + ".jpg");
        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            return file;
        } catch (IOException ex) {
            return null;
        }
    }

    void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

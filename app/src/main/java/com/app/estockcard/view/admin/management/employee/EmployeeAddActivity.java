package com.app.estockcard.view.admin.management.employee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.controller.OnDBResultListener;
import com.app.estockcard.model.Employee;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeAddActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_employee)
    AppCompatImageView photo;

    @BindView(R.id.image_upload_btn)
    AppCompatButton imageUploadBtn;

    @BindView(R.id.fullname_field)
    AppCompatEditText fullnameField;

    @BindView(R.id.residence_field)
    AppCompatEditText residenceField;

    @BindView(R.id.place_birth_field)
    AppCompatEditText placeBirthField;

    @BindView(R.id.date_birth_field)
    AppCompatEditText dateBirthField;

    @BindView(R.id.gender_field)
    RadioGroup genderField;


    @BindView(R.id.address_field)
    AppCompatEditText addressField;

    @BindView(R.id.cellular_number_field)
    AppCompatEditText cellularNumber;

    @BindView(R.id.whatsapp_number_field)
    AppCompatEditText whatsappNumber;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_management_add_employee);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.appstr_add_employee);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_navigation_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        dbManager = new DBManager();

        idUpdate = getIntent().getIntExtra("idUpdate", -1);
        if (idUpdate > -1) {
            ((AppCompatButton) findViewById(R.id.add_employee_btn)).setText("Update");

            byte[] streamPhoto = getIntent().getByteArrayExtra("photo");
            if (streamPhoto != null) {
                photo.setImageBitmap(BitmapFactory.decodeByteArray(streamPhoto, 0, streamPhoto.length));
            }
            fullnameField.setText(getIntent().getStringExtra("fullname"));
            residenceField.setText(getIntent().getStringExtra("residence"));
            placeBirthField.setText(getIntent().getStringExtra("placeBirth"));
            dateBirthField.setText(getIntent().getStringExtra("dateBirth"));
            AppCompatRadioButton genderRbtnSelect = getIntent().getStringExtra("gender").equalsIgnoreCase("F") ?
                    findViewById(R.id.female_field) : findViewById(R.id.male_field);
            genderRbtnSelect.setChecked(true);
            addressField.setText(getIntent().getStringExtra("address"));
            cellularNumber.setText(getIntent().getStringExtra("cellularPhoneNumber"));
            whatsappNumber.setText(getIntent().getStringExtra("whatsAppNumber"));
        }
    }

    private int idUpdate = -1;

    @OnClick({R.id.add_employee_btn, R.id.date_birth_field, R.id.image_upload_btn})
    void onClick(View view) {
        if (view.getId() == R.id.add_employee_btn) {

            boolean fieldEmpty = false;
            if (Objects.requireNonNull(addressField.getText()).toString().length() == 0) {
                addressField.setError("Harus diisi");
                addressField.requestFocus();
                fieldEmpty = true;
            }
            if (Objects.requireNonNull(dateBirthField.getText()).toString().length() == 0) {
                dateBirthField.setError("Harus diisi");
                dateBirthField.requestFocus();
                fieldEmpty = true;
            }
            if (Objects.requireNonNull(placeBirthField.getText()).toString().length() == 0) {
                placeBirthField.setError("Harus diisi");
                placeBirthField.requestFocus();
                fieldEmpty = true;
            }
            if (Objects.requireNonNull(residenceField.getText()).toString().length() == 0) {
                residenceField.setError("Harus diisi");
                residenceField.requestFocus();
                fieldEmpty = true;
            }
            if (Objects.requireNonNull(fullnameField.getText()).toString().length() == 0) {
                fullnameField.setError("Harus diisi");
                fullnameField.requestFocus();
                fieldEmpty = true;
            }

            if (!fieldEmpty) {
                Employee employee = new Employee();
                employee.setPhoto(getIntent().getByteArrayExtra("photo"));
                if (currPhotoFile != null) {
                    ByteArrayOutputStream streamPhoto = new ByteArrayOutputStream();
                    BitmapFactory.decodeFile(currPhotoFile.getAbsolutePath()).compress(Bitmap.CompressFormat.PNG, 100, streamPhoto);
                    employee.setPhoto(streamPhoto.toByteArray());
                }
                employee.setName(fullnameField.getText().toString());
                employee.setResidenceNumber(residenceField.getText().toString());
                employee.setPlaceBirthday(placeBirthField.getText().toString());
                employee.setDateBirthday(dateBirthField.getText().toString());

                employee.setGender((genderField.getCheckedRadioButtonId() == R.id.female_field) ? "F" : "M");
                employee.setAddress(addressField.getText().toString());
                employee.setCellularNumber(Objects.requireNonNull(cellularNumber.getText()).toString());
                employee.setWhatsAppNumber(Objects.requireNonNull(whatsappNumber.getText()).toString());

                if (idUpdate > -1) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "", Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", view12 -> snackbar.dismiss());
                    snackbar.setActionTextColor(Color.YELLOW);
                    employee.setId(idUpdate);

                    dbManager.updateEmployee(employee, new OnDBResultListener() {
                        @Override
                        public void onSuccess(String success) {
                            Log.v(EmployeeAddActivity.class.getSimpleName(), "Status Insert : " + success);
                            for (Employee e : new DBManager().getEmployees(Employee.EmployeeType)) {
                                Log.v(EmployeeAddActivity.class.getSimpleName(), "Name : " + e.getName() + " , Cellular Number : " + employee.getCellularNumber());
                            }
                            clearFields();
                            snackbar.setText("Data telah diupdate");
                            snackbar.show();
                        }

                        @Override
                        public void onError(String error) {
                            Log.v(EmployeeAddActivity.class.getSimpleName(), "Error Insert : " + error);
                        }
                    });
                } else {
                    boolean errorNameInput = false, errorResidenceNumberInput = false;
                    for (Employee item : dbManager.getEmployees(Employee.EmployeeType)) {
                        if (item.getName().equalsIgnoreCase(fullnameField.getText().toString())) {
                            errorNameInput = true;
                        }
                        if (item.getResidenceNumber().equalsIgnoreCase(residenceField.getText().toString())) {
                            errorResidenceNumberInput = true;
                        }
                    }
                    String message = "";
                    if (errorNameInput && errorResidenceNumberInput) {
                        message = "Karyawan dengan nama dan nomor induk ini sudah diinput";
                    } else if (errorNameInput) {
                        message = "Karyawan dengan nama ini sudah diinput";
                    } else if (errorResidenceNumberInput) {
                        message = "Karyawan dengan nomor induk ini sudah diinput";
                    }

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", view1 -> snackbar.dismiss());
                    snackbar.setActionTextColor(Color.YELLOW);

                    if (errorNameInput || errorResidenceNumberInput) {
                        snackbar.show();
                    } else {
                        dbManager.insertEmployee(employee, new OnDBResultListener() {
                            @Override
                            public void onSuccess(String success) {
                                Log.v(EmployeeAddActivity.class.getSimpleName(), "Status Insert : " + success);
                                for (Employee e : new DBManager().getEmployees(Employee.EmployeeType)) {
                                    Log.v(EmployeeAddActivity.class.getSimpleName(), "Name : " + e.getName() + " , Cellular Number : " + employee.getCellularNumber());
                                }
                                clearFields();
                                snackbar.setText("Data telah disimpan");
                                snackbar.show();
                            }
                            @Override
                            public void onError(String error) {
                                Log.v(EmployeeAddActivity.class.getSimpleName(), "Error Insert : " + error);
                            }
                        });
                    }
                }
            }
        } else if (view.getId() == R.id.date_birth_field) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this, myDateListener, year - 17, month, day).show();
        } else if (view.getId() == R.id.image_upload_btn) {
            pickFromGallery();
        }
    }

    private final DatePickerDialog.OnDateSetListener myDateListener = (datePicker, year, month, day) -> showDate(year, month + 1, day);

    private void showDate(int year, int month, int day) {
        dateBirthField.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void clearFields() {
        fullnameField.setText("");
        residenceField.setText("");
        placeBirthField.setText("");
        dateBirthField.setText("");
        genderField.clearCheck();
        addressField.setText("");
        cellularNumber.setText("");
        whatsappNumber.setText("");
        photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sales));
    }

    private File currPhotoFile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            currPhotoFile = new File(generatePath(data.getData(), this));

            photo.setImageBitmap(BitmapFactory.decodeFile(currPhotoFile.getAbsolutePath()));
        }
    }

    private static final int PICK_IMAGE = 101;

    private void pickFromGallery() {
        Intent intent;
        String[] mimeTypes = {"image/*"};

        intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes[0]);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else {
            StringBuilder mimeTypesStr = new StringBuilder();
            for (String mimeType : mimeTypes) {
                mimeTypesStr.append(mimeType).append("|");
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
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
                File file = new File(getCacheDir().getAbsolutePath()+"/"+id);
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();
            }catch (FileNotFoundException ex){
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

    private final String TAG = EmployeeAddActivity.class.getSimpleName();

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
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }


}

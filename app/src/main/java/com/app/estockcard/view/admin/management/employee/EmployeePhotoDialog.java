package com.app.estockcard.view.admin.management.employee;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.app.estockcard.R;
import com.app.estockcard.model.Employee;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeePhotoDialog extends Dialog {

    public EmployeePhotoDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
    }
    private Employee employee;
    public void setData(Employee employee){
        this.employee = employee;
    }

    @BindView(R.id.title_dialog)
    AppCompatTextView title;
    @BindView(R.id.dialog_photo)
    AppCompatImageView photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_custom_dialog_photo);
        ButterKnife.bind(this);
        if (employee.getPhoto() != null) {
            byte []streamPhoto =employee.getPhoto();
            photo.setImageBitmap(BitmapFactory.decodeByteArray(streamPhoto,0,streamPhoto.length));
        }
        title.setText(employee.getName());

    }

}

package com.app.estockcard.view.admin.management.employee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.model.Employee;
import com.app.estockcard.view.admin.InfoHolder;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final Context context;

    public EmployeeAdapterRV(Context ctx){
        this.context = ctx;
    }

    class EmployeeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_parent_item_employee_rv)
        RelativeLayout parentLayout;

        @BindView(R.id.img_employee)
        AppCompatImageView photo;

        @BindView(R.id.fullname_field)
        AppCompatTextView fullname;

        @BindView(R.id.residence_field)
        AppCompatTextView residence;
        @BindView(R.id.place_datebirth_field)
        AppCompatTextView placeDateBirth;
        @BindView(R.id.gender_field)
        AppCompatTextView gender;

        @BindView(R.id.address_field)
        AppCompatTextView address;

        @BindView(R.id.cellular_number_field)
        AppCompatTextView cellularNumber;
        @BindView(R.id.whatsapp_number_field)
        AppCompatTextView whatsappNumber;

        @BindView(R.id.img_menu)AppCompatImageView menu;

        EmployeeHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface OnEmployeeRVAdapterClick{

        void onMenuClick(View view,int position);
    }

    private List<Employee> employees;
    private OnEmployeeRVAdapterClick mListener;

    public void setListener(OnEmployeeRVAdapterClick listner){
        this.mListener = listner;
    }


    public void setData(List<Employee> employees){
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void removeItem(int position) {
        employees.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Employee item, int position) {
        employees.add(position, item);
        notifyItemInserted(position);
    }

    private boolean selectionMode;
    public void setSelectionMode(boolean selectionMode){
        this.selectionMode = selectionMode;
    }

    public static final int TYPE_HOLDER_NO_DATA =1;
    public static final int TYPE_HOLDER_DISPLAY =2;

    @Override
    public int getItemCount() {
        return (employees == null) ? 0 : employees.size();
    }

    @Override
    public int getItemViewType(int position) {
        return employees.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (viewType == TYPE_HOLDER_NO_DATA )
                ?  new InfoHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_no_data,parent,false))
                :  new EmployeeHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_employee,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder , final int position) {
        Employee employee = employees.get(position);

        String content = employee.getResidenceNumber()+", "+employee.getName()+" , "+employee.getPlaceBirthday()+", "+employee.getDateBirthday()+" , "+employee.getGender()+" , "+employee.getAddress();

        if (getItemViewType(position) == TYPE_HOLDER_NO_DATA) {
            InfoHolder holder = (InfoHolder) viewHolder;
            holder.icon.setImageResource(employee.getDrawableID());
            holder.titleCenter.setText(employee.getName());
        }else if (getItemViewType(position) == TYPE_HOLDER_DISPLAY) {
            EmployeeHolder holder = (EmployeeHolder)viewHolder;
            if (selectionMode) {
                holder.parentLayout.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onMenuClick(holder.parentLayout,position);
                    }
                });
                holder.menu.setVisibility(View.INVISIBLE);
            }else {
                holder.menu.setOnClickListener(view -> {
                    if (mListener != null) {
                        mListener.onMenuClick(holder.menu,holder.getAdapterPosition());
                    }
                });
            }
            if (employee.getPhoto() != null) {
                Glide.with(context).asBitmap().load(employee.getPhoto()).into(holder.photo);
            }
            holder.residence.setText(employee.getResidenceNumber());
            holder.fullname.setText(employee.getName());
            holder.placeDateBirth.setText(employee.getPlaceBirthday()+", "+employee.getDateBirthday());
            holder.gender.setText(employee.getGender().equalsIgnoreCase("F") ? "Female" : "Male");
            holder.address.setText(employee.getAddress());
            holder.cellularNumber.setText(employee.getCellularNumber());
            holder.whatsappNumber.setText(employee.getWhatsAppNumber());
            ( (BaseActivity)   context ).setDrawablePreLollipop(holder.cellularNumber, ContextCompat.getDrawable(context,R.drawable.ic_cellular),null,null,null);
            ( (BaseActivity)   context ).setDrawablePreLollipop(holder.whatsappNumber, ContextCompat.getDrawable(context,R.drawable.ic_whatsapp),null,null,null);
            holder.photo.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onMenuClick(holder.photo,holder.getAdapterPosition());
                }
            });

        }

    }
}

package com.app.estockcard.view.admin.management.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.model.Employee;
import com.app.estockcard.view.admin.InfoHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private final Context context;
    private List<Employee> customers, customersFiltered;
    private OnCustomerRVAdapterClick mListener;

    public CustomerAdapterRV(Context ctx) {
        this.context = ctx;
        this.customers = new ArrayList<>();
        this.customersFiltered = new ArrayList<>();
    }

    class CustomerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_parent_item_customer_rv)
        RelativeLayout parentLayout;


        @BindView(R.id.delete_btn)
        AppCompatImageView deleteBtn;

        @BindView(R.id.fullname_field)
        AppCompatTextView fullname;

        @BindView(R.id.phone_number_field)
        AppCompatTextView phoneNumber;

        CustomerHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnCustomerRVAdapterClick {

        void onMenuClick(View view, int position);
    }


    public void setListener(OnCustomerRVAdapterClick listner) {
        this.mListener = listner;
    }


    public void setData(List<Employee> customers) {
        this.customersFiltered = customers;
        this.customers = customers;
    }

    public List<Employee> getCustomers() {
        return customersFiltered;
    }

    public void removeItem(int position) {
        customersFiltered.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Employee item, int position) {
        customersFiltered.add(position, item);
        notifyItemInserted(position);
    }

    private boolean selectionMode;

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
    }

    public static final int TYPE_HOLDER_NO_DATA = 1;
    public static final int TYPE_HOLDER_DISPLAY = 2;

    @Override
    public int getItemCount() {
        return (customersFiltered == null) ? 0 : customersFiltered.size();
    }

    @Override
    public int getItemViewType(int position) {
        return customersFiltered.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (viewType == TYPE_HOLDER_NO_DATA)
                ? new InfoHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_no_data, parent, false))
                : new CustomerHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_customer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Employee customer = customersFiltered.get(position);

        String content = customer.getResidenceNumber() + ", " + customer.getName() + " , " + customer.getPlaceBirthday() + ", " + customer.getDateBirthday() + " , " + customer.getGender() + " , " + customer.getAddress();

        if (getItemViewType(position) == TYPE_HOLDER_NO_DATA) {
            InfoHolder holder = (InfoHolder) viewHolder;
            holder.icon.setImageResource(customer.getDrawableID());
            holder.titleCenter.setText(customer.getName());
        } else if (getItemViewType(position) == TYPE_HOLDER_DISPLAY) {
            CustomerHolder holder = (CustomerHolder) viewHolder;
            if (selectionMode) {
                holder.parentLayout.setOnClickListener(v -> {
                    if (mListener != null) {
                        mListener.onMenuClick(holder.parentLayout, position);
                    }
                });
                holder.deleteBtn.setVisibility(View.INVISIBLE);
            } else {
                holder.deleteBtn.setOnClickListener(view -> {
                    if (mListener != null) {
                        mListener.onMenuClick(holder.deleteBtn, holder.getAdapterPosition());
                    }
                });
            }
            holder.phoneNumber.setClickable(false);
            holder.fullname.setClickable(false);
            holder.parentLayout.setClickable(true);

            holder.phoneNumber.setText(customer.getCellularNumber());
            holder.fullname.setText(customer.getName());
            holder.parentLayout.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onMenuClick(holder.parentLayout, holder.getAdapterPosition());
                }
            });

        }
    }

    private final String TAG = CustomerAdapterRV.class.getSimpleName();


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    customersFiltered = customers;
                } else {
                    List<Employee> filteredList = new ArrayList<>();
                    for (Employee item : customers) {
                        if (item.getName().equalsIgnoreCase(charString)) {
                             filteredList.add(item);
                        }
                    }
                    customersFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = customersFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                customersFiltered = (ArrayList<Employee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}

package com.app.estockcard.view.admin.shift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;
import com.app.estockcard.controller.DBManager;
import com.app.estockcard.model.Shift;
import com.app.estockcard.view.admin.InfoHolder;

import java.util.ArrayList;
import java.util.List;

public class ShiftAdapterRV extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    public ShiftAdapterRV(Context ctx) {
        this.context = ctx;
        this.shifter = new ArrayList<>();
    }

    public interface OnShiftRVAdapterClick {
        void onMenuClick(View view, int position);
    }

    public static final int TYPE_HOLDER_NO_DATA =1;
    public static final int TYPE_HOLDER_INPUT = 2;
    public static final int TYPE_HOLDER_DISPLAY =3;

    private final String TAG = ShiftAdapterRV.class.getSimpleName();
    @Override
    public int getItemViewType(int position) {
        return shifter.get(position).getHolderType();
    }

    private List<Shift> shifter;
    private OnShiftRVAdapterClick mListener;

    public void setListener(OnShiftRVAdapterClick listner) {
        this.mListener = listner;
    }

    public void setData(List<Shift> shifter) {
        this.shifter = shifter;
    }

    public List<Shift> getShifter() {
        return shifter;
    }

    public void setItem(Shift item, int position) {
        shifter.set(position, item);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        shifter.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Shift item, int position) {
        shifter.add(position, item);
        notifyItemInserted(position);
    }

    public void add(Shift item, RecyclerView parentRV) {
        shifter.add(item);
        notifyItemInserted(getShifter().size() - 1);
        parentRV.scrollToPosition(getShifter().size() - 1);
    }

    @Override
    public int getItemCount() {
        return shifter.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HOLDER_NO_DATA) {
            viewHolder =  new InfoHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_no_data, parent, false));
        } else if (viewType == TYPE_HOLDER_INPUT)  {
            viewHolder = new ShiftInputHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_shift_input, parent, false));
        }else if (viewType == TYPE_HOLDER_DISPLAY) {
            viewHolder = new ShiftDisplayHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_shift_display, parent, false));
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Shift shift = shifter.get(position);


        if (getItemViewType(position) == TYPE_HOLDER_NO_DATA) {
            InfoHolder holder = (InfoHolder) viewHolder;
            holder.icon.setImageResource(shift.getDrawableID());
            holder.titleCenter.setText(shift.getName());
        } else if (getItemViewType(position) == TYPE_HOLDER_DISPLAY) {
            ShiftDisplayHolder holder = (ShiftDisplayHolder) viewHolder;
            StringBuilder info = new StringBuilder();
            StringBuilder employee= new StringBuilder();
            String dateWork="";
            for (Shift item  : shift.getShifter()) {
                dateWork = item.getDateWork();
               employee.append(new DBManager().getEmployee(item.getEmployeeID()).getName()).append(" \n");
                info.append("Employee ID : ").append(item.getEmployeeID()).append(" , Date Work ").append(item.getDateWork()).append(" , startHour : ").append(item.getStartHour()).append(" , endHour : ").append(item.getEndHour()).append("\n");
            }
            holder.titleDisplayDate.setText(dateWork);
            holder.titleNumber.setText(""+(position+1));
            holder.titleName.setText(employee.toString());
            holder.titleTime.setText(info.toString());

        } else if (getItemViewType(position) == TYPE_HOLDER_INPUT) {
            ShiftInputHolder holder = (ShiftInputHolder) viewHolder;
            holder.title.setText("" + (position + 1));
            holder.employee.setText(shift.getName());
           holder.time.setText(shift.getStartHour() + " - " + shift.getEndHour());

            ( (BaseActivity)   context ).setDrawablePreLollipop(holder.employee, ContextCompat.getDrawable(context, R.drawable.ic_person_add_black), null, null, null);
            ( (BaseActivity)   context ).setDrawablePreLollipop(holder.time, ContextCompat.getDrawable(context, R.drawable.ic_access_time_black), null, null, null);

            holder.layout_employee.setOnClickListener(v -> {
                if (mListener != null) {
                   mListener.onMenuClick(holder.layout_employee, position);
                }
            });

            holder.time.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onMenuClick(holder.time, position);
                }
            });

        }


    }
}

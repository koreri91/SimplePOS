package com.app.estockcard.view.admin.settings;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseItemAdapter extends RecyclerView.Adapter<ChooseItemAdapter.ItemHolder> {

    private Context context;

    public ChooseItemAdapter(Context context){
        this.context = context;
    }
    String []data;
    public void setData(String []items){
        this.data = items;
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.parent)
        LinearLayout parent;

        @BindView(R.id.display_name)
        AppCompatRadioButton displayName;
        public ItemHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    interface OnChooseItemAdapter{
        void onClick(String name);
    }
    private OnChooseItemAdapter mListener;
    public void setListener(OnChooseItemAdapter listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_item_chooser,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder,final int position) {
        holder.displayName.setText(data[position]);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(data[position]);
                }
            }
        });
    }
}

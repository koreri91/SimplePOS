package com.app.estockcard.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EKardDialogList extends Dialog {

    private final Context context;
    private final OnDialogListListener parentListener;
    private final String[] items;

    public EKardDialogList(Context context, String[] items, OnDialogListListener listener) {
        super(context);
        this.context = context;
        this.parentListener = listener;
        this.items = items;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    private final String TAG = EKardDialogList.class.getSimpleName();

    @BindView(R.id.list_item)
    RecyclerView listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_list);
        ButterKnife.bind(this);

        DialogListAdapter dialogListAdapter = new DialogListAdapter(context, items, (position, name) -> {
            if (parentListener != null) {
                parentListener.onResultItemSelected(position, name);
            }
            dismiss();
        });
        listItem.setAdapter(dialogListAdapter);
        listItem.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        listItem.addItemDecoration(new DividerItemDecoration(context,RecyclerView.VERTICAL));

    }

    @Override
    protected void onStart() {
        super.onStart();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
    }

    public interface OnDialogListListener {
        void onResultItemSelected(int position,String name);
    }

    class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.DialogItemHolder> {
        private final Context context;
        private final String[] items;
        private final OnDialogListListener mListener;

        public DialogListAdapter(Context context, String[] items, OnDialogListListener listener) {
            this.context = context;
            this.items = items;
            this.mListener = listener;
        }


        class DialogItemHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.parent_dialog_list_item)
            LinearLayout parent;

            @BindView(R.id.title)
            AppCompatTextView title;

            DialogItemHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        @Override
        public void onBindViewHolder(@NonNull DialogItemHolder holder, final int position) {
            holder.title.setText(items[position]);
            holder.parent.setOnClickListener(v -> {
                if (mListener != null) {

                    mListener.onResultItemSelected(position,items[position]);
                }
            });
        }

        @NonNull
        @Override
        public DialogItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DialogItemHolder(LayoutInflater.from(context).inflate(R.layout.admin_rv_list_item_dialog, parent, false));
        }

    }


}

package com.app.estockcard.view.admin.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.estockcard.R;
import com.app.estockcard.model.Product;
import com.app.estockcard.view.admin.order.BillItemsAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseItemDialog extends Dialog {

    private String []items;
    private String title;

    public ChooseItemDialog(Context ctx,String [] items,String title){
        super(ctx);
        this.items = items;
        this.title = title;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @BindView(R.id.dialog_title)
    AppCompatTextView dialogTitle;

    @BindView(R.id.data_rv)
    RecyclerView dataRV;

    private ChooseItemAdapter chooseItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_item);
        ButterKnife.bind(this);

        dialogTitle.setText(title);

        chooseItemAdapter = new ChooseItemAdapter(getContext());
        chooseItemAdapter.setListener(new ChooseItemAdapter.OnChooseItemAdapter() {
            @Override
            public void onClick(String name) {
                if (mListener != null) {
                    dismiss();
                    mListener.onClick(name);
                }
            }
        });
        chooseItemAdapter.setData(items);
        dataRV.setAdapter(chooseItemAdapter);
        dataRV.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }
    private ChooseItemAdapter.OnChooseItemAdapter mListener;
    public void setListener(ChooseItemAdapter.OnChooseItemAdapter listener){
        this.mListener = listener;
    }

    @OnClick(R.id.close_btn)
    void onClick(){
        dismiss();
    }


}

package com.app.estockcard.view.admin.shift;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.estockcard.R;
import com.app.estockcard.controller.BaseActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShiftFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private OnFragmentInteractionListener mListener;

    public ShiftFragment() {
    }

    public static ShiftFragment newInstance(String param1, String param2) {
        ShiftFragment fragment = new ShiftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @BindView(R.id.title_set_schedule)
    AppCompatTextView titleSetSchedule;

    @BindView(R.id.title_history_shift)
    AppCompatTextView titleHistotyShift;

    private BaseActivity parent;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_shift, container, false);
        parent = (BaseActivity)getActivity();
        ButterKnife.bind(this, view);


        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Drawable drawable = ContextCompat.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_arrow_right);
        parent.setDrawablePreLollipop(titleSetSchedule, null, null,drawable , null);
        parent.setDrawablePreLollipop(titleHistotyShift, null, null,drawable, null);
    }

    private String TAG = ShiftFragment.class.getSimpleName();

    @OnClick({R.id.layout_add_schedule, R.id.layout_view_history})
    void onClick(View view) {
        if (view.getId() == R.id.layout_add_schedule) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), ShiftAddScheduleActivity.class));
        } else if (view.getId() == R.id.layout_view_history) {
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), ShiftHistoryActivity.class));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onShiftFragmentInteraction();
    }
}

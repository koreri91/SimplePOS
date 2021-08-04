package com.app.estockcard.view.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.estockcard.R;

public class AdapterDisplayCover extends FragmentStatePagerAdapter {

    public AdapterDisplayCover(Context context,FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentStepOne();
        }else if (position == 1) {
            return new FragmentStepTwo();
        }else if (position == 2) return new FragmentStepThree();
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public static final class  FragmentStepOne extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
            return inflater.inflate(R.layout.admin_fragment_cover_step_one,container,false);
        }
    }

    public  static final  class  FragmentStepTwo extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
            return inflater.inflate(R.layout.admin_fragment_cover_step_two,container,false);
        }
    }

    public  static final  class  FragmentStepThree extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
            return inflater.inflate(R.layout.admin_fragment_cover_step_three,container,false);
        }
    }



}

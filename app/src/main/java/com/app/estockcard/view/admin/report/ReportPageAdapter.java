package com.app.estockcard.view.admin.report;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ReportPageAdapter extends FragmentStatePagerAdapter {

    private String[] pagesTitle;
    public ReportPageAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        pagesTitle= new String[]{"Laporan Keuangan","Laporan Penjualan"};
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment =null;
        if (position == 0 ) {
            fragment = new PageFinanceReportFragment();
        }else  if (position == 1 ) {
            fragment = new PageSalesReportFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pagesTitle[position];
    }

    @Override
    public int getCount() {
        return pagesTitle.length;
    }
}

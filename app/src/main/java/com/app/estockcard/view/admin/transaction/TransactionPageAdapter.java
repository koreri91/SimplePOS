package com.app.estockcard.view.admin.transaction;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.estockcard.R;

public class TransactionPageAdapter extends FragmentStatePagerAdapter {

    private String []pagesTitle= null;

    public TransactionPageAdapter(Context ctx, FragmentManager fragmentManager) {
        super(fragmentManager);
        pagesTitle = new String[]{
                ctx.getString(R.string.appstr_list_of_transactions),
                ctx.getString(R.string.appstr_credit_detail)};
    }

    @Override
    public int getCount() {
        return pagesTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pagesTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment =null;
        if (position == 0 ) {
            fragment = new PageAllListTransactionFragment();
        }else  if (position == 1 ) {
            fragment = new PageCreditSummaryFragment();
        }
        return fragment;
    }
}

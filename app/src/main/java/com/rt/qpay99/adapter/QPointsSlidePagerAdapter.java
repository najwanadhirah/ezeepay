package com.rt.qpay99.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rt.qpay99.fragment.BuyProductFragmentUI;
import com.rt.qpay99.fragment.QPointsPageFourFragment;
import com.rt.qpay99.fragment.QPointsPageOneFragment;
import com.rt.qpay99.fragment.QPointsPageThreeFragment;
import com.rt.qpay99.fragment.QPointsPageTwoFragment;

public class QPointsSlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 4;

    public QPointsSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new QPointsPageOneFragment();
        }

        if (position == 1) {
            return new QPointsPageTwoFragment();
        }

        if (position == 2) {
            return new QPointsPageThreeFragment();
        }

        if (position == 3) {
            return new QPointsPageFourFragment();
        }
        if (position == 4) {
            return new QPointsPageFourFragment();
        }

        return new BuyProductFragmentUI();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}

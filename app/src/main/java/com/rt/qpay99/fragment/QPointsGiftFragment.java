package com.rt.qpay99.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rt.qpay99.R;
import com.rt.qpay99.activity.ui.LuckyDrawPageUI;
import com.rt.qpay99.activity.ui.PointRedeemPageUI;
import com.rt.qpay99.activity.ui.ShakeAndWinUI;
import com.rt.qpay99.adapter.QPointsSlidePagerAdapter;

public class QPointsGiftFragment extends Fragment {


    public QPointsGiftFragment() {

    }

    LinearLayout llShakeWin,llLuckyDraw,llRedemption;
    private Context mContext;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qpoints_gift,
                container, false);
        mContext = this.getActivity();
        mPager = rootView.findViewById(R.id.pager);
        mPagerAdapter = new QPointsSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout =  rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mPager, true);

        llShakeWin = rootView.findViewById(R.id.llShakeWin);
        llLuckyDraw = rootView.findViewById(R.id.llLuckyDraw);
        llRedemption = rootView.findViewById(R.id.llRedemption);

        llShakeWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ShakeAndWinUI.class);
                startActivity(i);
            }
        });

        llLuckyDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, LuckyDrawPageUI.class);
                startActivity(i);
            }
        });

        llRedemption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, PointRedeemPageUI.class);
                startActivity(i);
            }
        });



        return rootView;
    }


}

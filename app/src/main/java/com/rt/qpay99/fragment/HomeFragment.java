package com.rt.qpay99.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.rt.qpay99.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HomeFragment extends Fragment {


    private String TAG = this.getClass().getName();
    private Context mContext;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container,
                false);

        ViewPager viewPager =  view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs =  view.findViewById(R.id.result_tabs);
        tabs.setTabTextColors(
                getResources().getColor(R.color.bootstrap_gray),
                getResources().getColor(R.color.solid_black));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(viewPager);


        mContext = this.getActivity();


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new TopupFragment_v2UI(), "TOPUP");
        adapter.addFragment(new PINFragment_v2UI(), "PIN");
        adapter.addFragment(new PayBillFragment_v2UI(), "BILL");
        adapter.addFragment(new DataFragment(), "DATA");
        adapter.addFragment(new TopupIntFragmentUI(), "INT");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        int lucky_number = getRandomNumber(0, 3);
//        String lastLogin = SharedPreferenceUtil.getDailyPromo();
//        long date = System.currentTimeMillis();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        String dateString = sdf.format(date);
//
//        DLog.e(TAG, "lastLogin " + lastLogin);
//        if (lastLogin.equalsIgnoreCase(dateString)) {
//            DLog.e(TAG, "NO PROMO POPUP");
//        } else {
//            SharedPreferenceUtil.editDailyPromo(dateString);
//            Intent i = new Intent();
//            if(lucky_number==1){
//                i = new Intent(mContext, ShakeAndWinUI.class);
//                startActivity(i);
//            }
//
//            if(lucky_number==2){
//                i = new Intent(mContext, PointRedeemPageUI.class);
//                startActivity(i);
//            }
//
//            if(lucky_number==3){
//                i = new Intent(mContext, LuckyDrawPageUI.class);
//                startActivity(i);
//            }
//
//
//        }
    }

    private int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }



}

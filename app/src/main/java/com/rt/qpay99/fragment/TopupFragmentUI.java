package com.rt.qpay99.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.TopupAdapter;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.listener.ProductGridviewOnItemClickListener;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class TopupFragmentUI extends Fragment {

    private String TAG = this.getClass().getName();
    private TopupAdapter topupAdapter, flexiAdapter;
    private GridView gdTopUp, gdFlexi;
    List<ProductInfo> gdTopUpInfo, gdFlexiInfo;

    ScrollTextView scrolltext;

    public TopupFragmentUI() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DLog.e(TAG, " onCreate ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DLog.e(TAG, " onCreateView " + Config.MOVING_TEXT);
        View rootView = inflater.inflate(R.layout.view_layout_gridview,
                container, false);


        scrolltext =  rootView.findViewById(R.id.scrolltext);
        scrolltext.setText(Config.MOVING_TEXT);
        //scrolltext.setTextColor(getResources().getColor(R.color.soild_dark_red));
        scrolltext.startScroll();



        gdTopUp = (GridView) rootView.findViewById(R.id.gdTopup);
        gdTopUpInfo = SharedPreferenceUtil.getPRODUCT_TOPUP_PREFERENCE();
        topupAdapter = new TopupAdapter(this.getActivity(), gdTopUpInfo);
        gdTopUp.setAdapter(topupAdapter);
        gdTopUp.setOnItemClickListener(new ProductGridviewOnItemClickListener(
                this.getActivity(), getFragmentManager()));



        return rootView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        DLog.e(TAG, " onResume");
        scrolltext.setText(Config.MOVING_TEXT);
    }

}

package com.rt.qpay99.fragment;

import android.content.Context;
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
import com.rt.qpay99.listener.PayBillGridviewOnItemClickListener;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class PayBillFragmentUI extends Fragment {

	private Context mContext;

	private String TAG = this.getClass().getName();
	private TopupAdapter payBillAdapter;
	private GridView gdPayBill;
	List<ProductInfo> gdPayBillInfo;
    ScrollTextView scrolltext;
	public PayBillFragmentUI() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.view_layout_gridview_paybill,
				container, false);

        scrolltext = (ScrollTextView) rootView.findViewById(R.id.scrolltext);
        scrolltext.setText(Config.MOVING_TEXT);
        //scrolltext.setTextColor(getResources().getColor(R.color.soild_dark_red));
        scrolltext.startScroll();


		gdPayBill = (GridView) rootView.findViewById(R.id.gdTopup);
		gdPayBillInfo = SharedPreferenceUtil.getPRODUCT_PAYBILL_PREFERENCE();
		payBillAdapter = new TopupAdapter(this.getActivity(), gdPayBillInfo);
		gdPayBill.setAdapter(payBillAdapter);
		gdPayBill
				.setOnItemClickListener(new PayBillGridviewOnItemClickListener(
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

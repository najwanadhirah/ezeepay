package com.rt.qpay99.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.rt.qpay99.util.DLog;

public class BluetootDeviceOnItemClickLister implements
		AdapterView.OnItemClickListener {

	private String TAG = this.getClass().getName();
	private Context mContext;
	private AlertDialog ad;

	public BluetootDeviceOnItemClickLister(Context mContext, AlertDialog ad) {
		this.mContext = mContext;
		this.ad = ad;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		DLog.e(TAG, "PricePinOnItemClickListener");
		ad.dismiss();
	}

}

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
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;

import java.util.List;

public class RegistrationFragmentUI extends Fragment {
	private String TAG = this.getClass().getName();
	private TopupAdapter SIMAdapter;
	private GridView gdSIM;
	List<ProductInfo> gdPinInfo;
    ScrollTextView scrolltext;

	public RegistrationFragmentUI() {
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
		View rootView = inflater.inflate(R.layout.view_layout_registration,
				container, false);

        scrolltext = (ScrollTextView) rootView.findViewById(R.id.scrolltext);
        scrolltext.setText(Config.MOVING_TEXT);
        scrolltext.startScroll();



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

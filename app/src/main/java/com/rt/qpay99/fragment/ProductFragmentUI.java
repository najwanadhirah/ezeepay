package com.rt.qpay99.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;

public class ProductFragmentUI extends Fragment {

	private String TAG = "ProductFragmentUI";

	public ProductFragmentUI() {
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
		// TODO Auto-generated method stub
		DLog.e(TAG, "ProductFragmentUI");
		View rootView = inflater.inflate(R.layout.fragment_product_detail,
				container, false);
		TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);
		dummyTextView.setText("ProductFragmentUI");
		return rootView;
	}

}

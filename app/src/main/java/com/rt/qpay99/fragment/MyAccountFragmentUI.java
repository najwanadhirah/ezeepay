package com.rt.qpay99.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.MyAccountGridViewImageAdapter;
import com.rt.qpay99.listener.MyAccountGridviewOnItemClickListener;


public class MyAccountFragmentUI extends Fragment {
	private String TAG = this.getClass().getName();
	private MyAccountGridViewImageAdapter adapter;
	private GridView gd;
	private AdView mAdView;

	public MyAccountFragmentUI() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.view_layout_gridview_paybill,
				container, false);

		MobileAds.initialize(getActivity().getApplicationContext(),
				"ca-app-pub-7069957738539410/8983539706");



		mAdView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		gd = (GridView) rootView.findViewById(R.id.gdTopup);

		adapter = new MyAccountGridViewImageAdapter(this.getActivity());
		gd.setAdapter(adapter);
		gd.setOnItemClickListener(new MyAccountGridviewOnItemClickListener(this
				.getActivity(), getFragmentManager()));



		return rootView;
	}

}

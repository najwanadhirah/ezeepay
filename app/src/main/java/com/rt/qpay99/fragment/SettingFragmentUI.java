package com.rt.qpay99.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rt.qpay99.R;
import com.rt.qpay99.activity.ui.LoginUI;
import com.rt.qpay99.adapter.SettingGridViewImageAdapter;
import com.rt.qpay99.listener.SettingGridviewOnItemClickListener;
import com.rt.qpay99.listener.SettingGridviewOnItemClickListener.LogoutListener;
import com.rt.qpay99.util.DLog;

public class SettingFragmentUI extends Fragment {

	private String TAG = this.getClass().getName();
	private SettingGridViewImageAdapter adapter;
	private GridView gd;
	private AdView mAdView;
	public SettingFragmentUI() {
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

		adapter = new SettingGridViewImageAdapter(this.getActivity());
		gd.setAdapter(adapter);
		gd.setOnItemClickListener(new SettingGridviewOnItemClickListener(this
				.getActivity(), getFragmentManager(), new logoutListener()));

		return rootView;
	}

	private class logoutListener implements LogoutListener {

		@Override
		public void logoutStart() {
			// TODO Auto-generated method stub
			DLog.e(TAG, "logoutStart ");
		}

		@Override
		public void logoutEnd() {
			// TODO Auto-generated method stub
			DLog.e(TAG, "logoutEnd ");
			Intent intent = new Intent(getActivity(), LoginUI.class);
			getActivity().startActivity(intent);
			getActivity().finish();
		}

	}
}

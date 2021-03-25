package com.rt.qpay99.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.TopupAdapter;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.List;


public class WhatsHotFragment extends Fragment {
	private String TAG = this.getClass().getName();
	private TopupAdapter mTopUpAdapter;
	private ListView lvProduct;
	List<ProductInfo> productList;
//	ScrollTextView scrolltext;

	com.beardedhen.androidbootstrap.BootstrapButton btnFBMessenger,btnTelegram,btnFBTutorial;



	private WebView wvWhatshot;

	private AdView mAdView;

	public WhatsHotFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_whatshot, container,
				false);

		MobileAds.initialize(getActivity().getApplicationContext(),
				"ca-app-pub-7069957738539410/8983539706");

		mAdView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

//		scrolltext = (ScrollTextView) rootView.findViewById(scrolltext);
//		scrolltext.setText(Config.MOVING_TEXT);
//		scrolltext.startScroll();
		DLog.e(TAG,"Config.MOVING_TEXT " + Config.MOVING_TEXT);
		

		getMessageInfo("WHATSHOT");

		wvWhatshot = (WebView) rootView.findViewById(R.id.wvWhatshot);
		btnTelegram=(com.beardedhen.androidbootstrap.BootstrapButton)rootView.findViewById(R.id.btnTelegram);
		btnTelegram.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/qpay99bot"));
				startActivity(intent);
			}
		});

		btnFBMessenger=(com.beardedhen.androidbootstrap.BootstrapButton)rootView.findViewById(R.id.btnFBMessenger);
		btnFBMessenger.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.messenger.com/t/qpay99messenger"));
				startActivity(i2);
			}
		});

		btnFBTutorial=(com.beardedhen.androidbootstrap.BootstrapButton)rootView.findViewById(R.id.btnFBTutorial);
		btnFBTutorial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/qpay99/posts/1441130132636260"));
				startActivity(i2);
			}
		});



		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DLog.e(TAG, " onResume");
//		scrolltext.setText(Config.MOVING_TEXT);
	}


	RTWS rtWS = new RTWS();
	private void getMessageInfo(final String sMessageType) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(Void... arg0) {
				return rtWS
						.GetMessageInfo(
								SharedPreferenceUtil.getsClientUserName(),
								SharedPreferenceUtil.getsClientPassword(),
								sMessageType);

			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				String mText = SharedPreferenceUtil.getMovingText();
				if (FunctionUtil.isSet(result)){
					if(!result.equalsIgnoreCase(mText)){
						SharedPreferenceUtil.editMovingText(result);
						mText=result;
					}
					wvWhatshot.getSettings().setJavaScriptEnabled(true);
					wvWhatshot.getSettings().setJavaScriptEnabled(true);
					wvWhatshot.getSettings().setDomStorageEnabled(true);
					DLog.e(TAG,"" + mText);
					mText = mText.replace("{MSG}","name=chew kim wei&mobileno=0126112184");
					wvWhatshot.loadDataWithBaseURL("", mText, "text/html", "UTF-8", "");
					//Config.MOVING_TEXT = mText;
				}


			}

		}.execute();
	}
}

package com.rt.qpay99.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.object.RTResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.List;

public class SignUpFragment extends Fragment {
	private String TAG = this.getClass().getName();


	List<ProductInfo> productList;
	ScrollTextView scrolltext;

	private AdView mAdView;
	private EditText edRegName,edRegMobileNo;
	private Button btnLogin;
	private TextView tvTutorial,tvNewAgentMobileWeb;

	public SignUpFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_sign_up, container,
				false);

		MobileAds.initialize(getActivity().getApplicationContext(),
				"ca-app-pub-7069957738539410/8983539706");

		mAdView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		scrolltext = (ScrollTextView) rootView.findViewById(R.id.scrolltext);
		scrolltext.setText(Config.MOVING_TEXT);
		scrolltext.startScroll();
		DLog.e(TAG, "Config.MOVING_TEXT " + Config.MOVING_TEXT);

		edRegName = (EditText)rootView.findViewById(R.id.edRegName);
		//edRegEmail = (EditText)rootView.findViewById(R.id.edRegEmail);
		edRegMobileNo = (EditText)rootView.findViewById(R.id.edRegMobileNo);

		tvTutorial = (TextView) rootView.findViewById(R.id.tvTutorial);
		tvTutorial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse("http://qpay99topup.blogspot.my"));
				startActivity(i2);
			}
		});

		tvNewAgentMobileWeb = (TextView) rootView.findViewById(R.id.tvNewAgentMobileWeb);
		tvNewAgentMobileWeb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse("http://onlinereload.dyndns.org:9999/agentmobile/login.aspx"));
				startActivity(i2);
			}
		});

		btnLogin =(Button)rootView.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signUpNow();
			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DLog.e(TAG, " onResume");
		scrolltext.setText(Config.MOVING_TEXT);
	}

	private String RegName,RegMobileNo;

	public void signUpNow(){

		if(edRegName.getText().length()>5){
			RegName  = edRegName.getText().toString();
		}else{
			transcationAlertDialog("Please enter a username between 6 and 30 characters").show();
			return;
		}

		if(edRegMobileNo.getText().length()>8){
			RegMobileNo = edRegMobileNo.getText().toString();
			if(RegMobileNo.substring(0,2).equalsIgnoreCase("01")){
				RegMobileNo = "6"+ RegMobileNo;
			}
		}else{
			transcationAlertDialog("Invalid  mobile number. Please try again!!!").show();
			return;
		}
		RegisterDealerAsync();
	}

	private AlertDialog transcationAlertDialog(String msg) {
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.app_name)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

	private ProgressDialog pd;
	private RTWS rtWS = new RTWS();
	private void RegisterDealerAsync() {
		new AsyncTask<Void, Void, RTResponse>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd = new ProgressDialog(getActivity(), R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Please wait ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				pd = new ProgressDialog(getActivity());
				pd.setTitle(ss1);
				pd.setMessage("Verifying ...");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected RTResponse doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				String sTS = FunctionUtil.getsDNReceivedID();
				String sEncKey = FunctionUtil.getsEncK(RegMobileNo + "RichTech6318" + sTS);
				return rtWS.RegisterDealer(SharedPreferenceUtil.getsClientUserName(),SharedPreferenceUtil.getsClientPassword(),RegName, RegMobileNo,"",
						"", String.valueOf(SharedPreferenceUtil.getsClientID()),sTS,sEncKey);

			}

			@SuppressWarnings("unused")
			@Override
			protected void onPostExecute(RTResponse result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(pd!=null)
					pd.dismiss();
				if(result.isResultBoolean()){
					SignUpSuccessAlertDialog("Congratulation. You have successful sign up. Password has been send to " + RegMobileNo + "\n" + result.getsResponseMessage()).show();

				}else{
					transcationAlertDialog(result.getsResponseMessage()).show();
				}


			}

		}.execute();
	}

	private AlertDialog SignUpSuccessAlertDialog(String msg) {
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.app_name)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								initText();
							}
						}).create();
	}


	private void initText(){
		edRegName.setText("");
		edRegMobileNo.setText("");
	}
}

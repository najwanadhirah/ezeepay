package com.rt.qpay99.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rt.qpay99.Config;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.activity.ui.AgentBIList;
import com.rt.qpay99.activity.ui.AgentInfoUI;
import com.rt.qpay99.activity.ui.CheckCustomerTxByDNUI;
import com.rt.qpay99.activity.ui.CheckCustomerTxStatusUI;
import com.rt.qpay99.activity.ui.ReportListUI;
import com.rt.qpay99.activity.ui.ShareCreditUI;
import com.rt.qpay99.activity.ui.VerifyAccountUI;
import com.rt.qpay99.adapter.MyAccountGridViewImageAdapter;
import com.rt.qpay99.adapter.MyAccountRecycleAdapter;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.NetworkUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MyAccountFragment_v2UI extends Fragment {
	private String TAG = this.getClass().getName();
	private MyAccountGridViewImageAdapter adapter;
	private RecyclerView gd;
	private AdView mAdView;
	ScrollTextView scrolltext;
	private Context mContext;
	private LayoutInflater inflater2;

	public MyAccountFragment_v2UI() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.recycleview_myaccount,
				container, false);
		mContext = this.getActivity();
		inflater2 = getActivity().getLayoutInflater().from(mContext);
		MobileAds.initialize(getActivity().getApplicationContext(),
				"ca-app-pub-7069957738539410/8983539706");


		sClientUserName = SharedPreferenceUtil.getsClientUserName();

		mAdView =  rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		gd =  rootView.findViewById(R.id.gdTopup);
		gd.setHasFixedSize(true);
		int numberOfColumns = FunctionUtil.calculateNoOfColumns(getActivity(),120);
//		gd.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
		gd.setLayoutManager(new LinearLayoutManager(getActivity()));
		gd.setAdapter(new MyAccountRecycleAdapter(mContext, new mRecyclerViewOnItemClick()));

		scrolltext =  rootView.findViewById(R.id.scrolltext);
		scrolltext.setText(Config.MOVING_TEXT);
		scrolltext.startScroll();

//		adapter = new MyAccountGridViewImageAdapter(this.getActivity());
//		gd.setAdapter(adapter);
//		gd.setOnItemClickListener(new MyAccountGridviewOnItemClickListener(this
//				.getActivity(), getFragmentManager()));



		return rootView;
	}

	private final int CHECK_BALANCE = 0;
	private final int CHECK_TRANSACTION = 1;
	private final int CHECK_REPORT = 2;
	private final int CHECK_BANK_IN = 3;
	private final int CHECK_BANK_IN_LIST = 4;
	private final int CHECK_AGENT_INFO = 5;
	private final int CHECK_DN = 6;
	private final int SHARE_CREDIT=7;
	private final int UPDATE_FB =8;
	private final int VERIFY =9;
	
	private RTWS rt = new RTWS();
	private String sClientUserName, sClientPassword, CheckCustomerStatus;

	private TextView tv;
	private EditText ed;
	private AlertDialog ad;
	private RTWS rtWS = new RTWS();
	private ProgressDialog pd;
	private LayoutInflater inflater;
	private Calendar myCalendar = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
	private SimpleDateFormat sdfMinSec = new SimpleDateFormat("mmss");
	private EditText edBankInDate, edBankInTime, edBankInAmount, edBankInCode;
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			// TODO Auto-generated method stub
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}

	};
	private void updateLabel() {
		String myFormat = "ddMM"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		edBankInDate.setText(sdf.format(myCalendar.getTime()));
	}

	public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
		@Override
		public void recyclerViewListClicked(View v, int position) {
			Intent intent = null;
			switch (position) {
				case CHECK_BALANCE:
					if(NetworkUtil.isOnline(mContext))
						CheckBalanceAsync();
					else
						Toast.makeText(mContext,"No internet connection.",Toast.LENGTH_SHORT).show();
					break;
				case CHECK_TRANSACTION:
					intent = new Intent(mContext, CheckCustomerTxStatusUI.class);
					mContext.startActivity(intent);
					break;
				case CHECK_REPORT:
					intent = new Intent(mContext, ReportListUI.class);
					mContext.startActivity(intent);
					break;
				case CHECK_BANK_IN:
					DLog.e(TAG, " Config.sMasterID " + Config.sMasterID);
					if (FunctionUtil.isSet(Config.sMasterID))
						if (!Config.sMasterID.equalsIgnoreCase(""))
							// block all E Biz Marketing downline
							if (!Config.sMasterID.equalsIgnoreCase("382"))
								getBankInAlertDialog().show();
					break;
				case CHECK_BANK_IN_LIST:
					intent = new Intent(mContext, AgentBIList.class);
					mContext.startActivity(intent);
					break;
				case CHECK_AGENT_INFO:
					intent = new Intent(mContext, AgentInfoUI.class);
					mContext.startActivity(intent);
					break;
				case CHECK_DN:
					intent = new Intent(mContext, CheckCustomerTxByDNUI.class);
					mContext.startActivity(intent);
					break;

				case SHARE_CREDIT:
					intent = new Intent(mContext, ShareCreditUI.class);
					mContext.startActivity(intent);
					break;

				case UPDATE_FB:
					intent = new Intent(mContext, HomeFragment.class);
					mContext.startActivity(intent);
					break;

				case VERIFY:
					intent = new Intent(mContext, VerifyAccountUI.class);
					mContext.startActivity(intent);
					break;


			}

		}
	}


	private AlertDialog getBankInAlertDialog() {
		View view = inflater2.inflate(R.layout.adapter_layout_bank_in, null);
		final Spinner spinnerBank;
		edBankInAmount =  view.findViewById(R.id.edBankInAmount);
		edBankInDate =  view.findViewById(R.id.edBankInDate);
		edBankInTime =  view.findViewById(R.id.edBankInTime);
		edBankInCode =  view.findViewById(R.id.edBankInCode);

		if (sClientUserName.substring(0, 2).equalsIgnoreCase("60")) {
			edBankInCode.setText(sClientUserName.substring(1, sClientUserName.length()));
		} else {
			edBankInCode.setText(sClientUserName);
		}

		spinnerBank =  view.findViewById(R.id.spinnerBank);
		List<String> list = new ArrayList<String>();
		list.add("MBB");
		list.add("CIMB");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerBank.setAdapter(dataAdapter);

		edBankInAmount = view.findViewById(R.id.edBankInAmount);

		edBankInDate.setText(sdf.format(new Date()));
		edBankInDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new DatePickerDialog(mContext, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		myCalendar = Calendar.getInstance();
		String hours = String.format("%02d%02d",
				myCalendar.get(Calendar.HOUR_OF_DAY),
				myCalendar.get(Calendar.MINUTE));
		edBankInTime.setText(hours);

		edBankInTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);
				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(mContext,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker timePicker,
												  int selectedHour, int selectedMinute) {
								String output = String.format("%02d%02d",
										selectedHour, selectedMinute);
								// edBankInTime.setText("" + selectedHour + ":"
								// + selectedMinute);
								edBankInTime.setText(output);
							}
						}, hour, minute, true);
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();

			}
		});

		return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
				R.style.AlertDialogCustom))
				// .setTitle(mName)
				.setTitle("Bank In Info")
				.setView(view)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								final String sBank, sAmount, sDate, sTime, sBiCode;

								String c = sClientUserName;
								DLog.e(TAG, "" + sClientUserName);
								if (sClientUserName.substring(0, 2).equalsIgnoreCase("60")) {
									sBiCode = sClientUserName.substring(1, sClientUserName.length());
								} else {
									sBiCode = sClientUserName;
								}


								if (spinnerBank.getSelectedItem().toString() == null)
									return;
								if (edBankInAmount.getText() == null)
									return;
								sBank = spinnerBank.getSelectedItem()
										.toString();
								sAmount = edBankInAmount.getText().toString();
								sDate = edBankInDate.getText().toString();
								sTime = edBankInTime.getText().toString();

								try {
									Double amt = Double.parseDouble(sAmount);
									if (amt < 299) {
										normalAlertDialog(
												"Minimum Bank In Amount RM300.")
												.show();
										return;
									}

								} catch (Exception e) {
									normalAlertDialog(
											"Minimum Bank In Amount RM300.")
											.show();
									return;
								}

								SubmitBankInAsync(sBank, sAmount, sDate, sTime,
										sBiCode);


							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	private void SubmitBankInAsync(final String sBank, final String sAmount,
								   final String sDate, final String sTime, final String sBiCode) {
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Please wait ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				pd = new ProgressDialog(mContext);
				pd.setTitle(ss1);
				pd.setMessage("Submiting ...");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				RTWS rt = new RTWS();
				return rt.SubmitBankIn(SharedPreferenceUtil.getsClientUserName(),
						SharedPreferenceUtil.getsClientPassword(), sBank, sAmount, sDate, sTime,
						sBiCode);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (pd != null)
					pd.dismiss();
				if (result) {
					normalAlertDialog("Transaction Successful.").show();
				} else {
					normalAlertDialog(
							"Transaction Failed. Sila Cuba Sebentar Lagi.")
							.show();
				}

			}
		}.execute();
	}

	private void CheckBalanceAsync() {
		new AsyncTask<Void, Void, CheckBalanceResponse>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(mContext);
				SpannableString ss1 = new SpannableString("Please wait ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				pd = new ProgressDialog(mContext);
				pd.setTitle(ss1);
				pd.setMessage("Check balance ...");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected CheckBalanceResponse doInBackground(Void... params) {

				return rt.CheckBalance(
						SharedPreferenceUtil.getsClientUserName(),
						SharedPreferenceUtil.getsClientPassword());
			}

			@Override
			protected void onPostExecute(CheckBalanceResponse result) {
				super.onPostExecute(result);
				if (pd != null)
					if (pd.isShowing()) {
						pd.dismiss();
						pd = null;
					}
				if (result.getsResponseStatus() != null) {
					if (result.getsResponseStatus().equalsIgnoreCase(
							"QUERY_SUCCESS")) {
						normalAlertDialog(
								"Your Account (" + SharedPreferenceUtil.getsClientUserName()
										+ ") balance is RM "
										+ result.getdBalance()).show();
					}
				}
			}
		}.execute();

	}

	private AlertDialog normalAlertDialog(final String msg) {

		return new AlertDialog.Builder(mContext)
				// .setTitle(mName)
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




	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DLog.e(TAG, " onResume");
		scrolltext.setText(Config.MOVING_TEXT);
	}

}

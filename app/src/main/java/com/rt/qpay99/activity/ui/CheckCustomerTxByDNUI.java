package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.CustomerTxStatusInfo;
import com.rt.qpay99.object.RequestReloadPinObject;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

public class CheckCustomerTxByDNUI extends FragmentActivity implements
		OnClickListener {

	private EditText edCustAcc;

	private Context mContext;

	private String TAG = this.getClass().getName();

	private Button btnCheckStats, btnPrint;

	private ProgressDialog pd;
	private String sDNId;

	CustomerTxStatusInfo mCustomerTxStatusInfo = new CustomerTxStatusInfo();
	private Button nav_btn_right;
	private TextView nav_text;
	private String mName, pDate, pAmount, pStatus, pDN, pCode, pLocalMOId,
			pReloadMSISDN, pProduct, pSN, pPin;

	private TextView tvChkProduct, tvChkAmount, tvChkStatus, tvChkDN,
			tvChkCode, tvChkDatetime, tvReloadMSISDN, tvPIN, tvSN;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_check_cust_tx_dn);
		// TODO Auto-generated method stub

		mContext = this;
		edCustAcc = (EditText) findViewById(R.id.edCustAcc);

		tvChkProduct = (TextView) findViewById(R.id.tvChkProduct);
		tvChkAmount = (TextView) findViewById(R.id.tvChkAmount);
		tvChkStatus = (TextView) findViewById(R.id.tvChkStatus);
		tvChkDN = (TextView) findViewById(R.id.tvChkDN);
		tvChkCode = (TextView) findViewById(R.id.tvChkCode);
		tvChkDatetime = (TextView) findViewById(R.id.tvChkDatetime);
		tvReloadMSISDN = (TextView) findViewById(R.id.tvReloadMSISDN);
		tvPIN = (TextView) findViewById(R.id.tvPIN);
		tvSN = (TextView) findViewById(R.id.tvSN);

		btnCheckStats = (Button) findViewById(R.id.btnCheckStats);
		btnPrint = (Button) findViewById(R.id.btnPrint);
		btnPrint.setOnClickListener(this);
		btnCheckStats.setOnClickListener(this);

		nav_text = (TextView) findViewById(R.id.nav_text);
		nav_text.setText(R.string.my_acc_check_DN);
		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		if (FunctionUtil.isSet(SRSApp.printerMacAdd))
			printDataService = new PrintDataService(mContext,
					SRSApp.printerMacAdd);

		InputMethodManager im = (InputMethodManager) this
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private AlertDialog connectPrinterAlertDialog() {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + " - No printer connected!!")
				.setMessage(
						"Please check printer connection before print out transaction record.")
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

	private RequestReloadPinObject mRequestReloadPinObject = new RequestReloadPinObject();
	RTWS rtWS = new RTWS();

	private void getGetReloadPIN(final String sClientUserName,
			final String sClientPassword, final String sLocalMOID) {
		new AsyncTask<Void, Void, RequestReloadPinObject>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
				// SpannableString ss1 = new
				// SpannableString("Pin requesting ...");
				// ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				// ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				// ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				// ss1.setSpan(
				// new ForegroundColorSpan(Color.parseColor("#008000")),
				// 0, ss1.length(), 0);
				// pd = new ProgressDialog(mContext);
				// pd.setTitle(ss1);
				pd.setMessage("Please wait ...");
				pd.setCancelable(false);
				pd.show();

			}

			@Override
			protected RequestReloadPinObject doInBackground(Void... params) {

				DLog.e(TAG, "sLocalMOID: " + sLocalMOID);
				return rtWS.getReloadPIN(sClientUserName, sClientPassword,
						sLocalMOID);

			}

			@Override
			protected void onPostExecute(RequestReloadPinObject result) {
				super.onPostExecute(result);
				if (pd != null)
					pd.dismiss();
				mRequestReloadPinObject = result;

				if (result.isGetReloadPINResult()) {
					if (result.getsReloadPin() != null) {
						DLog.e(TAG, "" + result.getsReloadPin());
						DLog.e(TAG, "" + result.getsSerialNumber());
//						pPin = result.getsReloadPin();
//						pSN = result.getsSerialNumber();
						if (FunctionUtil.isSet(result.getsRetailPrice()))
							pAmount = result.getsRetailPrice();
						tvChkAmount.setText(pAmount);
						tvPIN.setText(result.getsReloadPin());
						tvSN.setText(result.getsSerialNumber());
						if (PrintDataService.isPrinterConnected())
							// printReceipt();

							return;
					}
				}

			}
		}.execute();

	}

	private AlertDialog transcationFailedAlertDialog(String msg) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

	// private AlertDialog transcationSuccessAlertDialog(String msg) {
	// return new AlertDialog.Builder(new ContextThemeWrapper(this,
	// R.style.AlertDialogCustom))
	// .setTitle(mName)
	// .setMessage(msg)
	// .setCancelable(false)
	// .setPositiveButton(R.string.title_ok,
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface arg0, int arg1) {
	// if (PrintDataService.isPrinterConnected())
	// printReceipt();
	//
	// }
	// }).create();
	// }

	private void CheckCustomerTxStatus() {

		new AsyncTask<String, Void, CustomerTxStatusInfo>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
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
				pd.setMessage("Verifying DN ...");
				pd.setCancelable(false);
				pd.show();
			}

			protected CustomerTxStatusInfo doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				// if (edCustAcc.getText().length() == 0)
				// return null;
				sDNId = "";
				if (edCustAcc.getText().length() > 1)
					sDNId = edCustAcc.getText().toString();
				else
					return null;

				RTWS rtWS = new RTWS();
				return rtWS.GetPinTxByDN(
						SharedPreferenceUtil.getsClientUserName(),
						SharedPreferenceUtil.getsClientPassword(), "", sDNId);

			}

			@Override
			protected void onPostExecute(CustomerTxStatusInfo result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				// pd.dismiss();
				if (result != null) {

					mCustomerTxStatusInfo = result;
					DLog.e(TAG, "" + mCustomerTxStatusInfo.getAmount());
					DLog.e(TAG, "" + mCustomerTxStatusInfo.getProduct());
					DLog.e(TAG, "" + mCustomerTxStatusInfo.getLocalMOID());

					mName = mCustomerTxStatusInfo.getProduct();
					pSN = "";
					pPin = "";
					tvPIN.setText(pPin);
					tvSN.setText(pSN);

					pDate	 = "";
					if (FunctionUtil.isSet(mCustomerTxStatusInfo.getDateTime()))
						pDate = mCustomerTxStatusInfo.getDateTime();

					DLog.e(TAG, "pDate " + pDate.toString());
//					pDate = FunctionUtil.getConvertdate(pDate,
//							"yyyy-MM-dd HH:mm:ss");
					DLog.e(TAG, "pDate " + pDate.toString());
					tvChkDatetime.setText(pDate);

					pReloadMSISDN = "";
					if (FunctionUtil.isSet(mCustomerTxStatusInfo
							.getsReloadMSISDN()))
						pReloadMSISDN = mCustomerTxStatusInfo
								.getsReloadMSISDN();
					tvReloadMSISDN.setText(pReloadMSISDN);

					pAmount = "";
//					if (FunctionUtil.isSet(mCustomerTxStatusInfo.getAmount()))
//						pAmount = mCustomerTxStatusInfo.getAmount();
//					tvChkAmount.setText(pAmount);
					if (FunctionUtil.isSet(mCustomerTxStatusInfo.getRetailPrice()))
						pAmount = mCustomerTxStatusInfo.getRetailPrice();
					tvChkAmount.setText(pAmount);

					pStatus = "";
					if (FunctionUtil.isSet(mCustomerTxStatusInfo.getStatus()))
						pStatus = mCustomerTxStatusInfo.getStatus();
					tvChkStatus.setText(pStatus);

					pProduct = "";
					if (FunctionUtil.isSet(mCustomerTxStatusInfo.getProduct()))
						pProduct = mCustomerTxStatusInfo.getProduct();
					tvChkProduct.setText(pProduct);

					pCode = "";
					if (FunctionUtil.isSet(mCustomerTxStatusInfo.getCode()))
						pStatus = mCustomerTxStatusInfo.getCode();
					tvChkCode.setText(pCode);

					pDN = "";
					if (FunctionUtil.isSet(mCustomerTxStatusInfo.getDN()))
						pDN = mCustomerTxStatusInfo.getDN();
					tvChkDN.setText(pDN);

					pReloadMSISDN = "";
					if (FunctionUtil.isSet(mCustomerTxStatusInfo
							.getsReloadMSISDN()))
						pReloadMSISDN = mCustomerTxStatusInfo
								.getsReloadMSISDN();

					pLocalMOId = mCustomerTxStatusInfo.getLocalMOID();
					getGetReloadPIN(SharedPreferenceUtil.getsClientUserName(),
							SharedPreferenceUtil.getsClientPassword(),
							pLocalMOId);

				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edCustAcc.getWindowToken(), 0);
			}

		}.execute();

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCheckStats:
			if (edCustAcc.getText().length() > 1)
				CheckCustomerTxStatus();
			break;

		case R.id.btnPrint:
			if ((mName != "") && (mName == null))
				break;
			if ((pDN != "") && (pDN == null))
				break;
			if (PrintDataService.isPrinterConnected())
				printReceipt();
			else
				connectPrinterAlertDialog().show();
			break;
		}

	}

	private PrintDataService printDataService = null;

	private void printReceipt() {
		printDataService.setCommand(21);
		printDataService.send("\n");
		printDataService.setCommand(23);
		printDataService.setCommand(4);
		printDataService.setCommand(21);
		//printDataService.printImage();
		printDataService.send(mContext.getResources().getString(R.string.app_name) + " ");
		printDataService.setCommand(4);
		printDataService.setCommand(20);
		printDataService.setCommand(35);
		printDataService.setCommand(36);
		printDataService.setCommand(37);
		printDataService.send("\n");
		printDataService.send("** Duplicate Copied **");
		printDataService.send("\n");
		printDataService.setCommand(3);
		printDataService.setCommand(2);
		printDataService.send("\n");

		printDataService.send("Transaction Date :");
		printDataService.send(countSpacing("Transaction Date :", pDate));
		printDataService.send(pDate);

		printDataService.send("\n");

		printDataService.send("Serial No:");
		printDataService.send(countSpacing("Serial No:",
				mRequestReloadPinObject.getsSerialNumber()));
		printDataService.send(mRequestReloadPinObject.getsSerialNumber());
		printDataService.send("\n");

		if (mName.indexOf("PIN") > -1) {
			printDataService.send("Transaction No. :");
			printDataService.send(countSpacing("Transaction No. :", pDN));
			printDataService.send(pDN);
			printDataService.send("\n");
		}

		printDataService.setCommand(2);
		printDataService.setCommand(22);
		printDataService.setCommand(21);

		printDataService.setCommand(3);
		printDataService.setCommand(4);
		// printDataService.setCommand(20);

		if (mName.indexOf("PIN") > -1) {
			printDataService.setCommand(21);
			// printDataService.send(mRequestReloadPinObject.getsReloadTelco()
			// + " " + "RM" + mRequestReloadPinObject.getsAmount());
			printDataService.send(pProduct + " " + "RM"
					+ mRequestReloadPinObject.getsRetailPrice());
			printDataService.send("\n");
			printDataService.setCommand(20);
			printDataService.send("PIN :");
			printDataService.send("\n");
			printDataService.send(mRequestReloadPinObject.getsReloadPin());
			printDataService.setCommand(3);
			printDataService.send("\n");

			printDataService.setCommand(2);
			printDataService.send("Pin Expired Date");
			printDataService.send("\n");

			if (mRequestReloadPinObject.getsExpiryDate() != null)
				printDataService.send(mRequestReloadPinObject.getsExpiryDate());
			printDataService.send("\n");

			printDataService.setCommand(2);
			printDataService.send("Topup Instruction");
			printDataService.send("\n");
			if (mRequestReloadPinObject.getsDescription() != null)
				printDataService
						.send(mRequestReloadPinObject.getsDescription());
			printDataService.send("\n");

		} else {
			printDataService.setCommand(21);
			printDataService.send(mName + " " + "RM" + pAmount);
			printDataService.send("\n");
		}
		printDataService.setCommand(3);
		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.send("Customer Care Line: " + Config.Custotmer_care);
		printDataService.send("\n");
		printDataService.send("9AM - 6PM (Monday - Friday)");
		printDataService.send("\n");
//		printDataService.send("www.QPay99.com");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");

	}

	private String countSpacing(String name, String total) {
		String space = "";
		if (total == null)
			total = "";

		int totalSpacing = 41;
		int cLen = name.length() + total.length();
		int newLen = totalSpacing - cLen;

		for (int i = 0; i <= newLen; i++) {
			space = space + " ";
		}

		return space;
	}

	String BILL;

	private String getBillBody() {
		BILL = "Agent No:     " + SharedPreferenceUtil.getsClientUserName()
				+ "\n";
		BILL += "Product:      " + mName + "\n";
		BILL += "Terminal:     " + SharedPreferenceUtil.getsDeviceID() + "\n";
		BILL += "Date:         " + pDate + "\n";
		BILL += "DN:           " + pDN + "\n";
		// BILL += "Code          " + pCode + "\n";

		return BILL;

	}
}

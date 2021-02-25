package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.List;

public class PayBillDetailUI extends FragmentActivity {

	private String TAG = this.getClass().getName();
	private TextView nav_text, tvBalance;

	private ProgressDialog pd;
	private Context mContext;

	private String Denomination, mName, mkeyword;

	private TextView tvRM30, tvRM40, tvRM50, tvRM60, tvRM70, tvRM80,
			tvProductConfirmBuy, tvProductName, tvSenderValue, tvSenderMobile,
			tvlain_lain;
	InputFilter[] filterArray = new InputFilter[1];
	private ImageView productImage;
	private List<String> denominationArray;

	private Button nav_btn_right;

	private String sCustomerMobileNumber, dProductPrice, sProductID,
			sCustomerAccountNumber, sClientTxID, BILL;

	private ViewFlipper productViewFlipper;
	private int MaxLen = 0, MinLen = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_product_paybill);
		Bundle extras = getIntent().getExtras();
		Denomination = extras.getString("Denomination");
		DLog.e(TAG, "" + "" + Denomination);

		if (extras.getString("MaxLen") != null) {
			MaxLen = Integer.parseInt(extras.getString("MaxLen"));
			DLog.e(TAG, "MaxLen " + MaxLen);
		}

		if (extras.getString("MinLen") != null) {
			MinLen = Integer.parseInt(extras.getString("MinLen"));
			DLog.e(TAG, "MinLen " + MinLen);
		}

		denominationArray = FunctionUtil.splitToStringList(Denomination, ",");
		// DLog.e(TAG, "" + "" + denominationArray.contains("300"));
		mName = extras.getString("Name");
		sProductID = extras.getString("pId");
		DLog.e(TAG, "" + "sProductID" + sProductID);
		productImage = (ImageView) findViewById(R.id.productImage);
		productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));

		nav_text = (TextView) findViewById(R.id.nav_text);
		tvBalance = (TextView) findViewById(R.id.tvBalance);
		tvRM30 = (TextView) findViewById(R.id.tvRM30);
		tvRM40 = (TextView) findViewById(R.id.tvRM40);
		tvRM50 = (TextView) findViewById(R.id.tvRM50);
		tvRM60 = (TextView) findViewById(R.id.tvRM60);
		tvRM70 = (TextView) findViewById(R.id.tvRM70);
		tvRM80 = (TextView) findViewById(R.id.tvRM80);
		tvlain_lain = (TextView) findViewById(R.id.tvlain_lain);
		tvSenderValue = (TextView) findViewById(R.id.tvSenderValue);
		tvSenderMobile = (TextView) findViewById(R.id.tvSenderMobile);
		tvProductName = (TextView) findViewById(R.id.tvProductName);
		tvProductConfirmBuy = (TextView) findViewById(R.id.tvProductConfirmBuy);

		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		tvProductConfirmBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RequestInputAsync();
				// confirmBuyAlertDialog().show();
			}
		});

		tvRM30.setEnabled(true);
		tvRM40.setEnabled(true);
		tvRM50.setEnabled(true);
		tvRM60.setEnabled(true);
		tvRM70.setEnabled(true);
		tvRM80.setEnabled(true);

		tvRM30.setVisibility(View.VISIBLE);
		tvRM40.setVisibility(View.VISIBLE);
		tvRM50.setVisibility(View.VISIBLE);
		tvRM60.setVisibility(View.VISIBLE);
		tvRM70.setVisibility(View.VISIBLE);
		tvRM80.setVisibility(View.VISIBLE);
		if (!denominationArray.contains("30")) {
			tvRM30.setEnabled(false);
			tvRM30.setTextColor(Color.DKGRAY);
			// tvRM30.setVisibility(View.INVISIBLE);
		}

		if (!denominationArray.contains("40")) {
			tvRM40.setEnabled(false);
			tvRM40.setTextColor(Color.DKGRAY);
			// tvRM40.setVisibility(View.INVISIBLE);
		}

		if (!denominationArray.contains("50")) {
			tvRM50.setEnabled(false);
			tvRM50.setTextColor(Color.DKGRAY);
			// tvRM50.setVisibility(View.INVISIBLE);
		}

		if (!denominationArray.contains("60")) {
			tvRM60.setEnabled(false);
			tvRM60.setTextColor(Color.DKGRAY);
			// tvRM60.setVisibility(View.INVISIBLE);
		}

		if (!denominationArray.contains("70")) {
			tvRM70.setEnabled(false);
			tvRM70.setTextColor(Color.DKGRAY);
			// tvRM70.setVisibility(View.INVISIBLE);
		}

		if (!denominationArray.contains("80")) {
			tvRM80.setEnabled(false);
			tvRM80.setTextColor(Color.DKGRAY);
			// tvRM80.setVisibility(View.INVISIBLE);
		}

		tvRM30.setOnClickListener(new valueOnClickListener("30"));
		tvRM40.setOnClickListener(new valueOnClickListener("40"));
		tvRM50.setOnClickListener(new valueOnClickListener("50"));
		tvRM60.setOnClickListener(new valueOnClickListener("60"));
		tvRM70.setOnClickListener(new valueOnClickListener("70"));
		tvRM80.setOnClickListener(new valueOnClickListener("80"));

		tvlain_lain.setOnClickListener(new valueOnClickListener(""));

		productViewFlipper = (ViewFlipper) findViewById(R.id.productViewFlipper);

		tvProductName.setText(mName);

		if (mName != null)
			nav_text.setText("PAYMENT ");

		if (FunctionUtil.isSet(SRSApp.printerMacAdd))
			printDataService = new PrintDataService(mContext,
					SRSApp.printerMacAdd);
		getMemberBalance();
	}

	private RTWS rt = new RTWS();

	private void getMemberBalance() {
		new AsyncTask<Void, Void, CheckBalanceResponse>() {

			@Override
			protected CheckBalanceResponse doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				return rt.CheckBalance(
						SharedPreferenceUtil.getsClientUserName(),
						SharedPreferenceUtil.getsClientPassword());

			}

			@Override
			protected void onPostExecute(CheckBalanceResponse result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result.getsResponseStatus() != null) {
					if (result.getsResponseStatus().equalsIgnoreCase(
							"QUERY_SUCCESS")) {
						if (Double.parseDouble(result.getdBalance()) < 250) {
							tvBalance.setTextColor(Color.RED);
						} else {
							tvBalance.setTextColor(Color.parseColor("#FFA500"));
						}
						tvBalance.setText("Balance RM " + result.getdBalance());
					}
				}
			}

		}.execute();
	}

	private PrintDataService printDataService = null;

	private void connectPrinterAsync() {
		new AsyncTask<Void, Void, Boolean>() {

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
				pd.setMessage("Connecting printer ...");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				DLog.e(TAG, "printDataService.connect()");
				printDataService = new PrintDataService(mContext,
						SRSApp.printerMacAdd);
				return printDataService.connect();

			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pd.dismiss();
				if (result) {
					Toast.makeText(mContext, "Printer Connected",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext,
							"Please check printer to use PIN page",
							Toast.LENGTH_LONG).show();
				}
			}

		}.execute();
	}

	private void RequestInputAsync() {
		new AsyncTask<Void, Void, RequestInputResponse>() {

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
				pd.setMessage("Verifying ...");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected RequestInputResponse doInBackground(Void... params) {
				RTWS rt = new RTWS();
				sClientTxID = FunctionUtil.getStringDateTimeSec();
				RequestInputResponse result = rt.RequestInput(
						sCustomerAccountNumber, sCustomerMobileNumber,
						dProductPrice, sProductID, "ANDROID_Q", sClientTxID,"",
						"", "", "");
				return result;
			}

			@Override
			protected void onPostExecute(RequestInputResponse result) {
				super.onPostExecute(result);
				pd.dismiss();
				if (result != null) {
					if (Config.WS_SUBMIT_SUCCESS.equalsIgnoreCase(result
							.getsResponseStatus())) {
						DLog.e(TAG, result.getsResponseStatus());

						transcationSuccessAlertDialog("Transaction Completed.")
								.show();
						return;
					}
				}

				transcationFailedAlertDialog(
						"Transaction Failed. Please try again later. Error : "
								+ result.getsResponseStatus()).show();

			}
		}.execute();

	}

	private AlertDialog connectPrinterAlertDialog() {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + " - No printer connected!!")
				.setMessage("Please connect printer!!!")
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// RequestInputAsync();
							}
						}).create();
	}

	private class valueOnClickListener implements OnClickListener {
		String mValue;

		public valueOnClickListener(String mValue) {
			this.mValue = mValue;
		}

		@Override
		public void onClick(View v) {

			if (mValue.equalsIgnoreCase("")) {
				lainlainAlertDialog("").show();
			} else {
				insertMSISDNAlertDialog(mValue).show();
			}
		}
	}

	private boolean IsUtilitiesBill(String mName) {

		if(mName.equalsIgnoreCase("MAXISBILL"))
			return false;
		if(mName.equalsIgnoreCase("DIGIBILL"))
			return false;
		if(mName.equalsIgnoreCase("CELCOMBILL"))
			return false;
		if(mName.equalsIgnoreCase("UMOBILEBILL"))
			return false;
		if(mName.equalsIgnoreCase("DIGIBILL"))
			return false;
		if(mName.equalsIgnoreCase("DIGIBILL"))
			return false;

		return true;
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

	private AlertDialog transcationSuccessAlertDialog(String msg) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								if (SharedPreferenceUtil.isRequiredPrinter())
									printReceipt();
								finish();
							}
						}).create();
	}

	private AlertDialog confirmBuyAlertDialog() {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName)
				.setMessage("Please click OK to proceed.")
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								RequestInputAsync();
								// buyProductAsync();
							}
						}).setNeutralButton(R.string.title_cancel, null)
				.create();
	}

	private void buyProductAsync() {
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
				pd.setMessage("Verifying ...");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				pd.dismiss();
			}
		}.execute();

	}

	private AlertDialog lainlainAlertDialog(String mobileNo) {
		View view = getLayoutInflater().inflate(
				R.layout.view_layout_insert_mobile_no_paybill, null);
		final EditText edMobileNo, edValue;
		edMobileNo = (EditText) view.findViewById(R.id.edMobileNo);
		// edMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);

		edValue = (EditText) view.findViewById(R.id.edValue);
		edValue.setInputType(InputType.TYPE_CLASS_PHONE);


        if((mName.equalsIgnoreCase("UMOBILEBILL")) || (mName.equalsIgnoreCase("CELCOMBILL"))|| (mName.equalsIgnoreCase("DIGIBILL"))|| (mName.equalsIgnoreCase("MAXISBILL"))) {
            edValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        edValue.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(edValue, 0);
            }
        }, 50);

		if (mobileNo.length() > 8)
			edMobileNo.setText(mobileNo);

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName)
				.setView(view)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								final String mValue = edValue.getText()
										.toString();
								final String mMobile = edMobileNo.getText()
										.toString();
								DLog.e(TAG, "mValue " + mValue);
								DLog.e(TAG, "mMobile" + mMobile);
								// validate(mValue, mMobile);
								ConfirmationAlertDialog(mValue, mMobile).show();
							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	private AlertDialog ConfirmationAlertDialog(final String mValue,
			final String msisdn) {
		final EditText input = new EditText(PayBillDetailUI.this);
		// input.setInputType(InputType.TYPE_CLASS_PHONE);
		input.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager keyboard = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboard.showSoftInput(input, 0);
			}
		}, 50);
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + " - RM" + mValue)
				.setView(input)
				.setMessage("Sila Masuk Telephone No. / Bil No. sekali lagi")
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								if (input.getText().toString()
										.equalsIgnoreCase(msisdn)) {
									DLog.e(TAG, "SUCCESS VALIDATE");
									validate(mValue, input.getText().toString());
					 			} else {
									DLog.e(TAG, "INVALID");
									invalidMSISDNAlertDialog(mValue,
											"Telephone No. / Bil No!! Sila cuba sekeli lagi.")
											.show();
								}
							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	private AlertDialog insertMSISDNAlertDialog(final String mValue) {
		final EditText input = new EditText(PayBillDetailUI.this);

        input.setInputType(InputType.TYPE_CLASS_PHONE);


        if((mName.equalsIgnoreCase("UMOBILEBILL")) || (mName.equalsIgnoreCase("CELCOMBILL"))|| (mName.equalsIgnoreCase("DIGIBILL"))|| (mName.equalsIgnoreCase("MAXISBILL"))|| (mName.equalsIgnoreCase("XOXBILL"))|| (mName.equalsIgnoreCase("TNBBILL"))) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }


		input.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager keyboard = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboard.showSoftInput(input, 0);
			}
		}, 50);
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + " - RM" + mValue)
				.setView(input)
				.setMessage(R.string.telefon_no)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// validate(mValue, input.getText().toString());
								ConfirmationAlertDialog(mValue,
										input.getText().toString()).show();
							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	private AlertDialog invalidMSISDNAlertDialog(final String mValue,
			final String message) {

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				// .setTitle(mName)
				.setTitle("Alert -")
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								insertMSISDNAlertDialog(mValue).show();
							}
						}).create();
	}

	private AlertDialog invalidValueAlertDialog(final String mobileNo) {

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				// .setTitle(mName)
				.setTitle("Alert -")
				.setMessage("Nilai tambah tidak sah. Sila masuk sekali lagi.")
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								lainlainAlertDialog(mobileNo).show();
							}
						}).create();
	}

	private void validate(String mValue, String mobileNo) {

		if (!denominationArray.contains(mValue)) {
			invalidValueAlertDialog(mobileNo).show();
			return;
		}

		if (mobileNo.trim().length() < MinLen
				|| mobileNo.trim().length() > MaxLen) {
			invalidMSISDNAlertDialog(mValue, "No. Bil/Telepon tidak sah.")
					.show();
			return;
		}

		DLog.e(TAG, "mValue" + mValue);
		DLog.e(TAG, "mobileNo" + mobileNo);
		tvSenderMobile.setText("RM " + mValue);
		tvSenderValue.setText(mobileNo);
		sCustomerMobileNumber = mobileNo;
		sCustomerAccountNumber = mobileNo;
		dProductPrice = mValue;
		productViewFlipper.showNext();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (productViewFlipper.getDisplayedChild() == 1)
				transcationSuccessAlertDialog(
						"Do you want to cancel this transaction?").show();
		}
		return super.onKeyDown(keyCode, event);
	}

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
		printDataService.send("PAY BILL ");
		printDataService.setCommand(35);
		printDataService.setCommand(36);
		printDataService.setCommand(37);
		printDataService.send("\n");
		printDataService.send(SharedPreferenceUtil.getMerchantName());
		printDataService.send("\n");
		printDataService.setCommand(3);
		printDataService.setCommand(2);

		printDataService.send("Date:");
		printDataService.send(countSpacing("Date:",
				FunctionUtil.getStrCurrentDateTime()));
		printDataService.send(FunctionUtil.getStrCurrentDateTime());
		printDataService.send("\n");

		printDataService.send("Ref No:");
		printDataService.send(countSpacing("Ref No:", sClientTxID));
		printDataService.send(sClientTxID);

		printDataService.send("Status:");
		printDataService.send(countSpacing("Status:", "Accepted"));
		printDataService.send("Accepted");
		printDataService.send("\n");
		printDataService.send("\n");

		printDataService.setCommand(2);
		printDataService.setCommand(22);

		printDataService.setCommand(3);
		printDataService.setCommand(4);

		printDataService.setCommand(21);
		printDataService.send(sCustomerAccountNumber);
		printDataService.send("\n");
		printDataService.send(mName + " " + "RM" + dProductPrice);
		printDataService.send("\n");

		printDataService.setCommand(2);
		printDataService.setCommand(22);
		printDataService.setCommand(3);

		printDataService.send("\n");
		printDataService.send("\n");
        printDataService.setCommand(21);
        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
		printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");
        printDataService.send(Config.Custotmer_website);
		printDataService.send("\n");
		printDataService.send("Thank You " + "Version : "+SRSApp.versionName);
		printDataService.send("\n");
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

	private String getBillBody() {
		BILL = "Customer No:          "
				+ SharedPreferenceUtil.getsClientUserName() + "\n";
		BILL += "Terminal:             " + SharedPreferenceUtil.getsDeviceID()
				+ "\n";
		BILL += "Mobile No.:           " + sCustomerAccountNumber + "\n";
		BILL += "Date:                 " + FunctionUtil.getStrCurrentDateTime()
				+ "\n";
		BILL += "Ref No:               " + sClientTxID + "\n";

		return BILL;

	}

	@Override
	protected void onDestroy() {
		close();
		super.onDestroy();
	}

	private void close() {
		// if (PrintDataService.isPrinterConnected())
		// PrintDataService.disconnect();
	}

}

package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.InternationalProductAdapter;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.List;

public class ProductInternationalDetailUI_v2 extends AppCompatActivity {

	private String TAG = "ProductDetailUI";
	private TextView  tvBalance;

	private ProgressDialog pd;
	private Context mContext;

	private String Denomination, mName, DenominationDescription;

	private TextView  tvProductConfirmBuy, tvProductName, tvSenderValue,
			tvSenderMobile, tvlain_lain;
	InputFilter[] filterArray = new InputFilter[1];





	private String sCustomerMobileNumber, dProductPrice, sProductID,
			sCustomerAccountNumber, sClientTxID;

	private ViewFlipper productViewFlipper;
	private ImageView productImage;

	private PrintDataService printDataService = null;
	String BILL;
	String priceTAG;

	private GridView gridview;
	private InternationalProductAdapter mInternationalProductAdapter;
	private List<String> denominationArray,denominationDescriptionArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_product_international);
		Bundle extras = getIntent().getExtras();
		Denomination = extras.getString("Denomination");
		DenominationDescription   = extras.getString("DenominationDescription");
		DLog.e(TAG, " Denomination "  + Denomination);
		DLog.e(TAG, " DenominationDescription "  + DenominationDescription);

		denominationArray = FunctionUtil.splitToStringList(Denomination, ",");
		if(FunctionUtil.isSet(DenominationDescription)){
			denominationDescriptionArray = FunctionUtil.splitToStringList(DenominationDescription, ",");
		}


		mName = extras.getString("Name");
		sProductID = extras.getString("pId");
		DLog.e(TAG, "" + "sProductID" + sProductID);
		productImage = (ImageView) findViewById(R.id.productImage);
		productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));

		tvBalance = (TextView) findViewById(R.id.tvBalance);

		tvlain_lain = (TextView) findViewById(R.id.tvlain_lain);
		tvSenderValue = (TextView) findViewById(R.id.tvSenderValue);
		tvSenderMobile = (TextView) findViewById(R.id.tvSenderMobile);
		tvProductName = (TextView) findViewById(R.id.tvProductName);
		tvProductConfirmBuy = (TextView) findViewById(R.id.tvProductConfirmBuy);



		gridview = (GridView) findViewById(R.id.gridview);
		mInternationalProductAdapter = new InternationalProductAdapter(mContext,denominationArray,denominationDescriptionArray);
		gridview.setAdapter(mInternationalProductAdapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if(denominationDescriptionArray!=null){
					priceTAG = denominationDescriptionArray.get(position);
					insertMSISDNAlertDialog(denominationArray.get(position), denominationDescriptionArray.get(position)).show();
				}else{
					priceTAG = denominationArray.get(position);
					insertMSISDNAlertDialog(denominationArray.get(position), denominationArray.get(position)).show();
				}

			}
		});


		tvProductConfirmBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// confirmBuyAlertDialog().show();
				RequestInputAsync();
			}
		});


		tvlain_lain.setOnClickListener(new valueOnClickListener("", ""));

		productViewFlipper = (ViewFlipper) findViewById(R.id.productViewFlipper);

		tvProductName.setText(mName);
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
						dProductPrice, sProductID, "ANDROID_Q", sClientTxID,
						"", "", "", "");
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
						// if (PrintDataService.isPrinterConnected())
						// printReceipt();
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

	private class valueOnClickListener implements OnClickListener {
		String mValue;
		String tags;

		public valueOnClickListener(String mValue, String tags) {
			this.mValue = mValue;
			this.tags = tags;

		}

		@Override
		public void onClick(View v) {
			DLog.e(TAG, "mValue  " + mValue);
			priceTAG = this.tags;
			if (mValue.equalsIgnoreCase("")) {
				// lainlainAlertDialog("").show();
			} else {
				insertMSISDNAlertDialog(mValue, tags).show();
			}
		}
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
								finish();
							}
						}).create();
	}

	// private void connectPrinterAsync() {
	// new AsyncTask<Void, Void, Boolean>() {
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// pd = ProgressDialog.show(mContext, "Please wait ...",
	// "Connecting printer ...", true);
	// pd.setCancelable(false);
	// }
	//
	// @Override
	// protected Boolean doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// DLog.e(TAG, "printDataService.connect()");
	// printDataService = new PrintDataService(mContext,
	// SRSApp.printerMacAdd);
	// return printDataService.connect();
	//
	// }
	//
	// @Override
	// protected void onPostExecute(Boolean result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// pd.dismiss();
	// if (result) {
	// Toast.makeText(mContext, "Printer Connected",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Toast.makeText(mContext, "Connecting failed", 1).show();
	// }
	// }
	//
	// }.execute();
	// }

	private void printReceipt() {
		printDataService.setCommand(4);
		printDataService.setCommand(21);
		printDataService.send(Config.printer_CompanyName);
		printDataService.setCommand(3);
		printDataService.send("\n");
		printDataService.send("Soft PIN");
		printDataService.setCommand(2);
		printDataService.setCommand(20);
		printDataService.send("\n");
		printDataService.send(getBillBody());
		printDataService.send("\n");
		printDataService.setCommand(2);
		printDataService.setCommand(22);
		getBillBody();
		printDataService.setCommand(3);
		printDataService.send("\n");

		printDataService.setCommand(21);
		printDataService.send("Customer Care Line: +60129336318");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
	}

	private String getBillBody() {
		// BILL = "Store:                 " + "1234567890" + "\n";
		BILL += "Staff ID:              "
				+ SharedPreferenceUtil.getsClientUserName() + "\n";
		BILL += "Terminal:              " + SharedPreferenceUtil.getsDeviceID()
				+ "\n";
		BILL += "Date:                  "
				+ FunctionUtil.getStrCurrentDateTime() + "\n";
		BILL += "Customer No:           "
				+ SharedPreferenceUtil.getsClientUserName() + "\n";
		BILL += "Expiry Date:           "
				+ FunctionUtil.getStrCurrentDateTime() + "\n";
		BILL += "Ref ID:                " + sClientTxID + "\n";
		BILL += "Value (RM):             " + dProductPrice + "\n";
		return BILL;

	}

	private AlertDialog transcationSuccessAlertDialog(String msg) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom)).setTitle(mName).setMessage(msg)
				.setCancelable(false)
				// .setNegativeButton(R.string.reprint,
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface arg0, int arg1) {
				// if (PrintDataService.isPrinterConnected())
				// printReceipt();
				//
				// }
				// })
				.setPositiveButton(R.string.title_closed,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
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
		edMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);

		edValue = (EditText) view.findViewById(R.id.edValue);
		edValue.setInputType(InputType.TYPE_CLASS_PHONE);
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
								validate(mValue, mMobile);
							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	private AlertDialog insertMSISDNAlertDialog(final String mValue,
												final String tags) {
		final EditText input = new EditText(ProductInternationalDetailUI_v2.this);
		input.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager keyboard = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboard.showSoftInput(input, 0);
			}
		}, 50);
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		filterArray[0] = new InputFilter.LengthFilter(Config.MAX_MOBILE_LENGHT);
		input.setFilters(filterArray);
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + " - " + tags)
				.setView(input)
				.setMessage(R.string.cust_telefon_no)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								validate(mValue, input.getText().toString());
							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	private AlertDialog invalidMSISDNAlertDialog(final String mValue) {

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				// .setTitle(mName)
				.setTitle("Alert -")
				.setMessage("No. Telefon tidak sah.")
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								insertMSISDNAlertDialog(mValue, "").show();
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

		if (mobileNo.trim().length() < 8) {
			invalidMSISDNAlertDialog(mValue).show();
			return;
		}

		DLog.e(TAG, "mValue" + mValue);
		DLog.e(TAG, "mobileNo" + mobileNo);
		tvSenderMobile.setText("RM " + mValue);
		tvSenderMobile.setText(priceTAG);
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

	@Override
	protected void onDestroy() {
		close();
		super.onDestroy();
	}

	private void close() {
		if (PrintDataService.isPrinterConnected())
			PrintDataService.disconnect();
	}

}

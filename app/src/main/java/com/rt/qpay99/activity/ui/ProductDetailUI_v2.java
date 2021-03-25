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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.adapter.TopupProductAdapter_v2;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.object.ProductPrice;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailUI_v2 extends AppCompatActivity {

	private String TAG = "ProductDetailUI";
	private TextView  tvBalance;

	private ProgressDialog pd;
	private Context mContext;

	private String Denomination, mName, DenominationDescription;

	private TextView tvProductConfirmBuy, tvProductName, tvSenderValue, tvSenderMobile,
			tvlain_lain;

	InputFilter[] filterArray = new InputFilter[1];

	private Switch printReceipt;

//	private Button nav_btn_right;

	private String sCustomerMobileNumber, dProductPrice, sProductID,
			sCustomerAccountNumber, sClientTxID;

	private ViewFlipper productViewFlipper;
	private ImageView productImage;

	private PrintDataService printDataService = null;
	String BILL;
	private int MaxLen = 0, MinLen = 0;
    private double GST_Tax=0.00;

	private GridView gridview;
	private TopupProductAdapter_v2 mInternationalProductAdapter;
	private List<String> denominationArray,denominationDescriptionArray;
	private String priceTAG;
	List<ProductPrice> topUpPriceList= new ArrayList<ProductPrice>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_layout_product);
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
		setTitle(mName);
		sProductID = extras.getString("pId");
		DLog.e(TAG, "" + "sProductID" + sProductID);
		productImage = (ImageView) findViewById(R.id.productImage);
		productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));


		AgentProductDiscount p = SRSApp.hashmapDiscountRate.get(mName);
		if(p!=null){
			DLog.e(TAG,"" + p.getDiscountRate() + " " + p.getDiscountType());
		}



		printReceipt = (Switch) findViewById(R.id.printReceipt);
		printReceipt.setChecked(false);
		if (SharedPreferenceUtil.isPrintTopup())
			printReceipt.setChecked(true);

		printReceipt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				DLog.e(TAG, "isChecked " + isChecked);
				SharedPreferenceUtil.editIsPrintTopup(isChecked);
				// Do Something
			}
		});



		tvBalance = (TextView) findViewById(R.id.tvBalance);


		tvlain_lain = (TextView) findViewById(R.id.tvlain_lain);
		tvSenderValue = (TextView) findViewById(R.id.tvSenderValue);
		tvSenderMobile = (TextView) findViewById(R.id.tvSenderMobile);
		tvProductName = (TextView) findViewById(R.id.tvProductName);
		tvProductConfirmBuy = (TextView) findViewById(R.id.tvProductConfirmBuy);

		if (extras.getString("MaxLen") != null) {
			MaxLen = Integer.parseInt(extras.getString("MaxLen"));
			DLog.e(TAG, "MaxLen " + MaxLen);
		}

        try{
            if (extras.getString("Tax") != null) {
                GST_Tax = Double.parseDouble(extras.getString("Tax"));
				GST_Tax= 0;
                DLog.e(TAG, "Tax " + GST_Tax);
            }
        }catch (Exception e){
            DLog.w(TAG, "GST_Tax Err " + e.getMessage());
        }


		if (extras.getString("MinLen") != null) {
			MinLen = Integer.parseInt(extras.getString("MinLen"));
			DLog.e(TAG, "MinLen " + MinLen);
		}

		tvProductConfirmBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// confirmBuyAlertDialog().show();

				RequestInputAsync();
			}
		});


		topUpPriceList = new ArrayList<ProductPrice>();

		if(denominationArray.contains("5")){
			topUpPriceList.add(new ProductPrice("RM 5", "5"));
		}
		if(denominationArray.contains("10")){
			topUpPriceList.add(new ProductPrice("RM 10", "10"));
		}
		if(denominationArray.contains("15")){
			topUpPriceList.add(new ProductPrice("RM 15", "15"));
		}
		if(denominationArray.contains("20")){
			topUpPriceList.add(new ProductPrice("RM 20", "20"));
		}
		if(denominationArray.contains("30")){
			topUpPriceList.add(new ProductPrice("RM 30", "30"));
		}
		if(denominationArray.contains("50")){
			topUpPriceList.add(new ProductPrice("RM 50", "50"));
		}
		if(denominationArray.contains("55")){
			topUpPriceList.add(new ProductPrice("RM 55", "55"));
		}
		if(denominationArray.contains("60")){
			topUpPriceList.add(new ProductPrice("RM 60", "60"));
		}
		if(denominationArray.contains("70")){
			topUpPriceList.add(new ProductPrice("RM 70", "70"));
		}
		if(denominationArray.contains("100")){
			topUpPriceList.add(new ProductPrice("RM 100", "100"));
		}
		if(denominationArray.contains("120")){
			topUpPriceList.add(new ProductPrice("RM 120", "120"));
		}
		if(denominationArray.contains("150")){
			topUpPriceList.add(new ProductPrice("RM 150", "150"));
		}
		if(denominationArray.contains("200")){
			topUpPriceList.add(new ProductPrice("RM 200", "200"));
		}
		if(denominationArray.contains("250")){
			topUpPriceList.add(new ProductPrice("RM 250", "250"));
		}

		if(denominationArray.contains("300")){
			topUpPriceList.add(new ProductPrice("RM 300", "300"));
		}

		if(denominationArray.contains("500")){
			topUpPriceList.add(new ProductPrice("RM 500", "500"));
		}

		if(denominationArray.contains("1000")){
			topUpPriceList.add(new ProductPrice("RM 1000", "1000"));
		}




		gridview = (GridView) findViewById(R.id.gridview);
		mInternationalProductAdapter = new TopupProductAdapter_v2(mContext,topUpPriceList,denominationArray,denominationDescriptionArray,mName);

		if (mName.equalsIgnoreCase("CELCOMMAGIC"))
			mInternationalProductAdapter = new TopupProductAdapter_v2(mContext,
					SRSApp.CELCOMMAGICPriceList,denominationArray,denominationDescriptionArray,mName);

		if (mName.equalsIgnoreCase("CELCOMADDON"))
			mInternationalProductAdapter = new TopupProductAdapter_v2(mContext,
					SRSApp.CELCOMADDONPriceList,denominationArray,denominationDescriptionArray,mName);

		gridview.setAdapter(mInternationalProductAdapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				ProductPrice p = (ProductPrice) parent.getAdapter()
						.getItem(position);
				dProductPrice = p.getPriceValue();
				priceTAG = p.getPriceTag();
				insertMSISDNAlertDialog(dProductPrice, priceTAG).show();


			}
		});




		tvlain_lain.setOnClickListener(new valueOnClickListener("",""));

		productViewFlipper = (ViewFlipper) findViewById(R.id.productViewFlipper);

		tvProductName.setText(mName);


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
						if (Double.parseDouble(result.getdBalance()) < 300) {
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
						dProductPrice, sProductID, "ANDROID_Q", sClientTxID,"",
						"", "", "");
				return result;
			}

			@Override
			protected void onPostExecute(RequestInputResponse result) {
				super.onPostExecute(result);
				if(pd!=null)
					if(pd.isShowing())
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
			if (mValue.equalsIgnoreCase("")) {
				lainlainAlertDialog("").show();
			} else {
				insertMSISDNAlertDialog(mValue,tags).show();
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


	private void printReceiptInnerPrinter() {
		printDataService.setByteCommand(new byte[]{0x1b});
		printDataService.setByteCommand(new byte[]{0x4d});
		printDataService.setByteCommand(new byte[]{0x00});

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
		printDataService.send("** Topup Receipt **");
        printDataService.send("\n");
//		printDataService.send("Merchant ID :");
//		printDataService.send(FunctionUtil.countSpacing("Merchant ID :",
//				SharedPreferenceUtil.getsClientUserName()));
//		printDataService.send(SharedPreferenceUtil.getsClientUserName());
//		printDataService.send("\n");
        printDataService.send("Date:");

		String dateNow = FunctionUtil.getStrCurrentDateTime();
		printDataService.send(FunctionUtil.countSpacing2("Date:",
				dateNow));
		printDataService.send(dateNow);

		printDataService.send("\n");
		printDataService.send("Status:");
		printDataService.send(FunctionUtil.countSpacing2("Status:", "Accepted"));
		printDataService.send("Accepted");
		printDataService.send("\n");
		printDataService.send("\n");

//		printDataService.setCommand(2);
		printDataService.setCommand(22);
		printDataService.setCommand(3);
		printDataService.setCommand(4);
		printDataService.setCommand(21);

		printDataService.send(sCustomerMobileNumber);
		printDataService.send("\n");

		if(mName.indexOf("FLEXI")>0){
			printDataService.send(mName + " "
					+  dProductPrice);
		}
		else{
			printDataService.send(mName + " "
					+ "RM" + dProductPrice);
		}

		printDataService.send("\n");



		printDataService.setCommand(3);
//		printDataService.setCommand(2);
		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.send("Customer Care Line: " + Config.Custotmer_care);
		printDataService.send("\n");
		printDataService.send("9AM - 6PM (Monday - Friday)");
		printDataService.send("\n");
		printDataService.send(Config.Custotmer_website);
		printDataService.send("\n");
		printDataService.send("Thank You " + "V."+SRSApp.versionName);
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
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
		printDataService.setCommand(35);
		printDataService.setCommand(36);
		printDataService.setCommand(37);
		printDataService.send("\n");
        printDataService.send("** Topup Receipt **");

//		printDataService.send("Merchant ID :");
//		printDataService.send(FunctionUtil.countSpacing("Merchant ID :",
//				SharedPreferenceUtil.getsClientUserName()));
//		printDataService.send(SharedPreferenceUtil.getsClientUserName());
//		printDataService.send("\n");
//        printDataService.send("Date:");

        String dateNow = FunctionUtil.getStrCurrentDateTime();
        printDataService.send(FunctionUtil.countSpacing("Date:",
                dateNow));
        printDataService.send(dateNow);

        printDataService.send("\n");
        printDataService.send("Status:");
        printDataService.send(FunctionUtil.countSpacing("Status:", "Accepted"));
        printDataService.send("Accepted");
        printDataService.send("\n");
        printDataService.send("\n");

        printDataService.setCommand(2);
        printDataService.setCommand(22);
        printDataService.setCommand(3);
        printDataService.setCommand(4);
        printDataService.setCommand(21);

        printDataService.send(sCustomerMobileNumber);
        printDataService.send("\n");

		if(mName.indexOf("FLEXI")>0){
			printDataService.send(mName + " "
					+  dProductPrice);
		}
       else{
			printDataService.send(mName + " "
					+ "RM" + dProductPrice);
		}

        printDataService.send("\n");



        printDataService.setCommand(3);
        printDataService.setCommand(2);
        printDataService.send("\n");
        printDataService.setCommand(21);
        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
		printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");
        printDataService.send(Config.Custotmer_website);
		printDataService.send("\n");
		printDataService.send("Thank You " + "V."+SRSApp.versionName);
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
								if (SharedPreferenceUtil.isPrintTopup())
									if (PrintDataService.isPrinterConnected())
										if(SharedPreferenceUtil.getPrinterName().equalsIgnoreCase("InnerPrinter:")){
											printReceiptInnerPrinter();
										}else
											printReceipt();
								finish();
							}
						}).create();
	}

	private AlertDialog transcationCancelAlertDialog(String msg) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom)).setTitle(mName).setMessage(msg)
				.setCancelable(false)

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
		edMobileNo.setInputType(InputType.TYPE_CLASS_NUMBER);

		edValue = (EditText) view.findViewById(R.id.edValue);
		edValue.setInputType(InputType.TYPE_CLASS_NUMBER);
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




		final EditText input = new EditText(ProductDetailUI_v2.this);
		input.postDelayed(new Runnable() {
			@Override
			public void run() {
				InputMethodManager keyboard = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				keyboard.showSoftInput(input, 0);
			}
		}, 50);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
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

		if (mobileNo.trim().length() < MinLen || mobileNo.trim().length() > MaxLen) {
			invalidMSISDNAlertDialog(mValue).show();
			return;
		}

		DLog.e(TAG, "mValue" + mValue);
		DLog.e(TAG, "mobileNo" + mobileNo);

		tvSenderValue.setText(mobileNo);
		sCustomerMobileNumber = mobileNo;
		sCustomerAccountNumber = mobileNo;
		dProductPrice = mValue;
		tvBalance.setVisibility(View.INVISIBLE);

        tvSenderMobile.setText("Top up RM " + dProductPrice);

		productViewFlipper.showNext();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (productViewFlipper.getDisplayedChild() == 1)
				transcationCancelAlertDialog(
						"Do you want to cancel this transaction?").show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		close();
		super.onDestroy();
		if(pd!=null)
			if(pd.isShowing()){
				pd.dismiss();
			}
		pd=null;
	}

	private void close() {
//		if (PrintDataService.isPrinterConnected())
//			PrintDataService.disconnect();
	}

}

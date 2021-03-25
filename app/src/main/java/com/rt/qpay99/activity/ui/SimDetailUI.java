package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.adapter.GridViewTextAdapter;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.object.RequestPINObject;
import com.rt.qpay99.object.RequestReloadPinObject;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.Date;
import java.util.List;

public class SimDetailUI extends FragmentActivity {

	private String TAG = this.getClass().getName();
	private TextView nav_text, tvBalance;

	private ProgressDialog pd;
	private Context mContext;

	private String Denomination, mName, mkeyword;

	private TextView tvProductConfirmBuy, tvProductName, tvSenderValue,
			tvSenderMobile;
	InputFilter[] filterArray = new InputFilter[1];

	private List<String> denominationArray;

	private Button nav_btn_right;

	private RequestReloadPinObject mRequestReloadPinObject = new RequestReloadPinObject();

	private String sCustomerMobileNumber, dProductPrice, sProductID,
			sCustomerAccountNumber;

	private GridView gdPinPrice;
	private GridViewTextAdapter adapter;
	private ViewFlipper productViewFlipper;

	private PrintDataService printDataService = null;
	private ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private ImageView productImage;
	private String BILL = "";

	String format = "dd-MM-yyyy hh:mm:ss";

	RequestPINObject mRequestPINObject = new RequestPINObject();
	RTWS rtWS = new RTWS();
	String mCurrency = " RM ";
	String mRM = "";
    private double GST_Tax=0.00;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_product_sim);
		Bundle extras = getIntent().getExtras();
		Denomination = extras.getString("Denomination");
		DLog.e(TAG, "" + "" + Denomination);

		denominationArray = FunctionUtil.splitToStringList(Denomination, ",");
		// DLog.e(TAG, "" + "" + denominationArray.contains("300"));
		mName = extras.getString("Name");

        try{
            if (extras.getString("Tax") != null) {
                GST_Tax = Double.parseDouble(extras.getString("Tax"));
                DLog.e(TAG, "Tax " + GST_Tax);
            }
        }catch (Exception e){
            DLog.w(TAG, "GST_Tax Err " + e.getMessage());
        }
        GST_Tax=0.00;



		sProductID = extras.getString("pId");
		DLog.e(TAG, "" + "sProductID" + sProductID);

		nav_text = (TextView) findViewById(R.id.nav_text);
		tvBalance = (TextView) findViewById(R.id.tvBalance);


		tvSenderValue = (TextView) findViewById(R.id.tvSenderValue);
		tvSenderMobile = (TextView) findViewById(R.id.tvSenderMobile);
		tvProductName = (TextView) findViewById(R.id.tvProductName);
		tvProductConfirmBuy = (TextView) findViewById(R.id.tvProductConfirmBuy);
		productImage = (ImageView) findViewById(R.id.productImage);
		productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));
		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				close();
				finish();
			}
		});

		tvProductConfirmBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Print ticket
				printReceipt("NEW PIN");
			}
		});


		productViewFlipper = (ViewFlipper) findViewById(R.id.productViewFlipper);

		tvProductName.setText(mName);

		if (mName != null)
			nav_text.setText("NEW " + mName);

		gdPinPrice = (GridView) findViewById(R.id.gdPinPrice);
		adapter = new GridViewTextAdapter(mContext, denominationArray,GST_Tax);
		gdPinPrice.setAdapter(adapter);
		// gdPinPrice.setOnItemClickListener(new
		// PricePinOnItemClickListener(mContext));
		gdPinPrice.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				@SuppressWarnings("unchecked")
				List<String> p = (List<String>) parent.getAdapter().getItem(
						position);
				DLog.e(TAG, "gdPinPrice " + p.get(position));
				dProductPrice = p.get(position);
				mRM = "";
				mCurrency = " RM";

				sCustomerMobileNumber = SharedPreferenceUtil.getsClientUserName().substring(1,
						SharedPreferenceUtil.getsClientUserName().length());
				sCustomerAccountNumber = SharedPreferenceUtil.getsClientUserName().substring(1,
						SharedPreferenceUtil.getsClientUserName().length());

                confirmBuyAlertDialog().show();

			}
		});
		if (FunctionUtil.isSet(SRSApp.printerMacAdd))
			printDataService = new PrintDataService(mContext,
					SRSApp.printerMacAdd);

		getMemberBalance();
        retryCount=0;
		//String barcode = getBarcode("DIGIPIN", "5");
		//DLog.e(TAG, " barcode " + barcode);
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
							"Please connect printer!!!",
							Toast.LENGTH_LONG).show();
				}
			}

		}.execute();
	}

	private String getBillBody() {
		BILL = "Customer No:     " + SharedPreferenceUtil.getsClientUserName()
				+ "\n";
		BILL += "Terminal:        " + SharedPreferenceUtil.getsDeviceID()
				+ "\n";
		BILL += "Date:            " + FunctionUtil.getStrCurrentDateTime()
				+ "\n";
		BILL += "Serial No:       "
				+ mRequestReloadPinObject.getsSerialNumber() + "\n";
		BILL += "DN:              "
				+ mRequestReloadPinObject.getsDNReceivedID() + "\n";
		BILL += "Expiry Date:     " + mRequestReloadPinObject.getsExpiryDate()
				+ "\n";

		return BILL;

	}

	private void printReceipt(String sType) {

        printDataService.setCommand(21);
        printDataService.send("\n");
        printDataService.setCommand(23);
        printDataService.setCommand(4);
        printDataService.setCommand(21);
        printDataService.printImage();
        // printDataService.send(getString(R.string.app_name));
        printDataService.setCommand(4);
        printDataService.setCommand(20);
        printDataService.send("COUPON ");
        printDataService.setCommand(3);
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("** " + sType + " **");
        printDataService.send("\n");

		if (sType.equalsIgnoreCase("Re-print")) {
			printDataService.send("Date:");
			String sDate = FunctionUtil.convertString2Date(
					mRequestReloadPinObject.getsPurchaseTS(),
					"yyyy-MM-dd HH:mm:ss");
			DLog.e(TAG, "" + sDate);

			printDataService.send(countSpacing("Date:", sDate));

			printDataService.send(sDate);
			printDataService.send("\n");
		} else {
			printDataService.send("Date:");
            String dateNow = FunctionUtil.getStrCurrentDateTime();
            printDataService.send(FunctionUtil.countSpacing("Date:",
                    dateNow));
            printDataService.send(dateNow);
            printDataService.send("\n");
		}

		printDataService.send("DN:");
		printDataService.send(countSpacing("DN:",
				mRequestReloadPinObject.getsDNReceivedID()));
		printDataService.send(mRequestReloadPinObject.getsDNReceivedID());
		printDataService.send("\n");

//		printDataService.send("GST ID:");
//		printDataService.send(countSpacing("GST ID:", getTelcoGSTID(mRequestReloadPinObject.getsReloadTelco())));
//		printDataService.send(getTelcoGSTID(mRequestReloadPinObject.getsReloadTelco()));
//		printDataService.send("\n");

        printDataService.send("Serial No:");
        printDataService.send(countSpacing("Serial No:",
                mRequestReloadPinObject.getsSerialNumber()));
        printDataService.send(mRequestReloadPinObject.getsSerialNumber());
        printDataService.send("\n");
        printDataService.send("\n");
		if (Config.printerName.equalsIgnoreCase("MPT-II:")
				|| Config.printerName.equalsIgnoreCase("Imprimer:")) {
			printDataService.setCommand(21);
			printDataService.setCommand(23);
			printDataService.setCommand(24);
			printDataService.setCommand(25);
			printDataService.setCommand(26);
			printDataService.setCommand(32);
			String barCode = getBarcode(
					mRequestReloadPinObject.getsReloadTelco(),
					mRequestReloadPinObject.getsAmount());
			DLog.e(TAG, "barCode 2-" + barCode);
			if (FunctionUtil.isSet(barCode))
				printDataService.send(barCode);
			// printDataService.send("55548390001");

		}

		printDataService.setCommand(2);
		printDataService.setCommand(22);

		printDataService.setCommand(3);
		printDataService.setCommand(4);

		printDataService.setCommand(21);
		printDataService.send(mRequestReloadPinObject.getsReloadTelco() + " "
				+ "RM" + FunctionUtil.getPriceWithGST(mRequestReloadPinObject.getsAmount(),GST_Tax) );
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
			printDataService.send(mRequestReloadPinObject.getsDescription());
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.send("Customer Care Line: " + Config.Custotmer_care);
		printDataService.send("\n");
		printDataService.send("9AM - 10PM (Monday - Friday)");
		printDataService.send("\n");
		printDataService.send(Config.Custotmer_website);
        printDataService.send("\n");
        printDataService.send("Thank You");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");

	}

    private static String TELCO_GST_REG_ID = "";
    private String getTelcoGSTID(String mProduct) {
        DLog.e(TAG, "mProduct " + mProduct);
        TELCO_GST_REG_ID="";
        if (mProduct.equalsIgnoreCase("DIGIPIN")
                || mProduct.equalsIgnoreCase("DIGI")) {
            TELCO_GST_REG_ID = Config.GST_ID_DIGI;
        }

        if (mProduct.equalsIgnoreCase("MAXISPIN")
                || mProduct.equalsIgnoreCase("MAXISPIN")) {
            TELCO_GST_REG_ID = Config.GST_ID_MAXIS;
        }

        if (mProduct.equalsIgnoreCase("CELCOMPIN")
                || mProduct.equalsIgnoreCase("CELCOM")) {
            TELCO_GST_REG_ID = Config.GST_ID_MAXIS;
        }

        if (mProduct.equalsIgnoreCase("UMOBILEPIN")
                || mProduct.equalsIgnoreCase("UMOBILE")) {
            TELCO_GST_REG_ID = Config.GST_ID_UMOBILE;
        }

        if (mProduct.equalsIgnoreCase("TUNETALKPIN")
                || mProduct.equalsIgnoreCase("TUNETALK")) {
            TELCO_GST_REG_ID = Config.GST_ID_UMOBILE;
        }

        if (mProduct.equalsIgnoreCase("MECHANTRADEPIN")
                || mProduct.equalsIgnoreCase("MECHANTRADE")) {
            TELCO_GST_REG_ID = Config.GST_ID_MERCHANTRADE;
        }

        if (mProduct.equalsIgnoreCase("CLIXSTERPIN")
                || mProduct.equalsIgnoreCase("CLIXSTERP")) {
            TELCO_GST_REG_ID = Config.GST_ID_CLIXSTER;
        }

        if (mProduct.equalsIgnoreCase("XOXPIN")
                || mProduct.equalsIgnoreCase("XOX")) {
            TELCO_GST_REG_ID = Config.GST_ID_XOX;
        }

        if (mProduct.equalsIgnoreCase("MOLPIN")
                || mProduct.equalsIgnoreCase("MOL")) {
            TELCO_GST_REG_ID = Config.GST_ID_MOL;
        }

        if (mProduct.equalsIgnoreCase("ONEMYPIN")
                || mProduct.equalsIgnoreCase("ONEMY")) {

        }

        return TELCO_GST_REG_ID;

    }


    private String getBarcode(String mProduct, String mValue) {
		DLog.e(TAG, "mProduct " + mProduct);
		DLog.e(TAG, "mValue " + mValue);
		String barCode = "";

		if (mProduct.equalsIgnoreCase("DIGIPIN")
				|| mProduct.equalsIgnoreCase("DIGI")) {

            TELCO_GST_REG_ID = Config.GST_ID_DIGI;

			if (mValue.equalsIgnoreCase("5"))
				return Config.DIGI_RM5;

			if (mValue.equalsIgnoreCase("10"))
				return Config.DIGI_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.DIGI_RM30;

			if (mValue.equalsIgnoreCase("50"))
				return Config.DIGI_RM50;

			if (mValue.equalsIgnoreCase("100"))
				return Config.DIGI_RM100;
		}

		if (mProduct.equalsIgnoreCase("MAXISPIN")
				|| mProduct.equalsIgnoreCase("MAXISPIN")) {

            TELCO_GST_REG_ID = Config.GST_ID_MAXIS;

			if (mValue.equalsIgnoreCase("5"))
				return Config.MAXIS_RM5;

			if (mValue.equalsIgnoreCase("10"))
				return Config.MAXIS_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.MAXIS_RM30;

			if (mValue.equalsIgnoreCase("100"))
				return Config.MAXIS_RM100;
		}

		if (mProduct.equalsIgnoreCase("CELCOMPIN")
				|| mProduct.equalsIgnoreCase("CELCOM")) {

            TELCO_GST_REG_ID = Config.GST_ID_MAXIS;

			if (mValue.equalsIgnoreCase("5"))
				return Config.CELCOM_RM5;

			if (mValue.equalsIgnoreCase("10"))
				return Config.CELCOM_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.CELCOM_RM30;

			if (mValue.equalsIgnoreCase("50"))
				return Config.CELCOM_RM50;

			if (mValue.equalsIgnoreCase("100"))
				return Config.CELCOM_RM100;
		}

		if (mProduct.equalsIgnoreCase("UMOBILEPIN")
				|| mProduct.equalsIgnoreCase("UMOBILE")) {

            TELCO_GST_REG_ID = Config.GST_ID_UMOBILE;

			if (mValue.equalsIgnoreCase("10"))
				return Config.UMOBILE_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.UMOBILE_RM30;

			if (mValue.equalsIgnoreCase("50"))
				return Config.UMOBILE_RM50;

		}

		if (mProduct.equalsIgnoreCase("TUNETALKPIN")
				|| mProduct.equalsIgnoreCase("TUNETALK")) {

            TELCO_GST_REG_ID = Config.GST_ID_UMOBILE;

			if (mValue.equalsIgnoreCase("10"))
				return Config.TUNETALK_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.TUNETALK_RM30;

			if (mValue.equalsIgnoreCase("50"))
				return Config.TUNETALK_RM50;

		}

		if (mProduct.equalsIgnoreCase("MECHANTRADEPIN")
				|| mProduct.equalsIgnoreCase("MECHANTRADE")) {

            TELCO_GST_REG_ID = Config.GST_ID_MERCHANTRADE;

			if (mValue.equalsIgnoreCase("5"))
				return Config.MERCHANTRADE_RM5;

			if (mValue.equalsIgnoreCase("10"))
				return Config.MERCHANTRADE_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.MERCHANTRADE_RM30;

		}

		if (mProduct.equalsIgnoreCase("CLIXSTERPIN")
				|| mProduct.equalsIgnoreCase("CLIXSTERP")) {

            TELCO_GST_REG_ID = Config.GST_ID_CLIXSTER;

			if (mValue.equalsIgnoreCase("10"))
				return Config.CLIXSTER_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.CLIXSTER_RM30;

		}

		if (mProduct.equalsIgnoreCase("XOXPIN")
				|| mProduct.equalsIgnoreCase("XOX")) {

            TELCO_GST_REG_ID = Config.GST_ID_XOX;

			if (mValue.equalsIgnoreCase("5"))
				return Config.XOX_RM5;

			if (mValue.equalsIgnoreCase("10"))
				return Config.XOX_RM10;

			if (mValue.equalsIgnoreCase("30"))
				return Config.XOX_RM30;

			if (mValue.equalsIgnoreCase("50"))
				return Config.XOX_RM50;

		}

		if (mProduct.equalsIgnoreCase("MOLPIN")
				|| mProduct.equalsIgnoreCase("MOL")) {

            TELCO_GST_REG_ID = Config.GST_ID_MOL;

			if (mValue.equalsIgnoreCase("10"))
				return Config.MOL_RM10;

			if (mValue.equalsIgnoreCase("20"))
				return Config.MOL_RM20;

			if (mValue.equalsIgnoreCase("30"))
				return Config.MOL_RM30;

			if (mValue.equalsIgnoreCase("50"))
				return Config.MOL_RM50;

			if (mValue.equalsIgnoreCase("100"))
				return Config.MOL_RM100;

		}

		if (mProduct.equalsIgnoreCase("ONEMYPIN")
				|| mProduct.equalsIgnoreCase("ONEMY")) {
			return Config.ONEMYPIN;

		}

		return "";

	}
    private int retryCount = 0;
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

				DLog.e(TAG, "sCustomerAccountNumber : "
						+ sCustomerAccountNumber);
				DLog.e(TAG, "sCustomerMobileNumber :  " + sCustomerMobileNumber);
				DLog.e(TAG, "dProductPrice  : " + dProductPrice);
				DLog.e(TAG, "sProductID : " + sProductID);
				// RequestInputResponse result = rt.RequestInput(
				// sCustomerAccountNumber, sCustomerMobileNumber,
				// dProductPrice, sProductID, "ANDROID",
				// FunctionUtil.getStringDateTimeSec(), "", "", "");
				mRequestPINObject = new RequestPINObject();
				mRequestPINObject.setsClientUserName(SharedPreferenceUtil
						.getsClientUserName());
				mRequestPINObject.setsClientPassword(SharedPreferenceUtil
						.getsClientPassword());
				mRequestPINObject
						.setsCustomerAccountNumber(sCustomerAccountNumber);
				mRequestPINObject
						.setsCustomerMobileNumber(sCustomerMobileNumber);
				mRequestPINObject.setdProductPrice(dProductPrice);
				mRequestPINObject.setsProductID(sProductID);
				mRequestPINObject.setsRemark(Config.OS_PLATFORM);
				mRequestPINObject.setsResponseID(FunctionUtil
						.getStringDateTimeSec());

				return rtWS.RequestInput(sCustomerAccountNumber,
						sCustomerMobileNumber, dProductPrice, sProductID,
						"ANDROID_Q", FunctionUtil.getStringDateTimeSec(), "","",
						"", "");

			}

			@Override
			protected void onPostExecute(RequestInputResponse result) {
				super.onPostExecute(result);
				pd.dismiss();
				if (result != null) {
					if (Config.WS_SUBMIT_SUCCESS.equalsIgnoreCase(result
							.getsResponseStatus())) {
						DLog.e(TAG, "" + result.getsResponseID());
						DLog.e(TAG, "" + result.getsResponseStatus());
                        transcationSuccessAlertDialog("Transaction SUCCESS",result.getsResponseStatus()).show();
						return;
					}
				}
				if (result.getsResponseStatus() != null) {
					if ("SUBMIT_INSUFFICIENT_BALANCE".equalsIgnoreCase(result
							.getsResponseStatus())) {
						transcationFailedAlertDialog(
								"INSUFFICIENT BALANCE, Please topup.").show();
						return;
					}
					transcationFailedAlertDialog(
							result.getsResponseStatus()
									+ ". Please try again later.").show();
					return;
				}
				transcationFailedAlertDialog(
						"Transaction Failed. Please try again later.").show();

			}
		}.execute();

	}

	private void getGetReloadPIN(final String sClientUserName,
			final String sClientPassword, final String sLocalMOID) {
		new AsyncTask<Void, Void, RequestReloadPinObject>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Pin requesting ...");
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
			protected RequestReloadPinObject doInBackground(Void... params) {

				DLog.e(TAG, "sCustomerAccountNumber : "
						+ sCustomerAccountNumber);
				DLog.e(TAG, "sCustomerMobileNumber :  " + sCustomerMobileNumber);
				DLog.e(TAG, "sLocalMOID  : " + sLocalMOID);
				return rtWS.getReloadPIN(sClientUserName, sClientPassword,
						sLocalMOID);

			}

			@Override
			protected void onPostExecute(RequestReloadPinObject result) {
				super.onPostExecute(result);
				pd.dismiss();
				mRequestReloadPinObject = result;

				if (result.isGetReloadPINResult()) {
					if (result.getsReloadPin() != null) {
						DLog.e(TAG, "" + result.getsReloadPin());
						DLog.e(TAG, "" + result.getsSerialNumber());
						DLog.e(TAG, "" + result.getsDNReceivedID());
						DLog.e(TAG, "" + result.getsAmount());
						DLog.e(TAG, "" + result.getsReloadTelco());
						if (PrintDataService.isPrinterConnected())
							printReceipt("Re-print");
						else
							transcationFailedAlertDialog(
									"Printer connecting failed!!!!").show();
						return;
					}
				}

				transcationFailedAlertDialog(
						"Transaction Failed. Please try again later.").show();

			}
		}.execute();

	}

	private boolean GetReloadPINImmediateResult = false;

	private void GetReloadPINImmediate(final String sClientUserName,
			final String sClientPassword, final String sLocalMOID) {
		new AsyncTask<Void, Void, RequestReloadPinObject>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Pin requesting ...");
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
			protected RequestReloadPinObject doInBackground(Void... params) {

				DLog.e(TAG, "sCustomerAccountNumber : "
						+ sCustomerAccountNumber);
				DLog.e(TAG, "sCustomerMobileNumber :  " + sCustomerMobileNumber);
				DLog.e(TAG, "sLocalMOID  : " + sLocalMOID);
				GetReloadPINImmediateResult = false;
				return rtWS.GetReloadPINImmediate(sClientUserName,
						sClientPassword, sLocalMOID);

			}

			@Override
			protected void onPostExecute(RequestReloadPinObject result) {
				super.onPostExecute(result);
				pd.dismiss();
				mRequestReloadPinObject = result;

				if (result.isGetReloadPINResult()) {
					if (result.getsReloadPin() != null) {
						DLog.e(TAG, "" + result.getsReloadPin());
						DLog.e(TAG, "" + result.getsSerialNumber());
						GetReloadPINImmediateResult = true;
						if (PrintDataService.isPrinterConnected())
							printReceipt("NEW PIN");
						else
							transcationFailedAlertDialog(
									"Printer connecting failed!!!!").show();
						return;
					}
				}

                if(retryCount==0){
                    retryCount=1;
                    DLog.e(TAG,"GetReloadPINImmediate RETRY ======================> 1");
                    GetReloadPINImmediate(
                            sClientUserName,
                            sClientPassword,
                            sLocalMOID);
                }else{
                    transcationFailedAlertDialog(
                            "Transaction Failed. Please try again later.").show();

                }
			}
		}.execute();

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

	private AlertDialog transcationSuccessAlertDialog(String msg,final String LocalMOID) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
                                Intent intentEmail = new Intent(
                                        Intent.ACTION_SEND);
                                intentEmail.putExtra(Intent.EXTRA_EMAIL,
                                        new String[] { Config.EMAIL_ADD1,
                                                Config.EMAIL_ADD2 });
                                // intentEmail.putExtra(Intent.EXTRA_BCC,
                                // new String[] { Config.EMAIL_ADD_BCC });
                                intentEmail.putExtra(Intent.EXTRA_SUBJECT,
                                        mName + " NEW SIM PACK REQUEST");
                                intentEmail.putExtra(Intent.EXTRA_TEXT, uName
                                        + "\n" + uAddress
                                        + "\n" + uContactNo
                                        + "\n" + " LocalMoID " + LocalMOID
                                        + "\n" + new Date());
                                intentEmail.setType("message/rfc822");
                                startActivity(Intent.createChooser(intentEmail,
                                        "Choose an email provider :"));
							}
						}).create();
	}

	private AlertDialog connectPrinterAlertDialog() {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + " - No printer connected!!")
				.setMessage("Please check printer connection!!!")
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

    private String uName,uContactNo,uAddress;
	private AlertDialog confirmBuyAlertDialog() {
        View view = getLayoutInflater().inflate(
                R.layout.adapter_layout_new_sim, null);
        final EditText edName, edContactNo,edAddress;
        edName = (EditText) view.findViewById(R.id.edName);
        edContactNo = (EditText) view.findViewById(R.id.edContactNo);
        edAddress = (EditText) view.findViewById(R.id.edAddress);



		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + mCurrency + FunctionUtil.PriceRoundUp(Double.parseDouble(FunctionUtil.getPriceWithGST(dProductPrice,GST_Tax))) + mRM)
				.setMessage(mName + " SIM akan hantar dengan POS LAJU \n Sila masukkan maklumat yang betul")
				.setCancelable(false)
                .setView(view)
				.setPositiveButton("HANTAR",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
                                final String mName = edName.getText()
                                        .toString();
                                final String mContactNo = edContactNo.getText()
                                        .toString();
                                final String mAddress = edAddress.getText()
                                        .toString();
                                uName = mName;
                                uContactNo = mContactNo;
                                uAddress = mAddress;
                                if(!FunctionUtil.isSet(mName)){
                                    transcationFailedAlertDialog("Sila masukkan name penuh").show();
                                    return;
                                }
                                if(!FunctionUtil.isSet(mContactNo)){
                                    transcationFailedAlertDialog("Sila masukkan nombor telefon").show();
                                    return;
                                }
                                if(!FunctionUtil.isSet(mAddress)){
                                    transcationFailedAlertDialog("Sila masukkan alamat surat-menyurat").show();
                                    return;
                                }

								RequestInputAsync();

							}
						}).setNegativeButton(R.string.title_cancel, null)

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
				R.layout.view_layout_insert_mobile_no, null);
		final EditText edMobileNo, edValue;
		edMobileNo = (EditText) view.findViewById(R.id.edMobileNo);
		edMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);
		edValue = (EditText) view.findViewById(R.id.edValue);
		edValue.setInputType(InputType.TYPE_CLASS_PHONE);

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

	private AlertDialog insertMSISDNAlertDialog(final String mValue) {
		final EditText input = new EditText(SimDetailUI.this);
		input.setInputType(InputType.TYPE_CLASS_PHONE);
		filterArray[0] = new InputFilter.LengthFilter(10);
		input.setFilters(filterArray);

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName + " - RM" + mValue)
				.setView(input)
				.setMessage(R.string.telefon_no)
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

		if (mobileNo.trim().length() < 8) {
			invalidMSISDNAlertDialog(mValue).show();
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
                transcationFailedAlertDialog(
						"Do you want to cancel this transaction?").show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DLog.e(TAG, "onResume");
	}

	private void close() {
		// if (PrintDataService.isPrinterConnected())
		// PrintDataService.disconnect();
	}

	private String countSpacing(String name, String total) {
		String space = "";
		if (total == null)
			total = "";
		// if (FunctionUtil.isSet(name)) {
		// if (FunctionUtil.isSet(total)) {
		int totalSpacing = 41;
		int cLen = name.length() + total.length();
		int newLen = totalSpacing - cLen;

		for (int i = 0; i <= newLen; i++) {
			space = space + " ";
		}
		// }

		// }

		return space;
	}

}

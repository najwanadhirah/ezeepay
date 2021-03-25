package com.rt.qpay99.activity.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.MMExChangeRateResult;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IDMoneyPinUI extends FragmentActivity {

	private String TAG = this.getClass().getName();
	private TextView nav_text, tvBalance;
	RTWS rtWS = new RTWS();
	private ProgressDialog pd;
	private Context mContext;
	private String Denomination, mName, mkeyword;
	private ImageView productImage;
	private List<String> denominationArray;
	private String sCustomerMobileNumber, dProductPrice, sProductID,
			sCustomerAccountNumber;

	private BroadcastReceiver mSMSReceiver;

	private EditText edMYCurrency, edXChangeRate, edIDCurrency, edMMId;

	private int RM, XChange, Rp;
	private Button btnSendSMS, btnRegister;
	private Button nav_btn_right, nav_btn_left;
	private TextView tvXChangeRate, tvMMAppDL, tvXChangeRateDate;

	private long enqueue;
	private DownloadManager dm;
	int seconds = 0;
	private LinearLayout llMMAppDL;
	private boolean isPinKeluar = false;
	private Spinner spinnerGatewayNo;

	private int MaxValue = 0;
	private boolean isMY = false;
	private boolean isRp = false;
	List<String> gatewayList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_money_pin);
		Bundle extras = getIntent().getExtras();
		Denomination = extras.getString("Denomination");
		DLog.e(TAG, "" + "" + Denomination);
		denominationArray = FunctionUtil.splitToStringList(Denomination, ",");
		// DLog.e(TAG, "" + "" + denominationArray.contains("300"));
		mName = extras.getString("Name");
		sProductID = extras.getString("pId");
		DLog.e(TAG, "" + "sProductID" + sProductID);

		nav_btn_left = (Button) findViewById(R.id.nav_btn_left);
		nav_btn_left.setText("PIN Batal");
		nav_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PinBatalAlertDialog().show();
			}
		});

		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		RM = 1;
		isMY = true;
		XChange = 3600;

		llMMAppDL = (LinearLayout) findViewById(R.id.llMMAppDL);

		productImage = (ImageView) findViewById(R.id.productImage);
		productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));

		edMYCurrency = (EditText) findViewById(R.id.edMYCurrency);
		edXChangeRate = (EditText) findViewById(R.id.edXChangeRate);
		edIDCurrency = (EditText) findViewById(R.id.edIDCurrency);

		tvXChangeRateDate = (TextView) findViewById(R.id.tvXChangeRateDate);
		tvXChangeRate = (TextView) findViewById(R.id.tvXChangeRate);
		edMMId = (EditText) findViewById(R.id.edMMId);

		edMYCurrency.setText(String.valueOf(RM));
		edXChangeRate.setText(String.valueOf(XChange));
		// edIDCurrency.setText(String.valueOf(Rp));
		edMYCurrency.requestFocus();

		edMYCurrency.addTextChangedListener(XChangeRateWatcher);
		edMYCurrency.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					isMY = true;
					DLog.e(TAG, "edMYCurrency hasFocus");
				} else {
					isMY = false;
					DLog.e(TAG, "edMYCurrency unfocus");
				}
			}
		});

		edIDCurrency.addTextChangedListener(RpXChangeRateWatcher);
		edIDCurrency.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					isRp = true;
					DLog.e(TAG, "edIDCurrency hasFocus");
				} else {
					isRp = false;
					DLog.e(TAG, "edIDCurrency unfocus");
				}
			}
		});
		edXChangeRate.addTextChangedListener(XChangeRateWatcher);

		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i;
				PackageManager manager = getPackageManager();
				try {
					i = manager.getLaunchIntentForPackage("com.gracker.mmapp");
					if (i == null)
						throw new PackageManager.NameNotFoundException();
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					startActivity(i);
				} catch (PackageManager.NameNotFoundException e) {

				}
			}

		});

		tvMMAppDL = (TextView) findViewById(R.id.tvMMAppDL);
		llMMAppDL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				File direct = new File(Environment
						.getExternalStorageDirectory() + "/QPay99");

				if (!direct.exists()) {
					direct.mkdirs();
				}

				Calendar c = Calendar.getInstance();
				seconds = c.get(Calendar.SECOND);
				DLog.e(TAG, "seconds " + seconds);

				dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
				Request request = new Request(Uri
						.parse("http://qpay99.com/MMApp.v5.apk"));
				request.setDestinationInExternalPublicDir("/QPay99", "MMApp"
						+ seconds + ".apk");
				enqueue = dm.enqueue(request);
			}

		});

		btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		btnSendSMS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					String amount = "";
					String kadNumber = "";
					String Rupiah = "";

					int isValid = validateText();

					if (isValid == VALID) {

					} else if (isValid == INVALID_CURRENCY) {
						mAlertDialog("Alert!!!", "Sila masuk Ringgit Malaysia")
								.show();
						return;
					} else if (isValid == INVALID_MMID) {
						mAlertDialog("Alert!!!",
								"Sila masuk Kad Ahli yang sah!!!").show();
						return;
					} else {

					}

					kadNumber = edMMId.getText().toString();
					amount = edMYCurrency.getText().toString();
					Rupiah = edIDCurrency.getText().toString();

					ConfirmationAlertDialog(amount, "", kadNumber).show();
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		});

		spinnerGatewayNo = (Spinner) findViewById(R.id.spinnerGatewayNo);

//		if (Config.isDebug)
//			gatewayList.add("0166572577");
		gatewayList.add("0124283123");
		gatewayList.add("0123676123");
		gatewayList.add("0125682123");
		gatewayList.add("0124702123");
		gatewayList.add("0162282008");
		gatewayList.add("0162613155");
		gatewayList.add("0162262269");
		gatewayList.add("0162107833");
		gatewayList.add("0192336123");
		gatewayList.add("0196536123");
		gatewayList.add("0193336123");
		gatewayList.add("0193866123");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, gatewayList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGatewayNo.setAdapter(dataAdapter);

		getExchangeRateAsync(1);
		calculation(RM, XChange);

		if (FunctionUtil.isSet(SRSApp.printerMacAdd))
			printDataService = new PrintDataService(mContext,
					SRSApp.printerMacAdd);
	}

	private static int INVALID_MMID = 1;
	private static int INVALID_CURRENCY = 2;
	private static int INVALID = 3;
	private static int VALID = 0;

	private void sendSMS() {

		final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
				.setTitle("Sila tunggu...").setMessage("Please wait 60");

		try {
			String amount = "";
			String kadNumber = "";

			int isValid = validateText();

			if (isValid == VALID) {

			} else if (isValid == INVALID_CURRENCY) {
				mAlertDialog("Alert!!!", "Sila masuk Ringgit Malaysia").show();
				return;
			} else if (isValid == INVALID_MMID) {
				mAlertDialog("Alert!!!", "Sila masuk Kad Ahli yang sah!!!")
						.show();
				return;
			} else {

			}

			kadNumber = edMMId.getText().toString();
			amount = edMYCurrency.getText().toString();
			String msg = "pinkeluar#" + amount + "#" + kadNumber;

			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(spinnerGatewayNo.getSelectedItem()
					.toString(), null, msg, null, null);

			InsertMobileMobilemessageAsync(msg);
			dialog.setPositiveButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});
			final AlertDialog alert = dialog.create();
			alert.show();
			alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			new CountDownTimer(3000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					alert.setMessage("Please wait "
							+ (millisUntilFinished / 1000));
					alert.getButton(AlertDialog.BUTTON_POSITIVE).setText(
							"Please wait " + (millisUntilFinished / 1000));
				}

				@Override
				public void onFinish() {
					// info.setVisibility(View.GONE);
					// alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(
					// true);
					alert.dismiss();

				}
			}.start();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private int validateText() {
		try {

			if (edMYCurrency.getText().length() == 0) {
				return INVALID_CURRENCY;
			}

			if (edMMId.getText().toString() == "1") {
				return INVALID_CURRENCY;
			}

			if (edMMId.getText().length() < 10) {
				return INVALID_MMID;
			}

			return VALID;
		} catch (Exception ex) {
			return INVALID;
		}

	}

	private AlertDialog mAlertDialog(String mTitle, String msg) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mTitle)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

	private AlertDialog ConfirmMobileMoneyAlertDialog(final String msg,
			final String senderNum) {

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle("IDMONEYPIN")
				.setMessage(msg)
				.setCancelable(false)
				.setNegativeButton("Cancel", null)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								String[] msgs = msg.split(":");
								if (msgs.length > 1)
									sendSMS(msgs[1], senderNum);
							}
						}).create();
	}

	private void sendSMS(final String msg, final String mobile) {
		try {
			DLog.e(TAG, "msg " + msg);
			DLog.e(TAG, "mobile " + mobile);
			String[] msgs = msg.split(" ");
			DLog.e(TAG, "msgs[1] " + msgs[1]);
			DLog.e(TAG, "msgs[2] " + msgs[2]);
			SmsManager smsManager = SmsManager.getDefault();
			String newMsg = msgs[1] + " " + msgs[2];
			smsManager.sendTextMessage(mobile, null, newMsg, null, null);
			Toast toast = Toast.makeText(mContext, "mobile: " + mobile
					+ ", message: " + newMsg, Toast.LENGTH_LONG);
			toast.show();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private AlertDialog ConfirmationAlertDialog(String RM, String RP,
			String MMId) {
		View view = getLayoutInflater().inflate(
				R.layout.adapter_layout_mm_detail_confirmation, null);
		final TextView tvMMRM, tvMMRP, tvMMId;
		tvMMRM = (TextView) view.findViewById(R.id.tvMMRM);
		tvMMId = (TextView) view.findViewById(R.id.tvMMId);
		tvMMRP = (TextView) view.findViewById(R.id.tvMMRP);

		tvMMRM.setText(edMYCurrency.getText());
		tvMMRP.setText(edIDCurrency.getText());
		tvMMId.setText(edMMId.getText());

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(mName)
				.setView(view)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								isPinKeluar = true;
								sendSMS();
								btnSendSMS.setEnabled(false);
							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	TextWatcher XChangeRateWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			if (isMY) {

				if (!edMYCurrency.getText().toString().equals("")) {
					DLog.e(TAG, "" + edMYCurrency.getText().toString());
					if (Integer.parseInt(edMYCurrency.getText().toString()) > 3001) {
						edMYCurrency.setText(String.valueOf(3000));
					}
					if (!edXChangeRate.getText().toString().equals("")) {
						calculation(Integer.parseInt(edMYCurrency.getText()
								.toString()), Integer.parseInt(edXChangeRate
								.getText().toString()));
					}

					return;
				}

			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Do something or nothing.
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// Do something or nothing
		}
	};

	TextWatcher RpXChangeRateWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			if (isRp) {

				if (!edIDCurrency.getText().toString().equals("")) {
					DLog.e(TAG, "" + edIDCurrency.getText().toString());
					if (Integer.parseInt(edIDCurrency.getText().toString()) > MaxValue) {
						edIDCurrency.setText(String.valueOf(MaxValue));
					}
					if (!edXChangeRate.getText().toString().equals("")) {
						RpCalculation(Integer.parseInt(edIDCurrency.getText()
								.toString()), Integer.parseInt(edXChangeRate
								.getText().toString()));
					}

					return;
				} else {

					// edMYCurrency.setText("1");
				}

			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Do something or nothing.
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// Do something or nothing
		}
	};

	private void calculation(int value, int exchangeRate) {
		if (isMY) {
			int mValue = value * exchangeRate;
			DLog.e(TAG, "edIDCurrency " + mValue);
			edIDCurrency.setText(String.valueOf(mValue));
		}

	}

	private void RpCalculation(int value, int exchangeRate) {
		if (isRp) {
			if (value == 0)
				return;
			if (exchangeRate == 0)
				return;
			int mValue = value / exchangeRate;
			DLog.e(TAG, "edMYCurrency " + mValue);
			edMYCurrency.setText(String.valueOf(mValue));
		}

	}

	private void InsertMobileMobilemessageAsync(final String msg) {
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					String sDate = new Date().toString();

					rtWS.InsertMobileMobilemessage("", "", "PIN SEND", msg,
							sDate, String.valueOf(Config.sClientID),
							SharedPreferenceUtil.getsClientUserName());
				} catch (Exception ex) {

				}
				return true;
			}

		}.execute();
	}

	BroadcastReceiver mDownloadCompletedReceiver;

	private void downloadCompletedReceiver() {
		if (mDownloadCompletedReceiver == null) {
			mDownloadCompletedReceiver = new BroadcastReceiver() {

				@SuppressLint("SdCardPath")
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					DLog.e(TAG, "DOWNLOAD COMPLETED");
					try {
						File direct = new File(
								Environment.getExternalStorageDirectory()
										+ "/sdcard/QPay99/MMApp.apk");
						Intent install = new Intent(Intent.ACTION_VIEW);
						install.setDataAndType(
								Uri.fromFile(new File("/sdcard/QPay99"
										+ "/MMApp" + seconds + ".apk")),
								"application/vnd.android.package-archive");
						startActivity(install);
					} catch (Exception ex) {
						DLog.e(TAG, "" + ex.getMessage());
					}
				}

			};

			registerReceiver(mDownloadCompletedReceiver, new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		}
	}

	String mTid = "";
	String mBal = "";
	String mValue = "";
	String mMMName = "";
	String mMMSerial = "";
	String[] mMsg;
	String[] mMsg1;
	String mKadNumber = "";

	private void SMSReceivedBroadcastReceiver() {
		if (mSMSReceiver == null) {
			mSMSReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					DLog.e(TAG, "RECEIVED SMSES");
					// Retrieves a map of extended data from the intent.
					final Bundle bundle = intent.getExtras();

					try {

						if (bundle != null) {

							final Object[] pdusObj = (Object[]) bundle
									.get("pdus");

							for (int i = 0; i < pdusObj.length; i++) {

								SmsMessage currentMessage = SmsMessage
										.createFromPdu((byte[]) pdusObj[i]);
								String phoneNumber = currentMessage
										.getDisplayOriginatingAddress();

								String senderNum = phoneNumber;
								String message = currentMessage
										.getDisplayMessageBody();

								DLog.i("SmsReceiver", "senderNum: " + senderNum
										+ "; message: " + message);

								if (message.trim().toLowerCase()
										.indexOf("kad ahli tidah sah") > -1) {
									DLog.e(TAG,
											"kad ahli tidah sah ==========================");
									mAlertDialog("Kad ahli tidak sah",
											"Pelanggan yang tidak ada kad ahli sila daftar sebelum hantar.")
											.show();
									return;
								}

								if (message.trim().indexOf("Remit MoneyPIN,") > -1) {
									DLog.e(TAG,
											"kad ahli tidah sah ==========================");
									mAlertDialog("Remit MoneyPIN",
											"Minimum RM5.00").show();
									return;
								}

								// MoneyPIN R5413, RM5.00 telah sms ke chew kim
								// wei 0166572577. TID:68743284. Merchant
								// Bal:90.00
								// mMsg[0] MoneyPIN R5413, RM5
								// mMsg[1] 00 telah sms ke chew kim wei
								// 0166572577
								// mMsg[2] TID:68743284
								// mMsg[3] Merchant Bal:90

								// confirmation msg
								if (message.trim().indexOf("Merchant Bal") > -1) {

									mMsg = message.split("\\.");
									DLog.e(TAG, "mMsg.length " + mMsg.length);

									DLog.e(TAG, "mMsg[0] " + mMsg[0]);
									DLog.e(TAG, "mMsg[1] " + mMsg[1]);
									DLog.e(TAG, "mMsg[2] " + mMsg[2]);
									DLog.e(TAG, "mMsg[3] " + mMsg[3]);

									mValue = mMsg[0].replace("MoneyPIN", "");
									mMsg1 = mValue.split("\\,");

									mMMSerial = mMsg1[0].replace(" ", "");
									DLog.e(TAG, "mMMSerial " + mMMSerial);

									mValue = mMsg1[1].replace(" ", "");
									DLog.e(TAG, "mValue " + mValue);

									mMMName = mMsg[1].replace(" telah sms ke ",
											"");
									mMMName = mMMName.substring(2,
											mMMName.length());
									DLog.e(TAG, "mMMName " + mMMName);

									mTid = "TID" + mMsg[2].replace("TID:", "");
									DLog.e(TAG, "mTid" + mTid);

									mBal = mMsg[3].replace("Merchant Bal:", "");
									DLog.e(TAG, "mBal " + mBal);

									if (edMMId.getText() != null)
										mKadNumber = edMMId.getText()
												.toString();
									DLog.e(TAG, "mKadNumber " + mKadNumber);

									printReceipt("Money PIN");
									mAlertDialog("Remit MoneyPIN", message)
											.show();
									return;
								}

								// MM Pin Batal
								if (message.trim().indexOf(
										"telah dibatalkan dan wang telah") > -1) {
									mAlertDialog("Remit MoneyPIN", message)
											.show();
									return;
								}
								
								// MM Pin Batal
								if (message.trim().indexOf(
										"tidak dapat dibatalkan.") > -1) {
									mAlertDialog("Remit MoneyPIN", message)
											.show();
									return;
								}

								// MM akan keluar MoneyPIN
								if (message.trim().indexOf(
										"MM akan keluar MoneyPIN") > -1) {
									ConfirmMobileMoneyAlertDialog(message,
											senderNum).show();
									return;
								}

							}
						}

					} catch (Exception e) {
						Log.e("SmsReceiver", "Exception smsReceiver" + e);

					}

				}
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.provider.Telephony.SMS_RECEIVED");

			filter.setPriority(1000);

			mContext.registerReceiver(mSMSReceiver, filter);
		}

	}

	protected void unregisterSMSReceivedReceiver() {
		if (mSMSReceiver != null) {
			try {
				this.unregisterReceiver(mSMSReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mSMSReceiver = null;
		}
	}

	protected void unregisterDownloadCompletedReceiver() {
		if (mDownloadCompletedReceiver != null) {
			try {
				this.unregisterReceiver(mDownloadCompletedReceiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mDownloadCompletedReceiver = null;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SMSReceivedBroadcastReceiver();
		downloadCompletedReceiver();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unregisterSMSReceivedReceiver();
		unregisterDownloadCompletedReceiver();
	}

	private boolean isServerRate = false;

	private void getExchangeRateAsync(final int mId) {
		new AsyncTask<Void, Void, MMExChangeRateResult>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);

				SpannableString ss1 = new SpannableString(
						"Connecting Server ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				pd.show();
			}

			@Override
			protected MMExChangeRateResult doInBackground(Void... params) {
				try {
					// String sDate = new Date().toString();
					return rtWS.GetExChangeRateById("", "", mId);
				} catch (Exception ex) {

				}
				return null;

			}

			@Override
			protected void onPostExecute(MMExChangeRateResult result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pd.dismiss();
				if (result != null) {
					try {
						if (result.getmID() > 0) {
							isServerRate = true;
							edXChangeRate.setText(result.getForeignAmount());
							tvXChangeRate.setText("RM1=Rp. "
									+ result.getForeignAmount());

							MaxValue = Integer.valueOf(edXChangeRate.getText()
									.toString());
							tvXChangeRateDate.setText("Pembaruan terakhir : "
									+ result.getLastUpdated().toString());
							DLog.e(TAG, " MaxValue ===================="
									+ MaxValue);

							MaxValue = MaxValue * 3000;
							edIDCurrency.setText(MaxValue);

						}
					} catch (Exception ex) {
						DLog.e(TAG, "" + ex.getMessage());
					}
				}

			}

		}.execute();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isPinKeluar) {
			DLog.e(TAG, "onKeyDown ");
			exitAlertDialog("Sila tunggu").show();
		}

		return super.onKeyDown(keyCode, event);
	}

	private AlertDialog exitAlertDialog(String msg) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(R.string.app_name)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								finish();
							}
						}).setNegativeButton(R.string.title_cancel, null)
				.create();
	}

	private PrintDataService printDataService = null;

	private void printReceipt(String sType) {
		printDataService.setCommand(21);
		printDataService.send("\n");
		printDataService.setCommand(23);
		printDataService.setCommand(4);
		printDataService.setCommand(21);
//		printDataService.printImage();
		printDataService.send(getString(R.string.app_name));
		printDataService.setCommand(3);
		printDataService.send("\n");
		printDataService.send("** " + sType + " **");
		printDataService.setCommand(2);
		printDataService.setCommand(20);
		printDataService.send("\n");

		printDataService.send("Agent No:");
		printDataService.send(FunctionUtil.countSpacing("Agent No:",
				SharedPreferenceUtil.getsClientUserName()));
		printDataService.send(SharedPreferenceUtil.getsClientUserName());
		printDataService.send("\n");

		printDataService.send("Terminal:");
		printDataService.send(FunctionUtil.countSpacing("Terminal:",
				SharedPreferenceUtil.getsDeviceID()));
		printDataService.send(SharedPreferenceUtil.getsDeviceID());
		printDataService.send("\n");

		printDataService.send("Date:");
		printDataService.send(FunctionUtil.countSpacing("Date:",
				FunctionUtil.getStrCurrentDateTime()));
		printDataService.send(FunctionUtil.getStrCurrentDateTime());
		printDataService.send("\n");

		printDataService.send("Printer Code:");
		printDataService.send(FunctionUtil.countSpacing("Printer Code:",
				Config.printerId));
		printDataService.send(Config.printerId);
		printDataService.send("\n");

		if (mKadNumber.length() > 1) {
			printDataService.send("Card No:");
			printDataService.send(FunctionUtil.countSpacing("Card No:",
					mKadNumber));
			printDataService.send(mKadNumber);
			printDataService.send("\n");
		}

		printDataService.setCommand(2);
		printDataService.setCommand(22);

		printDataService.setCommand(3);
		printDataService.setCommand(4);

		printDataService.setCommand(21);
		printDataService.send("\n");
		printDataService.send("MONEYPIN" + " " + mValue);
		printDataService.send("\n");

		printDataService.setCommand(20);
		printDataService.send("PIN :");
		// printDataService.send("\n");
		printDataService.send(mTid);
		printDataService.setCommand(3);
		printDataService.send("\n");

		printDataService.setCommand(2);
		printDataService.send("");
		printDataService.send("\n");

		printDataService.send(mValue + " telah SMS Ke");
		printDataService.send("\n");
		printDataService.send(mMMName);
		printDataService.send("\n");

		printDataService.send("MoneyPIN " + mMMSerial);
		printDataService.send("\n");

		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.send("Customer Care Line: " + Config.Custotmer_care);
		printDataService.send("\n");
		printDataService.send("9AM - 10PM (Monday - Friday)");
		printDataService.send("\n");
//		printDataService.send("www.QPay99.com");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
	}

	private AlertDialog PinBatalAlertDialog() {
		View view = getLayoutInflater().inflate(
				R.layout.adapter_layout_pin_batal, null);
		final EditText edPinBatalPin;
		final Spinner spinnerPinBatalGatewayNo;

		edPinBatalPin = (EditText) view.findViewById(R.id.edPinBatalPin);
		spinnerPinBatalGatewayNo = (Spinner) view
				.findViewById(R.id.spinnerPinBatalGatewayNo);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, gatewayList);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPinBatalGatewayNo.setAdapter(dataAdapter);

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle("Pin Batal")
				.setView(view)
				.setCancelable(false)

				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								if (edPinBatalPin.getText() == null) {
									DLog.e(TAG, "Pin Batal null");
									return;
								}

								if (edPinBatalPin.getText().length() < 7) {
									DLog.e(TAG, "Invalid Pin length");
									return;
								}

								String msg = "pinbatal#"
										+ edPinBatalPin.getText();

								sendPinBatalSMS(msg, spinnerGatewayNo
										.getSelectedItem().toString());
							}
						}).create();
	}

	private void sendPinBatalSMS(String msg, String sendTo) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(sendTo.toString(), null, msg, null, null);
		InsertMobileMobilemessageAsync(msg);
		Toast.makeText(mContext, "Pin Batal send", Toast.LENGTH_SHORT).show();
	}
}

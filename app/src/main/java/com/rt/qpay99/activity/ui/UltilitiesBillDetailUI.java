package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.util.AnimationUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.List;

public class UltilitiesBillDetailUI extends AppCompatActivity {

    private static int SUCCESS = 0;
    private static int INVALID_MIN = 1;
    private static int INVALID_ACC = 2;
    private static int INVALID_NOT_MATCH = 3;
    private static int INVALID_DENO = 4;
    private static int INVALID_ERR = 5;
    private static int INVALID_BILNO = 5;
    InputFilter[] filterArray = new InputFilter[1];
    private String TAG = this.getClass().getName();
    private TextView  tvBalance;
    private ProgressDialog pd;
    private Context mContext;
    private String Denomination, mName, mkeyword;
    private TextView tvProductName, tvDesc, tvSenderMobile,
            tvlain_lain, tvProductDesc;
    private EditText edAmount, edAccount, edcAccount, edCustMSISDN, edBillNo;
    private ImageView productImage;
    private List<String> denominationArray;
    private Button  btnScan;
    private String sCustomerMobileNumber, dProductPrice, sProductID, strCat,
            sCustomerAccountNumber, sClientTxID, BILL, Description, sOtherParameter, CATEGORY, BillNo;
    private ViewFlipper productViewFlipper;
    private int MaxLen = 0, MinLen = 0;
    private Button btnHantar;
    private TextView tvAcc, tvcAcc, tvNoti, tvBillNo;
    private RTWS rt = new RTWS();
    private PrintDataService printDataService = null;
    private CheckBox cbBillSMS;



    private static int CAMERA_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_tnbbill_payment_detail_ui);
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

        cbBillSMS  = (CheckBox) findViewById(R.id.cbBillSMS);
        cbBillSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(mContext, "Hantar SMS", Toast.LENGTH_SHORT).show();
                    edCustMSISDN.setEnabled(true);
                }else{
                    edCustMSISDN.setText("");
                    edCustMSISDN.setEnabled(false);
                }
            }
        });
        tvDesc = findViewById(R.id.tvDesc);
        tvDesc.startAnimation(AnimationUtil.TextBlinking());
        tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(mContext, WebViewUI.class);
//                i.putExtra("redirect_url",ProductURL);
//                startActivity(i);
            }
        });


        denominationArray = FunctionUtil.splitToStringList(Denomination, ",");
        // DLog.e(TAG, "" + "" + denominationArray.contains("300"));
        mName = extras.getString("Name");
        setTitle(mName);
        CATEGORY = extras.getString("CATEGORY");
        Description = extras.getString("Description");
        sProductID = extras.getString("pId");
        DLog.e(TAG, "" + "sProductID" + sProductID);
        productImage = (ImageView) findViewById(R.id.productImage);
        productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));

        edAccount = (EditText) findViewById(R.id.edAccount);
        edBillNo = (EditText) findViewById(R.id.edBillNo);
        edAmount = (EditText) findViewById(R.id.edAmount);
        edcAccount = (EditText) findViewById(R.id.edcAccount);
        edCustMSISDN = (EditText) findViewById(R.id.edCustMSISDN);
        edCustMSISDN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        tvAcc = (TextView) findViewById(R.id.tvAcc);
        tvBillNo = (TextView) findViewById(R.id.tvBillNo);
        tvcAcc = (TextView) findViewById(R.id.tvcAcc);
        tvNoti = (TextView) findViewById(R.id.tvNoti);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setEnabled(true);
        if (mName.equalsIgnoreCase("TNBBILL")
                || mName.equalsIgnoreCase("ELECTRICBILL")
                || mName.equalsIgnoreCase("SAJBILL")
                || mName.equalsIgnoreCase("SYABASBILL")
                || mName.equalsIgnoreCase("SESBBILL")
                || mName.equalsIgnoreCase("JANSBILL")
                || mName.equalsIgnoreCase("TMBILL")
                || mName.equalsIgnoreCase("ASTROBILL")
                ) {
            btnScan.setEnabled(true);
        }

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                IntentIntegrator integrator = new IntentIntegrator(UltilitiesBillDetailUI.this);
                integrator.setPrompt("Scan a barcode");
                //integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            }
        });

        if (denominationArray.size() > 0) {
            edAmount.setHint("RM " + denominationArray.get(0) + " - RM " + denominationArray.get(denominationArray.size() - 1));
        }

        edBillNo.setVisibility(View.GONE);
        tvBillNo.setVisibility(View.GONE);


        if (CATEGORY.equalsIgnoreCase("UTILITIES")) {
            strCat = "No. Akaun";

            if (mName.equalsIgnoreCase("SADABILL"))
                strCat = "Bill Account No.";

            if (mName.equalsIgnoreCase("ELECTRICBILL"))
                strCat = "Bill Account No.";

            if (mName.equalsIgnoreCase("SESCOBILL"))
                strCat = "Customer Contract Acct No.";

            if (mName.equalsIgnoreCase("SESBBILL"))
                strCat = "Customer Account Number.";

            if (mName.equalsIgnoreCase("SAINSBILL"))
                strCat = "No. Akaun";

            if (mName.equalsIgnoreCase("PBAPPBILL"))
                strCat = "Customer Contract Acct No.";

            if (mName.equalsIgnoreCase("JANSBILL"))
                strCat = "Account No.";

//            if (mName.equalsIgnoreCase("AKSBBILL")) {
//                MaxLen = 15;
//                MinLen = 15;
//                edBillNo.setHint("6 digit Bil no.");
//                edBillNo.setVisibility(View.VISIBLE);
//                tvBillNo.setVisibility(View.VISIBLE);
//            }

            if (mName.equalsIgnoreCase("ELECTRICBILL")) {
                MaxLen = 12;
                MinLen = 12;
            }


            tvAcc.setText(strCat);
            tvcAcc.setText("Pengesahan " + strCat);

            edAccount.setHint(MaxLen + " digit");
            edcAccount.setHint(MaxLen + " digit");
            if(MaxLen!=MinLen){
                edcAccount.setHint(MinLen + " - "  + MaxLen + " digit");
            }
            if(MaxLen!=MinLen){
                edAccount.setHint(MinLen + " - "  + MaxLen + " digit");
            }
            edCustMSISDN.setEnabled(true);
            tvNoti.setVisibility(View.VISIBLE);
            edCustMSISDN.setEnabled(true);
            edCustMSISDN.setVisibility(View.VISIBLE);
            cbBillSMS.setVisibility(View.VISIBLE);
        } else {
            strCat = "Pascabayar Telepon No.";
            tvAcc.setText("Pascabayar Telepon No.");
            tvcAcc.setText("Pengesahan Pascabayar Telepon No.");
            edAccount.setHint("01xxxxxxxxx");
            edAccount.setInputType(InputType.TYPE_CLASS_NUMBER);
            edcAccount.setHint("01xxxxxxxxx");
            edcAccount.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvNoti.setVisibility(View.GONE);
            edCustMSISDN.setEnabled(false);
            edCustMSISDN.setVisibility(View.GONE);
            cbBillSMS.setVisibility(View.GONE);
        }
        edCustMSISDN.setEnabled(false);



        edAccount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MaxLen)});
        edcAccount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MaxLen)});


        tvBalance = (TextView) findViewById(R.id.tvBalance);

        tvProductName = (TextView) findViewById(R.id.tvProductName);
        tvProductName.setText(mName);

        tvProductDesc = (TextView) findViewById(R.id.tvProductDesc);
        tvProductDesc.setText(Description);

        btnHantar = (Button) findViewById(R.id.btnHantar);
        btnHantar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int result = validate();
                switch (result) {

                    case 1:
                        transcationFailedAlertDialog("Min RM " + denominationArray.get(0)).show();
                        break;
                    case 2:
                        transcationFailedAlertDialog(strCat + " tidak sah.").show();
                        break;
                    case 3:
                        transcationFailedAlertDialog("Pengesahan " + strCat + " tidak sepadan").show();
                        break;
                    case 4:
                        transcationFailedAlertDialog("Nilai tambah tidak sah. Sila masuk sekali lagi.").show();
                        break;
                    default:
                        confirmBuyAlertDialog("").show();
                        break;
                }
            }
        });


        if (FunctionUtil.isSet(SRSApp.printerMacAdd))
            printDataService = new PrintDataService(mContext,
                    SRSApp.printerMacAdd);


//        if (Config.isDebug) {
//            edAccount.setText("0123456789");
//            edAmount.setText("100");
//            edcAccount.setText("0123456789");
//            //edCustMSISDN.setText("0126112184");
//        }

        getMemberBalance();

    }

    private int validate() {

        try {
            int min = Integer.parseInt(edAmount.getText().toString());
            if (min < Integer.parseInt(denominationArray.get(0)))
                return INVALID_MIN;

            if ((edAccount.getText().length() == MinLen) || (edAccount.getText().length() == MaxLen)) {

            } else {
                return INVALID_ACC;
            }

            if (!edAccount.getText().toString().equalsIgnoreCase(edcAccount.getText().toString()))
                return INVALID_NOT_MATCH;

            sCustomerMobileNumber = edcAccount.getText().toString();
            sCustomerAccountNumber = edcAccount.getText().toString();
            sOtherParameter = "";
            if (CATEGORY.equalsIgnoreCase("UTILITIES")) {
                if(cbBillSMS.isChecked())
                    sOtherParameter = "CUSTMOBILE=" + edCustMSISDN.getText().toString();
            }
            dProductPrice = edAmount.getText().toString();

            if (!denominationArray.contains(dProductPrice))
                return INVALID_DENO;

            if (mName.equalsIgnoreCase("AKSBBILL")
//                    || mName.equalsIgnoreCase("SATUBILL")
//                    || mName.equalsIgnoreCase("LAPBILL")
                    ) {
                BillNo = edBillNo.getText().toString();
            }

            return SUCCESS;

        } catch (Exception ex) {
            DLog.e(TAG, "validate " + ex.getMessage());
            return INVALID_ERR;
        }

    }

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

//                if (mName.equalsIgnoreCase("AKSBBILL")
//                        || mName.equalsIgnoreCase("SATUBILL")
//                        || mName.equalsIgnoreCase("LAPBILL")
//                        ) {
//                    sCustomerMobileNumber = sCustomerMobileNumber + "@" + BillNo;
//                }

                sClientTxID = FunctionUtil.getStringDateTimeSec();
                RequestInputResponse result = rt.RequestInput(
                        sCustomerAccountNumber, sCustomerMobileNumber,
                        dProductPrice, sProductID, "ANDROID_Q", sClientTxID,
                        sOtherParameter, "", "", "");
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
                                    if(SharedPreferenceUtil.getPrinterName().equalsIgnoreCase("InnerPrinter:")){
                                        printReceiptInnerPrinter();
                                    }else
                                        printReceipt();
                                finish();
                            }
                        }).create();
    }

    private AlertDialog confirmBuyAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(mName)
                .setMessage(msg + "Please click OK to proceed.")
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


        if ((mName.equalsIgnoreCase("UMOBILEBILL"))
                || (mName.equalsIgnoreCase("CELCOMBILL"))
                || (mName.equalsIgnoreCase("DIGIBILL"))
                || (mName.equalsIgnoreCase("XOXBILL"))
                || (mName.equalsIgnoreCase("MAXISBILL"))) {
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
        final EditText input = new EditText(UltilitiesBillDetailUI.this);
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
                .setMessage("Sila Masuk Telephone No. / Bil No. sekali lagi" + "/n" + denominationArray.toString())
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
        final EditText input = new EditText(UltilitiesBillDetailUI.this);

        input.setInputType(InputType.TYPE_CLASS_PHONE);


        if ((mName.equalsIgnoreCase("UMOBILEBILL")) || (mName.equalsIgnoreCase("CELCOMBILL")) || (mName.equalsIgnoreCase("DIGIBILL")) || (mName.equalsIgnoreCase("MAXISBILL")) || (mName.equalsIgnoreCase("XOXBILL")) || (mName.equalsIgnoreCase("ELECTRICBILL"))) {
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

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            CloseAlertDialog(
                    "Do you want to cancel this transaction?").show();
        }
        return super.onKeyDown(keyCode, event);
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
        printDataService.setCommand(3);
//        printDataService.setCommand(2);
        printDataService.send(SharedPreferenceUtil.getMerchantName());
        printDataService.send("\n");
        printDataService.send("** Pay Bill **");
        printDataService.setCommand(20);

//		printDataService.send("Agent No:");
//		printDataService.send(countSpacing("Agent No:",
//				SharedPreferenceUtil.getsClientUserName()));
//		printDataService.send(SharedPreferenceUtil.getsClientUserName());
//		printDataService.send("\n");

//        printDataService.send("Terminal:");
//        printDataService.send(countSpacing("Terminal:",
//                SharedPreferenceUtil.getsDeviceID()));
//        printDataService.send(SharedPreferenceUtil.getsDeviceID());

        printDataService.send("\n");
        printDataService.send("Date:");
        printDataService.send(FunctionUtil.countSpacing2("Date:",
                FunctionUtil.getStrCurrentDateTime()));
        printDataService.send(FunctionUtil.getStrCurrentDateTime());
        printDataService.send("\n");

        printDataService.send("Ref No:");
        printDataService.send(FunctionUtil.countSpacing2("Ref No:", sClientTxID));
        printDataService.send(sClientTxID);

        printDataService.send("Status:");
        printDataService.send(FunctionUtil.countSpacing2("Status:", "Accepted"));
        printDataService.send("Accepted");
        printDataService.send("\n");
        printDataService.send("\n");

//        printDataService.setCommand(2);
        printDataService.setCommand(22);

        printDataService.setCommand(3);
        printDataService.setCommand(4);

        printDataService.setCommand(21);
        printDataService.send(sCustomerAccountNumber);
        printDataService.send("\n");
        printDataService.send(mName + " " + "RM" + dProductPrice);
        printDataService.send("\n");


//        printDataService.setCommand(2);
        printDataService.setCommand(22);
        printDataService.setCommand(3);

        printDataService.send("\n");
        printDataService.setCommand(21);
        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send(Config.WORKING_HOUR);
        printDataService.send("\n");
        printDataService.send(Config.Custotmer_website);
        printDataService.send("\n");
        printDataService.send("Thank You " + "V." + SRSApp.versionName);
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
        printDataService.setCommand(3);
        printDataService.setCommand(2);
        printDataService.send(SharedPreferenceUtil.getMerchantName());
        printDataService.send("\n");
        printDataService.send("** Pay Bill **");
        printDataService.setCommand(20);

//		printDataService.send("Agent No:");
//		printDataService.send(countSpacing("Agent No:",
//				SharedPreferenceUtil.getsClientUserName()));
//		printDataService.send(SharedPreferenceUtil.getsClientUserName());
//		printDataService.send("\n");

//        printDataService.send("Terminal:");
//        printDataService.send(countSpacing("Terminal:",
//                SharedPreferenceUtil.getsDeviceID()));
//        printDataService.send(SharedPreferenceUtil.getsDeviceID());

        printDataService.send("\n");
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
        printDataService.setCommand(21);
        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send(Config.WORKING_HOUR);
        printDataService.send("\n");
        printDataService.send(Config.Custotmer_website);
        printDataService.send("\n");
        printDataService.send("Thank You " + "V." + SRSApp.versionName);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        DLog.e(TAG, "BACK");
        try {
            if (scanResult != null) {
                // handle scan result
                DLog.e(TAG, scanResult.getContents().toString());
                edAccount.setText(scanResult.getContents().toString());
                edcAccount.setText(scanResult.getContents().toString());
            }

        } catch (Exception ex) {

        }


    }

    private AlertDialog CloseAlertDialog(String msg) {
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

    private class valueOnClickListener implements OnClickListener {
        String mValue;

        public valueOnClickListener(String mValue) {
            this.mValue = mValue;
        }

        @Override
        public void onClick(View v) {
            // if (!PrintDataService.isPrinterConnected()) {
            // connectPrinterAlertDialog().show();
            // return;
            // }


            if (mValue.equalsIgnoreCase("")) {
                lainlainAlertDialog("").show();
            } else {
                insertMSISDNAlertDialog(mValue).show();
            }
        }
    }

}

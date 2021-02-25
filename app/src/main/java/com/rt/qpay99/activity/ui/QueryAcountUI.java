package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.rt.qpay99.Config;
import com.rt.qpay99.FirebaseObj.PriceList;
import com.rt.qpay99.Helper.HttpHandlerHelper;
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

import org.json.JSONObject;

import java.util.List;

import static com.rt.qpay99.util.FunctionUtil.countSpacing;


public class QueryAcountUI extends AppCompatActivity {

    com.beardedhen.androidbootstrap.BootstrapEditText edAccNo,edAmounttopay;
    com.beardedhen.androidbootstrap.BootstrapButton btnScan,btnCheckBill,btnPayNow;

    private String TAG = this.getClass().getName();

    private ViewFlipper myViewFlipper;
    private ProgressDialog pd;
    private Context mContext;
    private RTWS rt=new RTWS();

    private ImageView productImage;
    private TextView tvAccountNo,tvProductName,tvBalance,tvProductDesc,tvAccountBalance,tvStatementDate,tvOutstandingBalance,tvLastPaymentAmout,tvAccountName,tvDeno;

    private String sCustomerAccountNumber,sCustomerMobileNumber, sClientTxID, BILL,  sOtherParameter, BillNo,dProductPrice;
    private String mName,CATEGORY,Description,sProductID,Denomination;
    private int MaxLen = 0, MinLen = 0;

    private PrintDataService printDataService = null;
    private List<String> denominationArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_acount_ui);
        mContext = this;


        Bundle extras = getIntent().getExtras();

        Denomination = extras.getString("Denomination");
        denominationArray = FunctionUtil.splitToStringList(Denomination, ",");
        DLog.e(TAG, "" + "" + Denomination);

        if (extras.getString("MaxLen") != null) {
            MaxLen = Integer.parseInt(extras.getString("MaxLen"));
            DLog.e(TAG, "MaxLen " + MaxLen);
        }

        if (extras.getString("MinLen") != null) {
            MinLen = Integer.parseInt(extras.getString("MinLen"));
            DLog.e(TAG, "MinLen " + MinLen);
        }

        mName = extras.getString("Name");
        CATEGORY = extras.getString("CATEGORY");
        Description = extras.getString("Description");
        sProductID = extras.getString("pId");
        DLog.e(TAG, "" + "sProductID" + sProductID);
        productImage = (ImageView) findViewById(R.id.productImage);
        productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));


        myViewFlipper =(ViewFlipper) findViewById(R.id.myViewFlipper);

        tvAccountNo=(TextView) findViewById(R.id.tvAccountNo);
        tvDeno=(TextView) findViewById(R.id.tvDeno);
        tvDeno.setText("RM " + denominationArray.get(0) + " - RM " + denominationArray.get(denominationArray.size() - 1));
        tvProductName =(TextView) findViewById(R.id.tvProductName);
        tvProductName.setText(mName);
        tvBalance =(TextView) findViewById(R.id.tvBalance);
        tvProductDesc =(TextView) findViewById(R.id.tvProductDesc);
        tvProductDesc.setText(Description);

        tvAccountBalance =(TextView) findViewById(R.id.tvAccountBalance);
        tvStatementDate =(TextView) findViewById(R.id.tvStatementDate);
        tvOutstandingBalance =(TextView) findViewById(R.id.tvOutstandingBalance);
        tvLastPaymentAmout =(TextView) findViewById(R.id.tvLastPaymentAmout);
        tvAccountName =(TextView) findViewById(R.id.tvAccountName);


        edAmounttopay=(com.beardedhen.androidbootstrap.BootstrapEditText )findViewById(R.id.edAmounttopay);
        edAccNo=(com.beardedhen.androidbootstrap.BootstrapEditText )findViewById(R.id.edAccNo);
        btnPayNow=(com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btnPayNow);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edAmounttopay.getText().length()>1){
                    dProductPrice = edAmounttopay.getText().toString();
                    int min = Integer.parseInt(dProductPrice);
                    if (min < Integer.parseInt(denominationArray.get(0))){
                        Toast.makeText(mContext,"Min RM " + denominationArray.get(0),Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!denominationArray.contains(dProductPrice))
                    {
                        Toast.makeText(mContext,"Invalid Deno",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    confirmBuyAlertDialog("Pay RM " + dProductPrice + " Please enter OK to proceed.").show();
                }else{
                    Toast.makeText(mContext,"Invalid A",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnScan=(com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                IntentIntegrator integrator = new IntentIntegrator(QueryAcountUI.this);
                integrator.setPrompt("RichTech Communications Sdn. Bhd.");
                //integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.initiateScan();
            }
        });

        btnCheckBill=(com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btnCheckBill);
        btnCheckBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AccNo = edAccNo.getText().toString();
                DLog.e(TAG,"" + AccNo.length());

                sCustomerMobileNumber= AccNo;
                sCustomerAccountNumber=AccNo;


                DLog.e(TAG,"mName " + mName);
                if (mName.equalsIgnoreCase("ASTROBILL")){
                    queryAccountAsync(sCustomerMobileNumber);
                }else if (mName.equalsIgnoreCase("AIRPAHANGBILL")){
                    queryAIRPAHANGBILLAsync(sCustomerMobileNumber);
                }


//                if(AccNo.length()==10){
//                    queryAccountAsync(sCustomerMobileNumber);
//                }else{
//                    Toast.makeText(mContext,"Invalid Account No.",Toast.LENGTH_SHORT).show();
//                }

            }
        });


        if (FunctionUtil.isSet(SRSApp.printerMacAdd))
            printDataService = new PrintDataService(mContext,
                    SRSApp.printerMacAdd);


        getMemberBalance();

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

    private void queryAccountAsync(final String accNo) {

        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
                SpannableString ss1 = new SpannableString("Please wait ...");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(Typeface.BOLD), 0,
                        ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                        0, ss1.length(), 0);
                pd = new ProgressDialog(mContext);
                pd.setTitle(ss1);
                pd.setMessage("Connecting ...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected String doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                DLog.e(TAG,"QueryAccount " + accNo );

                return rt.QueryAccount(accNo);
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (FunctionUtil.isSet(result)) {

                    List<String > s = FunctionUtil.splitToStringList(result, ",");
                    if(s.size()>0){
                        try{
                            DLog.e(TAG, "" + s.get(0));

                            double d = Double.parseDouble(s.get(0));
                            int OutstadingBill = (int) d;

                            tvAccountBalance.setText(s.get(0).toString());
                            tvStatementDate.setText(s.get(1).toString());
                            tvOutstandingBalance.setText(s.get(2).toString());
                            tvAccountName.setText(s.get(3).toString());
                            tvLastPaymentAmout.setText(s.get(4).toString());
                            edAmounttopay.setText(String.valueOf(OutstadingBill + 1));
                            tvAccountNo.setText(accNo);
                            myViewFlipper.showNext();
                        }catch (Exception e){
                            Toast.makeText(mContext,"No record found!!",Toast.LENGTH_SHORT).show();
                        }
                    }else
                        Toast.makeText(mContext,"No record found!!",Toast.LENGTH_SHORT).show();
                }


                if(pd!=null)
                    if(pd.isShowing()){
                        pd.dismiss();
                        pd=null;
                    }

            }

        }.execute();
    }

    private void queryAIRPAHANGBILLAsync(final String accNo) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext);
                SpannableString ss1 = new SpannableString("Please wait ...");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(Typeface.BOLD), 0,
                        ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                        0, ss1.length(), 0);
                pd = new ProgressDialog(mContext);
                pd.setTitle(ss1);
                pd.setMessage("Connecting ...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected String doInBackground(Void... arg0) {
                // TODO Auto-generated method stub

                try {
                    HttpHandlerHelper sh = new HttpHandlerHelper();
                    String url = "http://reload.dyndns.org:8020/rtweb/api/CheckBill/airpahang/" + accNo;
                    DLog.e(TAG, "url : " + url);
                    String jsonStr = sh.makeServiceCall(url);
                    DLog.e(TAG, "Response from url: " + jsonStr);
                    PriceList mPriceList = null;

                    return jsonStr;

                }catch (Exception ex){

                }

                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (FunctionUtil.isSet(result)) {
                    try
                    {
                        JSONObject json = new JSONObject(result);
                        DLog.e(TAG, "Nombor_Acc " + json.getString("Nombor_Acc"));
                        DLog.e(TAG, "Nombor_Pelangan " + json.getString("Nombor_Pelangan"));
                        DLog.e(TAG, "Nama " + json.getString("Nama"));
                        DLog.e(TAG, "Nombor_meter " + json.getString("Nombor_meter"));
                        DLog.e(TAG, "Alamat " + json.getString("Alamat"));
                        DLog.e(TAG, "Bil_Semasa " + json.getString("Bil_Semasa"));
                        DLog.e(TAG, "Tunggakan " + json.getString("Tunggakan"));
                        DLog.e(TAG, "Bayaran_Terakhir " + json.getString("Bayaran_Terakhir"));
                        DLog.e(TAG, "Bil_Date " + json.getString("Bil_Date"));

                        double d = Double.parseDouble(json.getString("Bil_Semasa"));


                        DLog.e(TAG, "0");
                        if (!TextUtils.isEmpty(json.getString("Bil_Semasa"))) {
                            tvAccountBalance.setText(json.getString("Bil_Semasa"));
                        }
                        DLog.e(TAG, "1");
                        if (!TextUtils.isEmpty(json.getString("Bil_Date"))) {
                            tvStatementDate.setText(json.getString("Bil_Date"));
                        }
                        DLog.e(TAG, "2");
                        if (!TextUtils.isEmpty(json.getString("Tunggakan"))) {
                            tvOutstandingBalance.setText(json.getString("Tunggakan"));
                        }
                        DLog.e(TAG, "3");
                        if (!TextUtils.isEmpty(json.getString("Nama"))) {
                            tvAccountName.setText(json.getString("Nama"));
                        }
                        DLog.e(TAG, "4");
                        if (!TextUtils.isEmpty(json.getString("Bayaran_Terakhir"))) {
                            tvLastPaymentAmout.setText(json.getString("Bayaran_Terakhir"));
                        }

                        try{

                            int OutstadingBill = (int) d;

                            if(OutstadingBill < 30)
                                OutstadingBill=30;
                            else{
                                OutstadingBill=OutstadingBill + 1;
                            }

                            edAmounttopay.setText(String.valueOf(OutstadingBill));
                            tvAccountNo.setText(accNo);
                            myViewFlipper.showNext();
                        }catch (Exception e){
                            Toast.makeText(mContext,"No record found!!",Toast.LENGTH_SHORT).show();
                        }




                    }catch (Exception ex){
                        Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(mContext, "No record found!!", Toast.LENGTH_SHORT).show();
                }



                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

            }

        }.execute();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        DLog.e(TAG, "BACK");
        try {
            if (scanResult != null) {
                // handle scan result
                DLog.e(TAG, scanResult.getContents().toString());
                edAccNo.setText(scanResult.getContents().toString());
            }

        } catch (Exception ex) {

        }


    }

    private void RequestInputAsync() {
        new AsyncTask<Void, Void, RequestInputResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
                SpannableString ss1 = new SpannableString("Please wait ...");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(Typeface.BOLD), 0,
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

    private AlertDialog confirmBuyAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(mName)
                .setMessage(msg)
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
}

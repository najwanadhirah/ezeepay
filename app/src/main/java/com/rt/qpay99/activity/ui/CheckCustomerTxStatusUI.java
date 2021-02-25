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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.adapter.CheckCustomerTxStatusAdapter;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.fragment.CustomDatePickerFragment;
import com.rt.qpay99.object.CustomerTxStatusInfo;
import com.rt.qpay99.object.RequestReloadPinObject;
import com.rt.qpay99.util.CalenderUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckCustomerTxStatusUI extends AppCompatActivity implements
        OnClickListener, CustomDatePickerFragment.DatePickedListener {

    CustomerTxStatusInfo mCustomerTxStatusInfo = new CustomerTxStatusInfo();
    RTWS rtWS = new RTWS();
    private EditText edCustAcc;
    private TextView edStartDate, edEndDate;
    private Context mContext;
    private Calendar cal = Calendar.getInstance();
    private String TAG = this.getClass().getName();
    private boolean isStarDateClick, isEndDateClick = false;
    private CustomDatePickerFragment newFragment = new CustomDatePickerFragment();
    private Button btnCheckStats;
    private CheckCustomerTxStatusAdapter mCheckCustomerTxStatusAdapter;
    private ProgressDialog pd;
    private String sSDate, sEDate, sCustomerAccount;
    private ListView lvCheckCustStatus;
    private List<CustomerTxStatusInfo> CustomerTxStatusInfos = new ArrayList<CustomerTxStatusInfo>();

    private String mName, pDate, pAmount, pStatus, pDN, pCode, pLocalMOId,
            pReloadMSISDN, pProduct;
    private RequestReloadPinObject mRequestReloadPinObject = new RequestReloadPinObject();
    private PrintDataService printDataService = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_layout_check_cust_tx);
        // TODO Auto-generated method stub

        mContext = this;
        edCustAcc = (EditText) findViewById(R.id.edCustAcc);
        edStartDate = (TextView) findViewById(R.id.edStartDate);
        edEndDate = (TextView) findViewById(R.id.edEndDate);
        edCustAcc.setOnClickListener(this);
        edStartDate.setOnClickListener(this);
        edEndDate.setOnClickListener(this);
        edStartDate.setText(CalenderUtil.getStringAddDate(new Date(),
                "yyyy/MM/dd", 0));
        edEndDate.setText(CalenderUtil.getStringAddDate(new Date(),
                "yyyy/MM/dd", 0));

        btnCheckStats =  findViewById(R.id.btnCheckStats);
        btnCheckStats.setOnClickListener(this);

        sSDate = edStartDate.getText().toString();
        sEDate = edEndDate.getText().toString();
        lvCheckCustStatus =  findViewById(R.id.lvCheckCustStatus);

        lvCheckCustStatus.setOnItemClickListener(new OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {
                CustomerTxStatusInfos = (List<CustomerTxStatusInfo>) adapter
                        .getItemAtPosition(position);

                mCustomerTxStatusInfo = CustomerTxStatusInfos.get(position);
                DLog.e(TAG, "" + mCustomerTxStatusInfo.getAmount());
                DLog.e(TAG, "" + mCustomerTxStatusInfo.getRetailPrice());
                DLog.e(TAG, "" + mCustomerTxStatusInfo.getProduct());
                DLog.e(TAG, "" + mCustomerTxStatusInfo.getLocalMOID());

                mName = mCustomerTxStatusInfo.getProduct();

                pDate = "";
                if (FunctionUtil.isSet(mCustomerTxStatusInfo.getDateTime()))
                    pDate = mCustomerTxStatusInfo.getDateTime();

                DLog.e(TAG, "pDate " + pDate.toString());
                pDate = FunctionUtil.getConvertdate(pDate,
                        "yyyy-MM-dd HH:mm:ss");
                DLog.e(TAG, "pDate " + pDate.toString());

                pAmount = "";
                if (FunctionUtil.isSet(mCustomerTxStatusInfo.getRetailPrice()))
                    pAmount = mCustomerTxStatusInfo.getRetailPrice();

                pStatus = "";
                if (FunctionUtil.isSet(mCustomerTxStatusInfo.getStatus()))
                    pStatus = mCustomerTxStatusInfo.getStatus();

                pProduct = "";
                if (FunctionUtil.isSet(mCustomerTxStatusInfo.getProduct()))
                    pProduct = mCustomerTxStatusInfo.getProduct();

                pCode = "";
                if (FunctionUtil.isSet(mCustomerTxStatusInfo.getCode()))
                    pCode = mCustomerTxStatusInfo.getCode();

                pDN = "";
                if (FunctionUtil.isSet(mCustomerTxStatusInfo.getDN()))
                    pDN = mCustomerTxStatusInfo.getDN();

                pReloadMSISDN = "";
                if (FunctionUtil.isSet(mCustomerTxStatusInfo.getsReloadMSISDN()))
                    pReloadMSISDN = mCustomerTxStatusInfo.getsReloadMSISDN();

                pLocalMOId = mCustomerTxStatusInfo.getLocalMOID();


                optionButton();
            }
        });

        if (FunctionUtil.isSet(SRSApp.printerMacAdd))
            printDataService = new PrintDataService(mContext,
                    SRSApp.printerMacAdd);

        sCustomerAccount = "";
        if (edCustAcc.getText().length() > 0)
            sCustomerAccount = edCustAcc.getText().toString();
        CheckCustomerTxStatus(sCustomerAccount);
        InputMethodManager im = (InputMethodManager) this
                .getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void optionButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Please Select")
                .setPositiveButton(R.string.shareReceipt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;

                        DLog.e(TAG, "" + mCustomerTxStatusInfo.getStatus());
                        DLog.e(TAG, "" + mCustomerTxStatusInfo.getAmount());
                        DLog.e(TAG, "" + mCustomerTxStatusInfo.getDateTime());
                        SharedPreferenceUtil.editShareVia(mCustomerTxStatusInfo);

                        if (mName.indexOf("PIN") > -1) {
                            getGetReloadPIN(
                                    SharedPreferenceUtil.getsClientUserName(),
                                    SharedPreferenceUtil.getsClientPassword(),
                                    pLocalMOId,true);
                            DLog.e(TAG, "" + mRequestReloadPinObject.getsReloadPin());
                            DLog.e(TAG, "mCustomerTxStatusInfo.getLocalMOID() " + mCustomerTxStatusInfo.getLocalMOID());
                            DLog.e(TAG, "pLocalMOId " + pLocalMOId);
                            mRequestReloadPinObject.setsLocalMOID(mCustomerTxStatusInfo.getLocalMOID());
                            SharedPreferenceUtil.editShareViaRelaodPin(mRequestReloadPinObject);

                        } else {
                            intent = new Intent(mContext, ShareViaUI.class);
                            intent.putExtra("mName", mName);
                            mContext.startActivity(intent);
                        }

                    }
                })

                .setNegativeButton(R.string.reprint, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (FunctionUtil.isSet(SRSApp.printerMacAdd)) {
                            printDataService = new PrintDataService(mContext,
                                    SRSApp.printerMacAdd);
                            if (PrintDataService.isPrinterConnected()) {
                                if (mName.indexOf("PIN") > -1) {
                                    DLog.e(TAG, "getGetReloadPIN");
                                    getGetReloadPIN(
                                            SharedPreferenceUtil.getsClientUserName(),
                                            SharedPreferenceUtil.getsClientPassword(),
                                            pLocalMOId,false);
                                } else {
                                    if (FunctionUtil.isPDA(SharedPreferenceUtil.getPrinterName())) {
                                        printReceiptInnerPrinter();
                                    }else
                                        printReceipt();


                                }
                            } else {
                                connectPrinterAlertDialog().show();
                            }
                        } else {
                            connectPrinterAlertDialog().show();
                        }
                    }

                });
        builder.create().show();
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

    private void getGetReloadPIN(final String sClientUserName,
                                 final String sClientPassword, final String sLocalMOID, final boolean bShare) {
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
                        if (bShare){
                            DLog.e(TAG, "" + mRequestReloadPinObject.getsReloadPin());
                            DLog.e(TAG, "mCustomerTxStatusInfo.getLocalMOID() " + mCustomerTxStatusInfo.getLocalMOID());
                            DLog.e(TAG, "pLocalMOId " + pLocalMOId);
                            mRequestReloadPinObject.setsLocalMOID(mCustomerTxStatusInfo.getLocalMOID());
                            SharedPreferenceUtil.editShareViaRelaodPin(mRequestReloadPinObject);
                            Intent intent = new Intent(mContext, ShareViaUI.class);
                            intent.putExtra("mName", mName);
                            mContext.startActivity(intent);
                            return;
                        }
                        if (PrintDataService.isPrinterConnected())
                            if (SharedPreferenceUtil.isRequiredPrinter())
                                if (FunctionUtil.isPDA(SharedPreferenceUtil.getPrinterName())) {
                                    printReceiptInnerPrinter();
                                }else
                                    printReceipt();



                        return;
                    }
                }

                transcationFailedAlertDialog(
                        "Transaction Failed. Please try again later.").show();

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

    private void CheckCustomerTxStatus(final String sCustomerAccount) {

        new AsyncTask<String, Void, List<CustomerTxStatusInfo>>() {

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
                pd.setMessage("Verifying ...");
                pd.setCancelable(false);
                pd.show();
            }

            protected List<CustomerTxStatusInfo> doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                return rtWS.CheckCustomerTxStatus(
                        SharedPreferenceUtil.getsClientUserName(),
                        SharedPreferenceUtil.getsClientPassword(),
                        sCustomerAccount, sSDate, sEDate);

            }

            @Override
            protected void onPostExecute(List<CustomerTxStatusInfo> result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                if (result != null) {
                    mCheckCustomerTxStatusAdapter = new CheckCustomerTxStatusAdapter(
                            mContext, result);
                    lvCheckCustStatus.setAdapter(mCheckCustomerTxStatusAdapter);
                } else {
                    mCheckCustomerTxStatusAdapter = new CheckCustomerTxStatusAdapter(
                            mContext, CustomerTxStatusInfos);
                    lvCheckCustStatus.setAdapter(mCheckCustomerTxStatusAdapter);
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edCustAcc.getWindowToken(), 0);
            }

        }.execute();

    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        DLog.e(TAG, " Click");
        switch (v.getId()) {
            case R.id.edStartDate:
                DLog.e(TAG, "edStartDate Click");
                isStarDateClick = true;
                newFragment.setmListener(this);
                newFragment.show(getSupportFragmentManager(), TAG);
                break;
            case R.id.edEndDate:
                DLog.e(TAG, "edEndDate Click");
                isEndDateClick = true;
                newFragment.setmListener(this);
                newFragment.show(getSupportFragmentManager(), TAG);
                break;
            case R.id.btnCheckStats:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edCustAcc.getWindowToken(), 0);
                sCustomerAccount = "";
                if (edCustAcc.getText().length() > 0)
                    sCustomerAccount = edCustAcc.getText().toString();
                CheckCustomerTxStatus(sCustomerAccount);
                break;
        }
    }

    @Override
    public void onDatePicked(Calendar time) {
        // TODO Auto-generated method stub
        DLog.e(TAG, "" + time.getTime());
        if (isStarDateClick) {
            edStartDate.setText(CalenderUtil.getStringAddDate(time.getTime(),
                    "yyyy/MM/dd", 0));
            edEndDate.setText(CalenderUtil.getStringAddDate(time.getTime(),
                    "yyyy/MM/dd", 0));
            isStarDateClick = false;
            sSDate = edStartDate.getText().toString();
            sEDate = edEndDate.getText().toString();
        }

        if (isEndDateClick) {
            edEndDate.setText(CalenderUtil.getStringAddDate(time.getTime(),
                    "yyyy/MM/dd", 0));
            isEndDateClick = false;
            sEDate = edEndDate.getText().toString();
        }

    }

    private void printReceiptInnerPrinter() {
        DLog.e(TAG,"=================== > 1");

        printDataService = new PrintDataService(mContext,
                SRSApp.printerMacAdd);

        DLog.e(TAG,"printReceiptInnerPrinter");
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
        printDataService.send("** Duplicate Copied **");
        printDataService.send("\n");

        printDataService.send("Tx Date :");
        printDataService.send(FunctionUtil.countSpacing2("Tx Date :", pDate));
        printDataService.send(pDate);

        printDataService.send("\n");
        DLog.e(TAG,"=================== > 2");
        if (mName.indexOf("PIN") > -1) {
            printDataService.send("Serial No. :");
            printDataService.send(FunctionUtil.countSpacing2("Serial No. :",
                    mRequestReloadPinObject.getsSerialNumber()));
            printDataService.send(mRequestReloadPinObject.getsSerialNumber());
            printDataService.send("\n");
            printDataService.send("Tx No. :");
            printDataService.send(FunctionUtil.countSpacing2("Tx No. :", pDN));
            printDataService.send(pDN);
            printDataService.send("\n");
        } else {
            printDataService.send("Status :");
            printDataService.send(FunctionUtil.countSpacing2("Status :",
                    pStatus));
            printDataService.send(pStatus);
            printDataService.send("\n");

            printDataService.send("");
            printDataService.send(FunctionUtil.countSpacing2("",
                    pCode));
            printDataService.send(pCode);
            printDataService.send("\n");
        }



        printDataService.setCommand(22);
        printDataService.setCommand(21);

        printDataService.setCommand(3);
        printDataService.setCommand(4);

        printDataService.setCommand(21);
        DLog.e(TAG,"=================== > 3");
        if (mName.indexOf("PIN") > -1) {
            DLog.e(TAG,"PIN");
            // printDataService.send(mRequestReloadPinObject.getsReloadTelco()
            // + " " + "RM" + mRequestReloadPinObject.getsAmount());
            printDataService.send(pProduct + " " + "RM" +
                    mRequestReloadPinObject.getsAmount());
            printDataService.send("\n");
            printDataService.setCommand(20);
            printDataService.send("PIN :");
            printDataService.send("\n");
            printDataService.send(FunctionUtil.getPINFormat(mRequestReloadPinObject.getsReloadPin()));
            printDataService.setCommand(3);
            printDataService.send("\n");

//            printDataService.setCommand(2);
            printDataService.send("Pin Expired Date");
            printDataService.send("\n");

            if (mRequestReloadPinObject.getsExpiryDate() != null)
                printDataService.send(mRequestReloadPinObject.getsExpiryDate());
            printDataService.send("\n");

//            printDataService.setCommand(2);
            printDataService.send("Topup Instruction");
            printDataService.send("\n");
            if (mRequestReloadPinObject.getsDescription() != null)
                printDataService
                        .send(mRequestReloadPinObject.getsDescription());
            printDataService.send("\n");

        } else {
            DLog.e(TAG,"BILL");
            printDataService.setCommand(21);
            if (mName.indexOf("BILL") > -1) {
                printDataService.send(pReloadMSISDN);
                printDataService.send("\n");
                printDataService.send(mName + " " + "RM" + pAmount);
                printDataService.send("\n");
            } else {
                printDataService.send(pReloadMSISDN);
                printDataService.send("\n");
                if(mName.indexOf("FLEXI")>0){
                    printDataService.send(mName + " " + pAmount);
                    printDataService.send("\n");
                }else{
                    printDataService.send(mName + " " + "RM" + pAmount);
                    printDataService.send("\n");
                }

            }

        }
        DLog.e(TAG,"=================== > 4");
        printDataService.setCommand(3);
        printDataService.setCommand(20);
        printDataService.send("\n");

        if (mName.equalsIgnoreCase("CELCOM") || mName.equalsIgnoreCase("CELCOMPIN")) {
            printDataService.send("CELCOM Careline : +603 36308888 atau 1111");
            printDataService.send("\n");
        }
        if (mName.equalsIgnoreCase("MAXIS") || mName.equalsIgnoreCase("MAXISPIN")) {
            printDataService.send("MAXIS Careline : 1300-820-120 atau 123");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("DIGI") || mName.equalsIgnoreCase("DIGIPIN")) {
            printDataService.send("DiGi Careline : +6016 2211 800");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("UMOBILE") || mName.equalsIgnoreCase("UMOBILEPIN")) {
            printDataService.send("UMOBILE Careline : +6018 388 1318");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("TUNETALK") || mName.equalsIgnoreCase("TUNETALKPIN")) {
            printDataService.send("TUNETALK Careline : +603-79490000 atau 13100");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("XOX") || mName.equalsIgnoreCase("XOXPIN")) {
            printDataService.send("XOX Careline : +603-7962 8000 atau 12273");
            printDataService.send("\n");
        }


        printDataService.send("Kupon Prepaid yg dibeli TIDAK boleh tukar ganti.");
        printDataService.send("\n");
//		printDataService.send(countSpacing("",
//				"(CUSTOMER COPY)"));
//		printDataService.send("(CUSTOMER COPY)");
//		printDataService.send("\n");
        printDataService.setCommand(21);
        printDataService.setCommand(21);


//        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");
        printDataService.send(Config.Custotmer_website);
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");

        DLog.e(TAG,"=================== > 5");
        try {
            UpdatePrintCountAsync();
        }catch (Exception e){

        }

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
        printDataService.send("** Duplicate Copied **");
        printDataService.send("\n");

        printDataService.send("Transaction Date :");
        printDataService.send(countSpacing("Transaction Date :", pDate));
        printDataService.send(pDate);

        printDataService.send("\n");

        if (mName.indexOf("PIN") > -1) {
            printDataService.send("Serial No. :");
            printDataService.send(countSpacing("Serial No. :",
                    mRequestReloadPinObject.getsSerialNumber()));
            printDataService.send(mRequestReloadPinObject.getsSerialNumber());
            printDataService.send("\n");
            printDataService.send("Transaction No. :");
            printDataService.send(countSpacing("Transaction No. :", pDN));
            printDataService.send(pDN);
            printDataService.send("\n");
        } else {
            printDataService.send("Status :");
            printDataService.send(countSpacing("Status :",
                    pStatus));
            printDataService.send(pStatus);
            printDataService.send("\n");

            printDataService.send("");
            printDataService.send(countSpacing("",
                    pCode));
            printDataService.send(pCode);
            printDataService.send("\n");
        }



        printDataService.setCommand(21);

        if (mName.indexOf("PIN") > -1) {


            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x68});
            printDataService.setByteCommand(new byte[]{70});
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x48});
            printDataService.setByteCommand(new byte[]{0x01});
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x6b});
            printDataService.setByteCommand(new byte[]{0x02});

            String barCode = FunctionUtil.getBarcode(
                    mRequestReloadPinObject.getsReloadTelco(),
                    mRequestReloadPinObject.getsAmount());
            DLog.e(TAG, "barCode 2-" + barCode);
            if (FunctionUtil.isSet(barCode))
                printDataService.send(barCode);

            printDataService.setCommand(2);
            printDataService.setCommand(22);
            printDataService.setCommand(21);

            printDataService.setCommand(3);
            printDataService.setCommand(4);

            printDataService.setCommand(21);
            // printDataService.send(mRequestReloadPinObject.getsReloadTelco()
            // + " " + "RM" + mRequestReloadPinObject.getsAmount());
            printDataService.send(pProduct + " " + "RM" +
                    mRequestReloadPinObject.getsAmount());
            printDataService.send("\n");
            printDataService.setCommand(20);
            printDataService.send("PIN :");
            printDataService.send("\n");
            printDataService.send(FunctionUtil.getPINFormat(mRequestReloadPinObject.getsReloadPin()));
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
            if (mName.indexOf("BILL") > -1) {
                printDataService.send(pReloadMSISDN);
                printDataService.send("\n");
                printDataService.send(mName + " " + "RM" + pAmount);
                printDataService.send("\n");
            } else {
                printDataService.send(pReloadMSISDN);
                printDataService.send("\n");
                if(mName.indexOf("FLEXI")>0){
                    printDataService.send(mName + " " + pAmount);
                    printDataService.send("\n");
                }else{
                    printDataService.send(mName + " " + "RM" + pAmount);
                    printDataService.send("\n");
                }

            }

        }

        printDataService.setCommand(3);
        printDataService.setCommand(20);
        printDataService.send("\n");

        if (mName.equalsIgnoreCase("CELCOM") || mName.equalsIgnoreCase("CELCOMPIN")) {
            printDataService.send("CELCOM Careline : +603 36308888 atau 1111");
            printDataService.send("\n");
        }
        if (mName.equalsIgnoreCase("MAXIS") || mName.equalsIgnoreCase("MAXISPIN")) {
            printDataService.send("MAXIS Careline : 1300-820-120 atau 123");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("DIGI") || mName.equalsIgnoreCase("DIGIPIN")) {
            printDataService.send("DiGi Careline : +6016 2211 800");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("UMOBILE") || mName.equalsIgnoreCase("UMOBILEPIN")) {
            printDataService.send("UMOBILE Careline : +6018 388 1318");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("TUNETALK") || mName.equalsIgnoreCase("TUNETALKPIN")) {
            printDataService.send("TUNETALK Careline : +603-79490000 atau 13100");
            printDataService.send("\n");
        }

        if (mName.equalsIgnoreCase("XOX") || mName.equalsIgnoreCase("XOXPIN")) {
            printDataService.send("XOX Careline : +603-7962 8000 atau 12273");
            printDataService.send("\n");
        }


        printDataService.send("Kupon Prepaid yg dibeli TIDAK boleh tukar ganti.");
        printDataService.send("\n");
//		printDataService.send(countSpacing("",
//				"(CUSTOMER COPY)"));
//		printDataService.send("(CUSTOMER COPY)");
//		printDataService.send("\n");
        printDataService.setCommand(21);
        printDataService.setCommand(21);


        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");
        printDataService.send(Config.Custotmer_website);
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");


        try {
            UpdatePrintCountAsync();
        }catch (Exception e){

        }

    }

    private void UpdatePrintCountAsync() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (mName.indexOf("PIN") > -1) {
                    DLog.e(TAG, "========================================= UpdatePrintCount");
                    String sClientUserName = SharedPreferenceUtil.getsClientUserName();
                    String sTS = FunctionUtil.getsDNReceivedID();
                    String sEncKey = FunctionUtil.getsEncK2(sClientUserName + "RichTech6318" + sTS);
                    rtWS.UpdatePrintCount(SharedPreferenceUtil.getsClientUserName(), pLocalMOId, sTS, sEncKey);

                }
                return true;
            }


            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                CheckCustomerTxStatus(sCustomerAccount);
            }
        }.execute();

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


    @Override
    protected void onDestroy() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        super.onDestroy();
    }
}

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.adapter.GridViewTextAdapter;
import com.rt.qpay99.bluetooth.service.BluetoothService;
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

import java.util.ArrayList;
import java.util.List;

public class PinDetailUI extends AppCompatActivity {

    private static String TELCO_GST_REG_ID = "";
    InputFilter[] filterArray = new InputFilter[1];
    String format = "dd-MM-yyyy hh:mm:ss";
    RequestPINObject mRequestPINObject = new RequestPINObject();
    RTWS rtWS = new RTWS();
    String mCurrency = " RM ";
    String mRM = "";
    private String TAG = this.getClass().getName();
    private TextView  tvBalance;
    private ProgressDialog pd;
    private Context mContext;
    private String Denomination, mName, mkeyword;
    private TextView tvProductConfirmBuy, tvProductName, tvSenderValue,
            tvSenderMobile, tvReprintLastPin;
    private List<String> denominationArray;

    private RequestReloadPinObject mRequestReloadPinObject = new RequestReloadPinObject();
    private String sCustomerMobileNumber, dProductPrice, sProductID,mLocalMoID,
            sCustomerAccountNumber;
    private GridView gdPinPrice;
    private GridViewTextAdapter adapter;
    private ViewFlipper productViewFlipper;
    private PrintDataService printDataService = null;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ImageView productImage;
    private String BILL = "";
    private double GST_Tax = 0.00;
    private boolean isShareVia = false;
    private RTWS rt = new RTWS();
    private int retryCount = 0;
    private boolean GetReloadPINImmediateResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_layout_product_pin);
        Bundle extras = getIntent().getExtras();
        Denomination = extras.getString("Denomination");
        DLog.e(TAG, "" + "" + Denomination);

        denominationArray = FunctionUtil.splitToStringList(Denomination, ",");
        // DLog.e(TAG, "" + "" + denominationArray.contains("300"));
        mName = extras.getString("Name");
        setTitle(mName);

        try {
            if (extras.getString("Tax") != null) {
                GST_Tax = Double.parseDouble(extras.getString("Tax"));
                DLog.e(TAG, "Tax " + GST_Tax);
            }
        } catch (Exception e) {
            DLog.w(TAG, "GST_Tax Err " + e.getMessage());
        }

        if (mName.equalsIgnoreCase("IDFLEXIPIN")) {
            denominationArray = new ArrayList<>();
            denominationArray.add("25k");
            denominationArray.add("10");
            mCurrency = "Rp.";
        }

        if (mName.equalsIgnoreCase("NPFLEXIPIN")) {
            denominationArray = new ArrayList<String>();
            denominationArray.add("100rp");
            denominationArray.add("200rp");
            denominationArray.add("5");
            denominationArray.add("10");
            mCurrency = "rp";
        }

        sProductID = extras.getString("pId");
        DLog.e(TAG, "" + "sProductID" + sProductID);


        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvReprintLastPin = (TextView) findViewById(R.id.tvReprintLastPin);
        tvReprintLastPin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DLog.e(TAG, "" + SharedPreferenceUtil.getLastPinNo());
                if (FunctionUtil.isSet(SharedPreferenceUtil.getLastPinNo()))
                    getGetReloadPIN(SharedPreferenceUtil.getsClientUserName(),
                            SharedPreferenceUtil.getsClientPassword(),
                            SharedPreferenceUtil.getLastPinNo(),"Duplicate Copied");
            }
        });

        tvSenderValue =  findViewById(R.id.tvSenderValue);
        tvSenderMobile =  findViewById(R.id.tvSenderMobile);
        tvProductName =  findViewById(R.id.tvProductName);
        tvProductConfirmBuy = findViewById(R.id.tvProductConfirmBuy);
        productImage =  findViewById(R.id.productImage);
        productImage.setImageResource(ImageUtil.setProductImages(mName, "TOPUP",mContext));


        tvProductConfirmBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Print ticket
                if(SharedPreferenceUtil.getPrinterName().equalsIgnoreCase("InnerPrinter:")){
                    printReceiptInnerPrinter("NEW PIN");
                }else
                    printReceipt("NEW PIN");
            }
        });



        productViewFlipper = (ViewFlipper) findViewById(R.id.productViewFlipper);

        tvProductName.setText(mName);



        gdPinPrice = (GridView) findViewById(R.id.gdPinPrice);
        adapter = new GridViewTextAdapter(mContext, denominationArray, GST_Tax,mName);
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
                if (mName.equalsIgnoreCase("IDFLEXIPIN")) {
                    if (dProductPrice.equalsIgnoreCase("RM10")) {
                        dProductPrice = "25";
                    }

                    if (dProductPrice.equalsIgnoreCase("25k")) {
                        dProductPrice = "25";
                    }
                    mRM = "k/RM10";
                    mCurrency = " ";
                }

                if (mName.equalsIgnoreCase("NPFLEXIPIN")) {
                    if (dProductPrice.equalsIgnoreCase("RM5")) {
                        dProductPrice = "100";
                        mRM = "rp/RM5";
                    }

                    if (dProductPrice.equalsIgnoreCase("100rp")) {
                        dProductPrice = "100";
                        mRM = "rp/RM5";
                    }

                    if (dProductPrice.equalsIgnoreCase("RM10")) {
                        dProductPrice = "200";
                        mRM = "rp/RM10";
                    }

                    if (dProductPrice.equalsIgnoreCase("200rp")) {
                        dProductPrice = "200";
                        mRM = "rp/RM10";
                    }
                    mCurrency = " ";

                }

                sCustomerMobileNumber = SharedPreferenceUtil.getsClientUserName().substring(1,
                        SharedPreferenceUtil.getsClientUserName().length());
                sCustomerAccountNumber = SharedPreferenceUtil.getsClientUserName().substring(1,
                        SharedPreferenceUtil.getsClientUserName().length());
                // productViewFlipper.showNext();
                if (PrintDataService.isPrinterConnected()) {
                    confirmBuyAlertDialog().show();
                } else {

                    connectPrinterAlertDialog().show();
                }


            }
        });
        if (FunctionUtil.isSet(SRSApp.printerMacAdd))
            printDataService = new PrintDataService(mContext,
                    SRSApp.printerMacAdd);

        getMemberBalance();

        //String barcode = getBarcode("DIGIPIN", "5");
        //DLog.e(TAG, " barcode " + barcode);
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
    private void printReceiptInnerPrinter(String sType) {

        try {
            UpdatePrintCountAsync();
        }catch (Exception e){

        }

        printDataService = new PrintDataService(mContext,
                SRSApp.printerMacAdd);

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
        printDataService.send("COUPON ");
        printDataService.setCommand(35);
        printDataService.setCommand(36);
        printDataService.setCommand(37);
        printDataService.send("\n");
        printDataService.send(SharedPreferenceUtil.getMerchantName());
        printDataService.send("\n");
        printDataService.setCommand(3);


        if (sType.equalsIgnoreCase("Re-print")) {
            printDataService.send("Date :");
            String sDate = FunctionUtil.convertString2Date(
                    mRequestReloadPinObject.getsPurchaseTS(),
                    "yyyy-MM-dd HH:mm:ss");
            DLog.e(TAG, "" + sDate);

            printDataService.send(FunctionUtil.countSpacing2("Date :", sDate));

            printDataService.send(sDate);
            printDataService.send("\n");
        } else {
            printDataService.send("Date :");
            String dateNow = FunctionUtil.getStrCurrentDateTime();
            printDataService.send(FunctionUtil.countSpacing2("Date :",
                    dateNow));
            printDataService.send(dateNow);
            printDataService.send("\n");

            printDataService.send("Batch :");
            printDataService.send(FunctionUtil.countSpacing2("Batch :",
                    mRequestReloadPinObject.getsBatchID()));
            printDataService.send(mRequestReloadPinObject.getsBatchID());
        }

        printDataService.send("Transaction No. :");
        printDataService.send(FunctionUtil.countSpacing2("Transaction No. :",
                mRequestReloadPinObject.getsDNReceivedID()));
        printDataService.send(mRequestReloadPinObject.getsDNReceivedID());
        printDataService.send("\n");

        printDataService.send("Serial No. :");
        printDataService.send(FunctionUtil.countSpacing2("Serial No. :",
                mRequestReloadPinObject.getsSerialNumber()));
        printDataService.send(mRequestReloadPinObject.getsSerialNumber());
        printDataService.send("\n");

        if (Config.printerName.equalsIgnoreCase("RichTech:")
                || Config.printerName.equalsIgnoreCase("Imprimer:")|| Config.printerName.equalsIgnoreCase("68topup:")
                || Config.printerName.equalsIgnoreCase("SRSMOBILE")
                || Config.printerName.equalsIgnoreCase("SRS MOBILE")
                || Config.printerName.equalsIgnoreCase("RichTech:")
                || Config.printerName.equalsIgnoreCase("MReload:")
                || Config.printerName.equalsIgnoreCase("SRS Mobile:")
                || Config.printerName.equalsIgnoreCase("SRS Mobile :")
                || Config.printerName.equalsIgnoreCase("SRSMobile")
                || Config.printerName.equalsIgnoreCase("SRSMobile:")
                || Config.printerName.equalsIgnoreCase("86topup:")
                || Config.printerName.equalsIgnoreCase("68Topup:")
                || Config.printerName.indexOf("ipos")>-1
                || Config.printerName.equalsIgnoreCase("MReload:")) {
            printDataService.setCommand(21);
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x68});
            printDataService.setByteCommand(new byte[]{70});
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x48});
            printDataService.setByteCommand(new byte[]{0x01});
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x6b});
            printDataService.setByteCommand(new byte[]{0x02});

            String barCode = getBarcode(
                    mRequestReloadPinObject.getsReloadTelco(),
                    mRequestReloadPinObject.getsAmount());
            DLog.e(TAG, "barCode 2-" + barCode);
            if (FunctionUtil.isSet(barCode))
                printDataService.send(barCode);

        }

//        printDataService.setCommand(2);
        printDataService.setCommand(22);
        printDataService.setCommand(3);
        printDataService.setCommand(4);
        printDataService.setCommand(21);
        printDataService.send(mRequestReloadPinObject.getsReloadTelco().replace("PIN", "") + " "
                + "RM" + mRequestReloadPinObject.getsAmount());
        printDataService.send("\n");

        printDataService.setCommand(20);
//        printDataService.setCommand(2);

        printDataService.send("PIN : ");
        printDataService.setCommand(4);
        printDataService.send("\n");
        printDataService.send(FunctionUtil.getPINFormat(mRequestReloadPinObject.getsReloadPin()));
        printDataService.setCommand(3);
        printDataService.send("\n");

//        printDataService.setCommand(2);
        printDataService.send("Expiry : ");
        if (mRequestReloadPinObject.getsExpiryDate() != null)
            printDataService.send(mRequestReloadPinObject.getsExpiryDate());
        printDataService.send("\n");
        printDataService.send("\n");

//        printDataService.setCommand(2);
        printDataService.send("Topup Instruction : ");
        printDataService.send("\n");
        if (mRequestReloadPinObject.getsDescription() != null)
            printDataService.send(mRequestReloadPinObject.getsDescription());
        printDataService.send("\n");

        if(mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("CELCOMPIN")){
            printDataService.send("CELCOM Careline : +6019 6011 111 atau 1111");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("MAXISPIN")) {
            printDataService.send("MAXIS Careline : 1300-820-120 atau 123");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("DIGIPIN")) {
            printDataService.send("DiGi Careline : +6016 2211 800");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("UMOBILEPIN")) {
            printDataService.send("UMOBILE Careline : +6018 388 1318");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("TUNETALKPIN")) {
            printDataService.send("TUNETALK Careline : +603-79490000 atau 13100");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("XOXPIN")) {
            printDataService.send("XOX Careline : +603-7962 8000 atau 12273");
            printDataService.send("\n");
        }

        printDataService.send("Kupon Prepaid yg dibeli TIDAK boleh tukar ganti.");
        printDataService.send("\n");
        printDataService.send(FunctionUtil.countSpacing2("",
                "(CUSTOMER COPY)"));
        printDataService.send("(CUSTOMER COPY)");
        printDataService.send("\n");

        printDataService.send("\n");
        printDataService.setCommand(21);
//        printDataService.send("Customer Care Line : " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");
//        printDataService.send(Config.Custotmer_website);
        printDataService.send("\n");
        printDataService.send("Thank You " + "V." + SRSApp.versionName);
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");




    }

    private void printReceipt(String sType) {

        try {
            UpdatePrintCountAsync();
        }catch (Exception e){

        }
        printDataService = new PrintDataService(mContext,
                SRSApp.printerMacAdd);
        printDataService.setCommand(21);
        printDataService.send("\n");
        printDataService.setCommand(23);
        printDataService.setCommand(4);
        printDataService.setCommand(21);
        //printDataService.printImage();

        if(SharedPreferenceUtil.getsClientID()!=545){
            printDataService.send(mContext.getResources().getString(R.string.app_name) + " ");
            printDataService.setCommand(4);
            printDataService.setCommand(20);
            printDataService.send("COUPON ");

            printDataService.send("\n");
        }

        printDataService.setCommand(35);
        printDataService.setCommand(36);
        printDataService.setCommand(37);
        printDataService.send(SharedPreferenceUtil.getMerchantName());
        printDataService.send("\n");
        printDataService.setCommand(3);
        printDataService.setCommand(2);

//		printDataService.send("Merchant ID :");
//		printDataService.send(countSpacing("Merchant ID :",
//				SharedPreferenceUtil.getsClientUserName()));
//		printDataService.send(SharedPreferenceUtil.getsClientUserName());
//		printDataService.send("\n");

        if (sType.equalsIgnoreCase("Re-print")) {
            printDataService.send("Date :");
            String sDate = FunctionUtil.convertString2Date(
                    mRequestReloadPinObject.getsPurchaseTS(),
                    "yyyy-MM-dd HH:mm:ss");
            DLog.e(TAG, "" + sDate);

            printDataService.send(countSpacing("Date :", sDate));

            printDataService.send(sDate);
            printDataService.send("\n");
        } else {
            printDataService.send("Date :");
            String dateNow = FunctionUtil.getStrCurrentDateTime();
            printDataService.send(FunctionUtil.countSpacing("Date :",
                    dateNow));
            printDataService.send(dateNow);
            printDataService.send("\n");

            printDataService.send("Batch :");
            printDataService.send(FunctionUtil.countSpacing("Batch :",
                    mRequestReloadPinObject.getsBatchID()));
            printDataService.send(mRequestReloadPinObject.getsBatchID());
        }

        printDataService.send("Transaction No. :");
        printDataService.send(countSpacing("Transaction No. :",
                mRequestReloadPinObject.getsDNReceivedID()));
        printDataService.send(mRequestReloadPinObject.getsDNReceivedID());
        printDataService.send("\n");

        printDataService.send("Serial No. :");
        printDataService.send(countSpacing("Serial No. :",
                mRequestReloadPinObject.getsSerialNumber()));
        printDataService.send(mRequestReloadPinObject.getsSerialNumber());
        printDataService.send("\n");

        if (Config.printerName.equalsIgnoreCase("RichTech:")
                || Config.printerName.equalsIgnoreCase("Imprimer:")|| Config.printerName.equalsIgnoreCase("68topup:")
                || Config.printerName.equalsIgnoreCase("SRSMOBILE")
                || Config.printerName.equalsIgnoreCase("SRS MOBILE")
                || Config.printerName.equalsIgnoreCase("RichTech:")
                || Config.printerName.equalsIgnoreCase("MReload:")
                || Config.printerName.equalsIgnoreCase("SRS Mobile:")
                || Config.printerName.equalsIgnoreCase("SRS Mobile :")
                || Config.printerName.equalsIgnoreCase("SRSMobile")
                || Config.printerName.equalsIgnoreCase("SRSMobile:")
                || Config.printerName.equalsIgnoreCase("86topup:")
                || Config.printerName.equalsIgnoreCase("68Topup:")
                || Config.printerName.equalsIgnoreCase("MReload:")) {
            printDataService.setCommand(21);
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x68});
            printDataService.setByteCommand(new byte[]{70});
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x48});
            printDataService.setByteCommand(new byte[]{0x01});
            printDataService.setByteCommand(new byte[]{0x1d});
            printDataService.setByteCommand(new byte[]{0x6b});
            printDataService.setByteCommand(new byte[]{0x02});

            String barCode = getBarcode(
                    mRequestReloadPinObject.getsReloadTelco(),
                    mRequestReloadPinObject.getsAmount());
            DLog.e(TAG, "barCode 2-" + barCode);
            if (FunctionUtil.isSet(barCode))
                printDataService.send(barCode);

        }

        printDataService.setCommand(2);
        printDataService.setCommand(22);
        printDataService.setCommand(3);
        printDataService.setCommand(4);
        printDataService.setCommand(21);
        printDataService.send(mRequestReloadPinObject.getsReloadTelco().replace("PIN", "") + " "
                + "RM" + mRequestReloadPinObject.getsAmount());
        printDataService.send("\n");

        printDataService.setCommand(20);
        printDataService.setCommand(2);

        printDataService.send("PIN : ");
        printDataService.setCommand(4);
        printDataService.send("\n");
        printDataService.send(FunctionUtil.getPINFormat(mRequestReloadPinObject.getsReloadPin()));
        printDataService.setCommand(3);
        printDataService.send("\n");

        printDataService.setCommand(2);
        printDataService.send("Expiry : ");
        if (mRequestReloadPinObject.getsExpiryDate() != null)
            printDataService.send(mRequestReloadPinObject.getsExpiryDate());
        printDataService.send("\n");
        printDataService.send("\n");

        printDataService.setCommand(2);
        printDataService.send("Topup Instruction : ");
        printDataService.send("\n");
        if (mRequestReloadPinObject.getsDescription() != null)
            printDataService.send(mRequestReloadPinObject.getsDescription());
        printDataService.send("\n");

        if(mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("CELCOMPIN")){
            printDataService.send("CELCOM Careline : +6019 6011 111 atau 1111");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("MAXISPIN")) {
            printDataService.send("MAXIS Careline : 1300-820-120 atau 123");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("DIGIPIN")) {
            printDataService.send("DiGi Careline : +6016 2211 800");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("UMOBILEPIN")) {
            printDataService.send("UMOBILE Careline : +6018 388 1318");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("TUNETALKPIN")) {
            printDataService.send("TUNETALK Careline : +603-27720000 atau 13100");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("XOXPIN")) {
            printDataService.send("XOX Careline : 1300 888 010");
            printDataService.send("\n");
        }

        if (mRequestReloadPinObject.getsReloadTelco().equalsIgnoreCase("MOL") || mName.equalsIgnoreCase("MOLPIN")) {
            printDataService.send("MOL Careline : +03 2148 3777");
            printDataService.send("\n");
        }

        printDataService.send("Kupon Prepaid yg dibeli TIDAK boleh tukar ganti.");
        printDataService.send("\n");
        printDataService.send(countSpacing("",
                "(CUSTOMER COPY)"));
        printDataService.send("(CUSTOMER COPY)");
        printDataService.send("\n");

        printDataService.send("\n");
        printDataService.setCommand(21);
//        printDataService.send("Customer Care Line : " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");
//        printDataService.send(Config.Custotmer_website);
        printDataService.send("\n");
        printDataService.send("Thank You " + "V." + SRSApp.versionName);
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");




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
                    rtWS.UpdatePrintCount(SharedPreferenceUtil.getsClientUserName(), sLocalMoID, sTS, sEncKey);
                }
                return true;
            }
        }.execute();

    }

    private String getTelcoGSTID(String mProduct) {
        DLog.e(TAG, "mProduct " + mProduct);
        TELCO_GST_REG_ID = "";
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

        if (mProduct.equalsIgnoreCase("NJOIPIN")
                || mProduct.equalsIgnoreCase("NJOI")) {


            if (mValue.equalsIgnoreCase("10"))
                return Config.NJOI10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.NJOI30;

            if (mValue.equalsIgnoreCase("20"))
                return Config.NJOI20;

            if (mValue.equalsIgnoreCase("50"))
                return Config.NJOI50;

        }

        if (mProduct.equalsIgnoreCase("DIGIPIN")
                || mProduct.equalsIgnoreCase("DIGI")) {
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
            if (mValue.equalsIgnoreCase("5"))
                return Config.MAXIS_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.MAXIS_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.MAXIS_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.MAXIS_RM50;

            if (mValue.equalsIgnoreCase("60"))
                return Config.MAXIS_RM60;

            if (mValue.equalsIgnoreCase("100"))
                return Config.MAXIS_RM100;
        }

        if (mProduct.equalsIgnoreCase("CELCOMPIN")
                || mProduct.equalsIgnoreCase("CELCOM")) {
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

            if (mValue.equalsIgnoreCase("5"))
                return Config.UMOBILE_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.UMOBILE_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.UMOBILE_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.UMOBILE_RM50;

            if (mValue.equalsIgnoreCase("100"))
                return Config.UMOBILE_RM100;

        }

        if (mProduct.equalsIgnoreCase("TUNETALKPIN")
                || mProduct.equalsIgnoreCase("TUNETALK")) {

            if (mValue.equalsIgnoreCase("5"))
                return Config.TUNETALK_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.TUNETALK_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.TUNETALK_RM30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.TUNETALK_RM50;

            if (mValue.equalsIgnoreCase("100"))
                return Config.TUNETALK_RM100;

        }

        if (mProduct.equalsIgnoreCase("MECHANTRADEPIN")
                || mProduct.equalsIgnoreCase("MECHANTRADE")) {

            if (mValue.equalsIgnoreCase("5"))
                return Config.MERCHANTRADE_RM5;

            if (mValue.equalsIgnoreCase("10"))
                return Config.MERCHANTRADE_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.MERCHANTRADE_RM30;

        }

        if (mProduct.equalsIgnoreCase("CLIXSTERPIN")
                || mProduct.equalsIgnoreCase("CLIXSTERP")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.CLIXSTER_RM10;

            if (mValue.equalsIgnoreCase("30"))
                return Config.CLIXSTER_RM30;

        }

        if (mProduct.equalsIgnoreCase("XOXPIN")
                || mProduct.equalsIgnoreCase("XOX")) {

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

        if (mProduct.equalsIgnoreCase("TMGOPIN")
                || mProduct.equalsIgnoreCase("TMGO")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.TMGO10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.TMGO20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.TMGO30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.TMGO50;

        }

        if (mProduct.equalsIgnoreCase("NJOIPIN")
                || mProduct.equalsIgnoreCase("NJOI")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.NJOI10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.NJOI20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.NJOI30;

            if (mValue.equalsIgnoreCase("50"))
                return Config.NJOI50;

        }

        if (mProduct.equalsIgnoreCase("ITALKPIN")
                || mProduct.equalsIgnoreCase("ITALK")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.ITALK10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.ITALK20;

            if (mValue.equalsIgnoreCase("30"))
                return Config.ITALK30;

        }


        if (mProduct.equalsIgnoreCase("GRABPIN")
                || mProduct.equalsIgnoreCase("GRAB")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.GRAB10;

            if (mValue.equalsIgnoreCase("20"))
                return Config.GRAB20;

            if (mValue.equalsIgnoreCase("50"))
                return Config.GRAB50;

        }

        if (mProduct.equalsIgnoreCase("LEBARAPIN")
                || mProduct.equalsIgnoreCase("LEBARA")) {

            if (mValue.equalsIgnoreCase("10"))
                return Config.LEBARA_RM10;

            if (mValue.equalsIgnoreCase("15"))
                return Config.LEBARA_RM15;
        }


        if (mProduct.equalsIgnoreCase("ONEMYPIN")
                || mProduct.equalsIgnoreCase("ONEMY")) {
            return Config.ONEMYPIN;

        }

        return "";

    }

    private String sLocalMoID = "";
    private String sClientTxID="";
    private String sOtherParameter;
    private void RequestInputAsync() {
        sOtherParameter = "CODE=BUYPIN";
        new AsyncTask<Void, Void, RequestInputResponse>() {

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
                pd.setMessage("Verifying ...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected RequestInputResponse doInBackground(Void... params) {
                retryCount = 0;
                DLog.e(TAG, "sCustomerAccountNumber : "
                        + sCustomerAccountNumber);
                DLog.e(TAG, "sCustomerMobileNumber :  " + sCustomerMobileNumber);
                DLog.e(TAG, "dProductPrice  : " + dProductPrice);
                DLog.e(TAG, "sProductID : " + sProductID);
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
                sClientTxID = FunctionUtil.getStringDateTimeSec();

                return rtWS.RequestInput(sCustomerAccountNumber,
                        sCustomerMobileNumber, dProductPrice, sProductID,
                        "ANDROID", sClientTxID, sOtherParameter,
                        "", "","");
            }

            @Override
            protected void onPostExecute(RequestInputResponse result) {
                super.onPostExecute(result);
                if (pd != null)
                    if (pd.isShowing()) pd.dismiss();
                if (result != null) {
                    if (Config.WS_SUBMIT_SUCCESS.equalsIgnoreCase(result
                            .getsResponseStatus())) {
                        DLog.e(TAG, "" + result.getsResponseID());
                        DLog.e(TAG, "" + result.getsResponseStatus());
                        SharedPreferenceUtil.editLastPinNo("");
                        SharedPreferenceUtil.editLastPinNo(result
                                .getsResponseID());

                        if (sOtherParameter.equalsIgnoreCase("CODE=BUYPIN")) {
                            DLog.e(TAG, "getGetReloadPIN ===========>");
                            getGetReloadPIN(SharedPreferenceUtil.getsClientUserName(),
                                    SharedPreferenceUtil.getsClientPassword(),
                                    result.getsResponseID(), "NEW PIN");
                            return;
                        } else {
                            DLog.e(TAG, "GetReloadPINImmediate ===========>");
                            GetReloadPINImmediate(SharedPreferenceUtil.getsClientUserName(),
                                    SharedPreferenceUtil.getsClientPassword(),
                                    result.getsResponseID());
                            return;
                        }
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
                } else {
                    if (sOtherParameter.equalsIgnoreCase("CODE=COUPON")) {
                        DLog.e(TAG, "result = null ==========================>");
                        GetReloadPINImmediate(
                                SharedPreferenceUtil.getsClientUserName(),
                                SharedPreferenceUtil.getsClientPassword(),
                                sClientTxID);
                        return;
                    }
                }
                transcationFailedAlertDialog(
                        "Transaction Failed. Please try again later.").show();

            }
        }.execute();

    }

    private void getGetReloadPIN(final String sClientUserName,
                                 final String sClientPassword, final String sLocalMOID, final String sType) {
        new AsyncTask<Void, Void, RequestReloadPinObject>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext);
                SpannableString ss1 = new SpannableString("Get PIN ... ");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                        ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ss1.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#008000")),
                        0, ss1.length(), 0);
                pd = new ProgressDialog(mContext);
                pd.setMessage("Verifying ... " + retryCount);
                pd.setTitle(ss1);
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected RequestReloadPinObject doInBackground(Void... params) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DLog.e(TAG, "sCustomerAccountNumber : "
                        + sCustomerAccountNumber);
                DLog.e(TAG, "sCustomerMobileNumber :  " + sCustomerMobileNumber);
                DLog.e(TAG, "sLocalMOID  : " + sLocalMOID);
                mLocalMoID = sLocalMOID;
                return rtWS.getReloadPIN(sClientUserName, sClientPassword,
                        sLocalMOID);

            }

            @Override
            protected void onPostExecute(RequestReloadPinObject result) {
                super.onPostExecute(result);
                if (pd != null)
                    if (pd.isShowing()) pd.dismiss();
                mRequestReloadPinObject = result;
                if (result.isGetReloadPINResult()) {
                    if (result.getsReloadPin() != null) {
                        if (isShareVia) {
                            Intent intent = new Intent(mContext, ShareViaUI.class);
                            intent.putExtra("mName", mName);
                            intent.putExtra("mType", "NEW PIN");
                            intent.putExtra("mCat", "PIN");
                            mRequestReloadPinObject.setsLocalMOID(sLocalMOID);
                            SharedPreferenceUtil.editShareViaRelaodPin(mRequestReloadPinObject);
                            mContext.startActivity(intent);
                            return;
                        }

                        DLog.e(TAG, "" + result.getsReloadPin());
                        DLog.e(TAG, "" + result.getsSerialNumber());
                        DLog.e(TAG, "" + result.getsDNReceivedID());
                        DLog.e(TAG, "" + result.getsAmount());
                        DLog.e(TAG, "" + result.getsReloadTelco());
                        if (PrintDataService.isPrinterConnected()) {
                            if (PrintDataService.isPrinterConnected()) {
                                if (FunctionUtil.isPDA(SharedPreferenceUtil.getPrinterName())) {
                                    printReceiptInnerPrinter("sType");
                                } else
                                    printReceipt("sType");
                            }
                        } else
                            transcationFailedAlertDialog(
                                    "Printer connecting failed!!!!").show();
                        return;

                    }
                }

                if (retryCount != 2) {
                    retryCount++;
                    getGetReloadPIN(SharedPreferenceUtil.getsClientUserName(),
                            SharedPreferenceUtil.getsClientPassword(),
                            sLocalMOID, sType);
                    return;
                }
                transcationFailedAlertDialog(
                        "Transaction Failed. Please try again later.").show();

            }
        }.execute();

    }


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
                if (pd.isShowing()) pd.dismiss();
                mRequestReloadPinObject = result;
                if(result!=null)
                    if (result.isGetReloadPINResult()) {
                        if (result.getsReloadPin() != null) {
                            DLog.e(TAG, "" + result.getsReloadPin());
                            DLog.e(TAG, "" + result.getsSerialNumber());
                            GetReloadPINImmediateResult = true;
                            if (isShareVia) {
                                Intent intent = new Intent(mContext, ShareViaUI.class);
                                intent.putExtra("mName", mName);
                                intent.putExtra("mType", "NEW PIN");
                                mRequestReloadPinObject.setsLocalMOID(sLocalMOID);
                                SharedPreferenceUtil.editShareViaRelaodPin(mRequestReloadPinObject);
                                mContext.startActivity(intent);
                                return;
                            }
                            if (PrintDataService.isPrinterConnected()) {
                                if(SharedPreferenceUtil.getPrinterName().equalsIgnoreCase("InnerPrinter:")){
                                    printReceiptInnerPrinter("NEW PIN");
                                }else
                                    printReceipt("NEW PIN");
                            } else
                                transcationFailedAlertDialog(
                                        "Connecting failed!!!!").show();
                            return;
                        }
                    }
                //retryCount = 1; //skip retry 20150805
                if (retryCount == 0) {
                    retryCount = 1;
                    DLog.e(TAG, "GetReloadPINImmediate RETRY ======================> 1");
                    GetReloadPINImmediate(
                            sClientUserName,
                            sClientPassword,
                            sLocalMOID);
                } else {
                    transcationFailedAlertDialog(
                            "Transaction Failed. Please try again later.").show();

                }
            }
        }.execute();

    }

    private AlertDialog transcationFailedAlertDialog(String msg) {
        return new AlertDialog.Builder(this)
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
        return new AlertDialog.Builder(this)
                .setTitle(mName)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (PrintDataService.isPrinterConnected())
                                    printReceipt("NEW PIN");
                                // finish();
                            }
                        }).create();
    }

    private AlertDialog connectPrinterAlertDialog() {
        return new AlertDialog.Builder(this)
                .setTitle(mName + " - No printer connected!!")
                .setMessage("Connect NOW or Share Via")
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                isShareVia = false;
                                if (SharedPreferenceUtil.isRequiredPrinter()) {
                                    if (SRSApp.printerMacAdd.length() < 1) {
                                        BluetoothService.setmContext(mContext);
                                        BluetoothService.getBOundedDevices(mContext);
                                        return;
                                    } else {
                                        if (!PrintDataService.isPrinterConnected()) {
                                            printDataService = new PrintDataService(mContext,
                                                    SRSApp.printerMacAdd);
                                            if (!printDataService.connect()) {
                                                BluetoothService.setmContext(mContext);
                                                BluetoothService.getBOundedDevices(mContext);
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                        })
                .setNegativeButton("Share Receipt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        isShareVia = true;
                        String masterId = SharedPreferenceUtil.getClientMasterId();
                        if(masterId.equalsIgnoreCase("566") || masterId.equalsIgnoreCase("545") ){
                            DLog.e(TAG,"" + "Share Receipt Disable" + masterId);

                        }else{
                            DLog.e(TAG,"" + "Share Receipt " + masterId);
                            RequestInputAsync();
                        }


                    }
                })
                .create();
    }

    private AlertDialog confirmBuyAlertDialog() {
        return new AlertDialog.Builder(this)
                .setTitle(mName + mCurrency + dProductPrice + mRM)
                .setMessage("Please click OK to proceed.")
                .setCancelable(false)
                .setPositiveButton(R.string.Print_1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                isShareVia = false;
                                RequestInputAsync();
                            }
                        })
                .setNeutralButton("Share Receipt", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        isShareVia = true;
                        RequestInputAsync();
                    }
                })
                .setNegativeButton(R.string.title_cancel, null)
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
        final EditText input = new EditText(PinDetailUI.this);
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
                transcationSuccessAlertDialog(
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

}

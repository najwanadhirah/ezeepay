package com.rt.qpay99.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rt.qpay99.Config;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.activity.ui.AgentMap;
import com.rt.qpay99.activity.ui.LoginUI;
import com.rt.qpay99.activity.ui.ShareReceiptUI;
import com.rt.qpay99.adapter.SettingRecycleAdapter;
import com.rt.qpay99.bluetooth.service.BluetoothService;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SettingFragment_v2UI extends Fragment {

    private String TAG = this.getClass().getName();

    private RecyclerView gd;
    private AdView mAdView;
    ScrollTextView scrolltext;
    private Context mContext;

    public SettingFragment_v2UI() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        DLog.e(TAG, " onCreate ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycleview_setting,
                container, false);
        mContext = this.getActivity();
        MobileAds.initialize(getActivity().getApplicationContext(),
                "ca-app-pub-7069957738539410/8983539706");

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        gd = rootView.findViewById(R.id.gdTopup);
        gd.setHasFixedSize(true);
        int numberOfColumns = FunctionUtil.calculateNoOfColumns(getActivity(), 120);
//        gd.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        gd.setLayoutManager(new LinearLayoutManager(getActivity()));
        gd.setAdapter(new SettingRecycleAdapter(mContext, new mRecyclerViewOnItemClick()));

        scrolltext = rootView.findViewById(R.id.scrolltext);
        scrolltext.setText(Config.MOVING_TEXT);
        scrolltext.startScroll();

//        adapter = new SettingGridViewImageAdapter(this.getActivity());
//        gd.setAdapter(adapter);
//        gd.setOnItemClickListener(new SettingGridviewOnItemClickListener(this
//                .getActivity(), getFragmentManager(), new SettingFragmentUI.logoutListener()));

        return rootView;
    }

    private final int CASE_HELP = 0;
    private final int CASE_SHARE = 1;
    private final int CASE_UPDATE_APP = 2;
    private final int CASE_UPDATE_PRODUCT = 3;
    private final int CASE_SELECT_PRINTER = 4;
    private final int CASE_VERIFY_PASSWORD = 5;
    private final int CASE_TEST_PRINT = 6;
    // private final int CASE_UPDATE_PN = 7;
    private final int CASE_GOOGLE_MAP = 7;
    private final int CASE_PERMISSION = 8;
    private final int CASE_LOGOUT = 9;

    private List<ProductInfo> productInfos;
    private List<ProductInfo> topUps = new ArrayList<ProductInfo>();
    private List<ProductInfo> topUps_os = new ArrayList<ProductInfo>();
    private List<ProductInfo> payBills = new ArrayList<ProductInfo>();
    private List<ProductInfo> pins = new ArrayList<ProductInfo>();
    private List<ProductInfo> sims = new ArrayList<ProductInfo>();
    private List<ProductInfo> datas = new ArrayList<ProductInfo>();
    private List<ProductInfo> QPromoList = new ArrayList<ProductInfo>();
    private ProgressDialog pd;

    private Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private PrintDataService printDataService = null;
    RTWS cs = new RTWS();

    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            DLog.e(TAG, "ProductGridviewOnItemClickListener");
            final String appPackageName = mContext.getPackageName();
            switch (position) {
                case CASE_HELP:
                    String mobileNo = Config.Custotmer_care;
                    showAlertDialog("SRS HELP",
                            "For asistance please call " + mobileNo).show();
                    break;
                case CASE_SHARE:
                    Intent intent = null;
                    intent = new Intent(mContext, ShareReceiptUI.class);
                    mContext.startActivity(intent);

                    break;
                case CASE_UPDATE_APP:
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse("http://play.google.com/store/apps/details?id="
                                        + appPackageName)));
                    }
                    break;
                case CASE_UPDATE_PRODUCT:
                    GetAgentProductDiscount();
//                new retrieveProductInfoAsync().execute();
                    break;
                case CASE_SELECT_PRINTER:
                    BluetoothService.setmContext(mContext);
                    BluetoothService.getBOundedDevices(mContext);
                    break;

                case CASE_VERIFY_PASSWORD:
                    changePasswordAlertDialog("Please insert old password").show();


                    break;
                case CASE_TEST_PRINT:

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
                        if (FunctionUtil.isPDA(SharedPreferenceUtil.getPrinterName())) {
                            printReceiptInnerPrinter();
                        } else
                            printReceipt();

                    }

                    break;

                case CASE_GOOGLE_MAP:
                    showAgentMapDialog(mContext.getString(R.string.app_name), "Please enter password").show();
                    break;

                case CASE_PERMISSION:
                    intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri = Uri.fromParts("package", appPackageName, null);
                    intent.setData(uri);
                    mContext.startActivity(intent);
                    break;

                case CASE_LOGOUT:
                    new LogoutAsync().execute();
                    break;

            }
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        DLog.e(TAG, " onResume");
        scrolltext.setText(Config.MOVING_TEXT);
    }

    private AlertDialog showAgentMapDialog(final String mTitle, final String msg) {
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle(mTitle)
                .setMessage(msg)
                .setView(input)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (!TextUtils.isEmpty(input.getText()))
                                    if (input.getText().toString().equalsIgnoreCase("22882288")) {
                                        Intent intent = new Intent(mContext, AgentMap.class);
                                        if (SharedPreferenceUtil.getsClientUserName().equalsIgnoreCase("60166572577")) {
                                            mContext.startActivity(intent);
                                        } else {
                                            Toast.makeText(mContext, "Haha, Please contact admin", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                            }
                        }).create();
    }

    private void printReceiptInnerPrinter() {
        printDataService = new PrintDataService(mContext, SRSApp.printerMacAdd);
        printDataService.setByteCommand(new byte[]{0x1b});
        printDataService.setByteCommand(new byte[]{0x4d});
        printDataService.setByteCommand(new byte[]{0x00});

        printDataService.setCommand(21);
        printDataService.send("\n");
        printDataService.setCommand(23);
        printDataService.setCommand(4);
        printDataService.setCommand(21);
//        printDataService.printImage();
        printDataService.send(mContext.getResources().getString(R.string.app_name) + " ");
        printDataService.setCommand(4);
        printDataService.setCommand(20);
        printDataService.send("\n");
        printDataService.send("COUPON");


        printDataService.setCommand(35);
        printDataService.setCommand(36);
        printDataService.setCommand(37);
        printDataService.send("\n");
        printDataService.send(SharedPreferenceUtil.getMerchantName());
        printDataService.send("\n");
        printDataService.setCommand(3);
//        printDataService.setCommand(2);

        printDataService.send("Merchant ID :");
        printDataService.send(FunctionUtil.countSpacing2("Merchant ID :",
                SharedPreferenceUtil.getsClientUserName()));
        printDataService.send(SharedPreferenceUtil.getsClientUserName());
        printDataService.send("\n");

        printDataService.send("Date :");
        printDataService.send(FunctionUtil.countSpacing2("Date :",
                FunctionUtil.getStrCurrentDateTime()));
        printDataService.send(FunctionUtil.getStrCurrentDateTime());
        printDataService.send("\n");

        printDataService.send("Serial No. :");
        printDataService.send(FunctionUtil.countSpacing2("Serial No. :", "100000001"));
        printDataService.send("100000001");
        printDataService.send("\n");

//        printDataService.setCommand(2);
        printDataService.setCommand(22);
        printDataService.setCommand(3);
        printDataService.setCommand(4);
        printDataService.setCommand(21);


//        printDataService.setByteCommand(new byte[]{0x1d});
//        printDataService.setByteCommand(new byte[]{0x68});
//        printDataService.setByteCommand(new byte[]{70}); //Barcode height
//
//        printDataService.setByteCommand(new byte[]{0x1d});
//        printDataService.setByteCommand(new byte[]{0x48});
//        printDataService.setByteCommand(new byte[]{0x02}); //printing position for HRI characters
//
//
//        printDataService.setByteCommand(new byte[]{0x1d});
//        printDataService.setByteCommand(new byte[]{0x6b});
//        printDataService.setByteCommand(new byte[]{0x02});
//        printDataService.send(Config.DIGI_RM10);
//        printDataService.setByteCommand(new byte[]{0x00});
//        printDataService.send("\n");


        printDataService.send("\n");
        printDataService.send("SAMPLE" + " " + "RM" + "5");
        printDataService.send("\n");

        printDataService.setCommand(20);
        printDataService.send("PIN :");
        printDataService.send("\n");
        printDataService.send(FunctionUtil.getPINFormat("1234567890123456"));
        printDataService.setCommand(3);
        printDataService.send("\n");

//        printDataService.setCommand(2);
        printDataService.send("Pin Expired Date : 2020-01-01");
        printDataService.send("\n");
        printDataService.send("\n");

//        printDataService.setCommand(2);
        printDataService.send("Topup Instruction :");
        printDataService.send("\n");

        printDataService.send("Key in *000*<16-digit reload PIN>#, press CALL");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.setCommand(21);
        printDataService.send("--------------------------------");
//        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");

        printDataService.send("Thank You");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");

    }

    private void printReceipt() {
        printDataService = new PrintDataService(mContext, SRSApp.printerMacAdd);
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
        printDataService.setCommand(2);

        printDataService.send("Merchant ID :");
        printDataService.send(countSpacing("Merchant ID :",
                SharedPreferenceUtil.getsClientUserName()));
        printDataService.send(SharedPreferenceUtil.getsClientUserName());
        printDataService.send("\n");

        printDataService.send("Date :");
        printDataService.send(countSpacing("Date :",
                FunctionUtil.getStrCurrentDateTime()));
        printDataService.send(FunctionUtil.getStrCurrentDateTime());
        printDataService.send("\n");

        printDataService.send("Serial No. :");
        printDataService.send(countSpacing("Serial No. :", "100000001"));
        printDataService.send("100000001");
        printDataService.send("\n");

        printDataService.send("\n");


//        printDataService.setCommand(23);
//        printDataService.setCommand(24);
//        printDataService.setCommand(25);
//        printDataService.setCommand(26);
//        printDataService.setCommand(32);
//        printDataService.send(Config.DIGI_RM5 );

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
        printDataService.send(Config.UMOBILE_RM5);
        printDataService.send("\n");

        printDataService.setCommand(2);
        printDataService.setCommand(22);
        printDataService.setCommand(3);
        printDataService.setCommand(4);
        printDataService.setCommand(21);

        printDataService.send("SAMPLE" + " " + "RM" + "5");
        printDataService.send("\n");

        printDataService.setCommand(20);
        printDataService.send("PIN :");
        printDataService.send("\n");
        printDataService.send(FunctionUtil.getPINFormat("1234567890123456"));
        printDataService.setCommand(3);
        printDataService.send("\n");

        printDataService.setCommand(2);
        printDataService.send("Pin Expired Date : 2020-01-01");
        printDataService.send("\n");
        printDataService.send("\n");

        printDataService.setCommand(2);
        printDataService.send("Topup Instruction :");
        printDataService.send("\n");

        printDataService.send("Key in *000*<16-digit reload PIN>#, press CALL");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.setCommand(21);
        printDataService.send("------------------------------------------");
        printDataService.send("Customer Care Line: " + Config.Custotmer_care);
        printDataService.send("\n");
        printDataService.send("9AM - 6PM (Monday - Friday)");
        printDataService.send("\n");
        printDataService.send("Thank You");
        printDataService.send("\n");
        printDataService.send("\n");
        printDataService.send("\n");
    }

    private void GetAgentProductDiscount() {

        new AsyncTask<String, Void, List<AgentProductDiscount>>() {

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

            protected List<AgentProductDiscount> doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                return rtWS.GetAgentProductDiscount(SharedPreferenceUtil.getsClientUserName(),
                        SharedPreferenceUtil.getsClientPassword());
            }

            @Override
            protected void onPostExecute(List<AgentProductDiscount> result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                SRSApp.DiscountRates = result;

                for (AgentProductDiscount p : result) {
                    SRSApp.hashmapDiscountRate.put(p.getProductName(), p);
                }


                if (pd != null) {
                    pd.dismiss();
                    pd = null;
                }
                new retrieveProductInfoAsync().execute();


            }

        }.execute();

    }

    public class retrieveProductInfoAsync extends
            AsyncTask<String, Void, List<ProductInfo>> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
            SpannableString ss1 = new SpannableString("Please wait ...");
            ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
            ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                    ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                    0, ss1.length(), 0);
            pd = new ProgressDialog(mContext);
            pd.setTitle(ss1);
            pd.setMessage("Downloading Data ...");
            pd.setCancelable(true);
            pd.show();
        }

        protected List<ProductInfo> doInBackground(String... arg0) {
            return cs.getProductInfo();
        }

        protected void onPostExecute(List<ProductInfo> result) {

            //prevent duplicate data
            pins = new ArrayList<ProductInfo>();
            payBills = new ArrayList<ProductInfo>();
            topUps_os = new ArrayList<ProductInfo>();
            sims = new ArrayList<ProductInfo>();
            datas = new ArrayList<ProductInfo>();
            QPromoList = new ArrayList<ProductInfo>();
            topUps = new ArrayList<ProductInfo>();

            for (ProductInfo product : result) {
//                if (product.getName().toLowerCase().substring(0,product.getName().length()-3)=="pin") {
                if (product.getName().toLowerCase().indexOf("pin") > 0) {
                    DLog.e("PIN", product.getName());
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        pins.add(product);
                } else if (product.getName().toLowerCase().indexOf("bill") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        payBills.add(product);
                    DLog.e("BILL", product.getName());
                } else if (product.getName().toLowerCase().indexOf("flexi") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        topUps_os.add(product);
                    DLog.e("FLEXI", product.getName());
                } else if (product.getName().toLowerCase().indexOf("sim") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        sims.add(product);
                    DLog.e("SIM", product.getName());
                } else if (product.getName().toLowerCase().indexOf("data") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        datas.add(product);
                    DLog.e("SIM", product.getName());
                } else if (product.getName().toLowerCase().indexOf("_qpromo") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        QPromoList.add(product);
                    DLog.e("QPromoList", product.getName());
                } else {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus())) {
                        if (!product.getName().equalsIgnoreCase("QDRAW"))
                            topUps.add(product);
//                        if (product.getName().equalsIgnoreCase("MAXIS")) {
//                            Config.GST_RATE = product.getTax();
//                        }
                    }

                    DLog.e("ORTHES", product.getName());
                }
                SharedPreferenceUtil.editPRODUCT_PAYBILL_PREFERENCE(payBills);
                SharedPreferenceUtil.editPRODUCT_SIM_PREFERENCE(sims);
                SharedPreferenceUtil.editPRODUCT_DATA_PREFERENCE(datas);
                SharedPreferenceUtil.editPRODUCT_PIN_PREFERENCE(pins);
                SharedPreferenceUtil
                        .editPRODUCT_TOPUP_OVERSEA_PREFERENCE(topUps_os);
                SharedPreferenceUtil.editPRODUCT_QPROMO_PREFERENCE(QPromoList);
                SharedPreferenceUtil.editPRODUCT_TOPUP_PREFERENCE(topUps);
                SharedPreferenceUtil.editGetProductInfo(false);
                pd.dismiss();
            }

        }
    }

    private AlertDialog showAlertDialog(final String mTitle, final String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
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

    private AlertDialog normalAlertDialog(final String msg) {

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create();
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

    public class MyPasswordTransformationMethod extends
            PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new MyPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    ;

    private AlertDialog changePasswordAlertDialog(final String msg) {
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(new MyPasswordTransformationMethod());
        input.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(input, 0);
            }
        }, 50);

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle("Verify Password")
                .setView(input)
                .setCancelable(true)
                .setMessage(msg)
                .setNegativeButton(R.string.title_cancel, null)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                VerifyChangePassword(input.getText().toString());
                            }
                        }).create();
    }


    private AlertDialog newPasswordAlertDialog(final String msg) {
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(new MyPasswordTransformationMethod());
        input.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(input, 0);
            }
        }, 50);

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle("Verify Password")
                .setView(input)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                changePasswordAsync(input.getText().toString());
                            }
                        }).create();
    }

    void VerifyChangePassword(String msisdn) {
        if (SharedPreferenceUtil.getsClientPassword().equals(msisdn)) {
            DLog.e(TAG, "SUCCESS VerifyChangePassword");
            newPasswordAlertDialog("Please insert new password.").show();
        } else {
            changePasswordAlertDialog("Your old password is not valid.\nPlease try again")
                    .show();
        }
    }

    RTWS rtWS = new RTWS();

    private void changePasswordAsync(final String sNewPassword) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();

            }

            @Override
            protected Boolean doInBackground(Void... arg0) {
                // TODO Auto-generated method stub

                if (sNewPassword.length() == 8)
                    return rtWS.changePassword(
                            SharedPreferenceUtil.getsClientUserName(),
                            SharedPreferenceUtil.getsClientPassword(),
                            sNewPassword);

                return false;

            }

            @Override
            protected void onPostExecute(Boolean result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result) {
                    Toast.makeText(mContext, "Password updated",
                            Toast.LENGTH_SHORT).show();
                    SharedPreferenceUtil.editsClientPassword(sNewPassword);
                } else
                    Toast.makeText(
                            mContext,
                            "Password update failed, Please insert 8 digit ONLY",
                            Toast.LENGTH_SHORT).show();

            }

        }.execute();
    }

    public class LogoutAsync extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
            pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
            SpannableString ss1 = new SpannableString("Please wait ...");
            ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
            ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                    ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                    0, ss1.length(), 0);
            pd = new ProgressDialog(mContext);
            pd.setTitle(ss1);
            pd.setMessage("Logout ...");
            pd.setCancelable(false);
            pd.show();
        }

        protected Boolean doInBackground(String... arg0) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SRSApp.sClientPassword = "";
            SRSApp.sClientUserName = "";
            SharedPreferenceUtil.editsClientUserName(null);
            SharedPreferenceUtil.editsClientPassword(null);
            return true;
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            Intent intent = new Intent(getActivity(), LoginUI.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

}

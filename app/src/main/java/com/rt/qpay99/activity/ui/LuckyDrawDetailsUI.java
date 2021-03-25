package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.R;
import com.rt.qpay99.object.AddressObj;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FirebaseUtil;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LuckyDrawDetailsUI extends SRSBaseActivity {
    String mName, FirebaseDBId;
    int mQPoint, mProductId;


    Button btnRedeem, btnRedeem2;
    String jsonStr, Description,ProductURL;
    TextView tvDesc;
    ImageView imgDraw;
    TextView tvProductName, tvInfo, tvQPointRequired,tvDeductInfo;


    @Override
    protected void onResume() {
        super.onResume();
        new getLuckyDrawCount().execute();
    }

    FirebaseUtil mFirebaseUtil = new FirebaseUtil();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_lucky_draw_details_ui;
    }

    @Override
    protected void activityCreated() {
        getSupportActionBar().setTitle("QPoints " + getString(R.string.luckydraw));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        Bundle extras = getIntent().getExtras();
        mName = extras.getString("mName");
        mProductId = extras.getInt("mProductId");
        mQPoint = extras.getInt("QPoints");
        FirebaseDBId = extras.getString("FirebaseDBId");
        Description = extras.getString("Description");
        ProductURL = extras.getString("ProductURL");

        DLog.e(TAG,"ProductURL -----" + ProductURL);

        tvDesc = findViewById(R.id.tvDesc);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(150); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tvDesc.startAnimation(anim);
        tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, WebViewUI.class);
                i.putExtra("redirect_url",ProductURL);
                startActivity(i);
            }
        });

        tvDesc.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(ProductURL)){
            tvDesc.setVisibility(View.VISIBLE);
        }

        tvDeductInfo = findViewById(R.id.tvDeductInfo);

        imgDraw = findViewById(R.id.imgDraw);
        tvProductName = findViewById(R.id.tvProductName);

        tvInfo = findViewById(R.id.tvInfo);
        tvQPointRequired = findViewById(R.id.tvQPointRequired);
//        tvQPointRequired.setText(mQPoint  + " QPoints / ticket");


        btnRedeem2 = findViewById(R.id.btnRedeem2);
        int rm = mQPoint / 100;
        dProductPrice = String.valueOf(rm);
        btnRedeem2.setText("RM " + rm + " / ticket");
        btnRedeem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QCashDrawAlertDialog("Purchase RM" + rm + " for "  + Description + " lucky draw ticket").show();

            }
        });

        if (mName.equalsIgnoreCase("QDRAW 3")) {
            btnRedeem2.setVisibility(View.GONE);
        }

        btnRedeem = findViewById(R.id.btnRedeem);
        btnRedeem.setText(mQPoint + " QPoints / ticket");
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QDrawAlertDialog("Redeem " + mQPoint + " QPoints for "   + Description + " lucky draw").show();
            }
        });
    }

    @Override
    protected void initData() {
        mFirebaseUtil.getImagesAsync(mContext, "QDraw" + mName, FirebaseDBId, imgDraw);
        tvProductName.setText(mName + " " + Description);

    }


    AddressObj address = new AddressObj();

    void isValidData(int mProductId , String mName, int mQPoint, String RefNo) {
        address.setId(mProductId);
        address.setName(mName);
        address.setQPoints(mQPoint);
        String msisdn = SharedPreferenceUtil.getsClientUserName().substring(1, SharedPreferenceUtil.getsClientUserName().length());
        address.setMobileNumber(msisdn);
        address.setAgentID(SharedPreferenceUtil.getsClientID());
        address.setRefNo(RefNo);

        new submitLuckyDrawLog().execute();
    }

    private class getLuckyDrawCount extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Loading...");
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            tvInfo.setText(aVoid);

        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.QPoint_LuckyDrawCount + mProductId;
            DLog.e(TAG, "url: " + url);
            jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr.toString());

            jsonStr = jsonStr.replace("\"", "");
            jsonStr = jsonStr.replace("\n", "");

            return jsonStr + " / 100 tickets redeemed";
        }
    }

    private class submitLuckyDrawLog extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Submiting....");
            pd.show();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();

            if (aVoid.equalsIgnoreCase("200")) {
                QAlertDialog("Congratulations, you have successfully redeemed your QPoints.").show();
            } else {
                QAlertDialog("Ops, Please try again later.").show();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = Config.QPoint_LuckyDraw;
//            url = "http://reload.dyndns.org:8020/rtweb/api/ProductRedemption/";
            DLog.e(TAG, "url " + url);
            Gson gson = new Gson();
            String json = gson.toJson(address);
            DLog.e(TAG, "json " + json);

            String r = httpPost(url, json);
            DLog.e(TAG, "" + r);
            return r;
        }
    }

    public String httpPost(String url, String data) {
        HttpURLConnection httpcon;
        String result = null;
        try {
            //Connect
            httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            DLog.e(TAG, "" + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e(TAG, "UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e(TAG, "IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private AlertDialog QCashDrawAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(true)
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                RequestInputAsync();
                            }
                        }).create();
    }

    private AlertDialog QDrawAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(true)
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                isValidData(mProductId,mName,mQPoint,"");
                            }
                        }).create();
    }

    private AlertDialog NormalAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create();
    }

    private AlertDialog QAlertDialog(String msg) {
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
                        }).create();
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
                sCustomerAccountNumber = SharedPreferenceUtil.getsClientUserName().substring(1, SharedPreferenceUtil.getsClientUserName().length());
                sCustomerMobileNumber = SharedPreferenceUtil.getsClientUserName().substring(1, SharedPreferenceUtil.getsClientUserName().length());
                RequestInputResponse result = rt.RequestInput(
                        sCustomerAccountNumber, sCustomerMobileNumber,
                        dProductPrice, "85", "ANDROID_QRAW", sClientTxID, "",
                        "", "", "");
                return result;
            }

            @Override
            protected void onPostExecute(RequestInputResponse result) {
                super.onPostExecute(result);
                if (pd != null)
                    if (pd.isShowing())
                        pd.dismiss();
                if (result != null) {
                    if (Config.WS_SUBMIT_SUCCESS.equalsIgnoreCase(result
                            .getsResponseStatus())) {
                        DLog.e(TAG, result.getsResponseStatus());
                        isValidData(mProductId,mName,Integer.parseInt(dProductPrice),result.getsResponseID());
                        return;
                    }
                }
                QAlertDialog(
                        "Transaction Failed. Please try again later. Error : "
                                + result.getsResponseStatus()).show();


            }
        }.execute();

    }


}

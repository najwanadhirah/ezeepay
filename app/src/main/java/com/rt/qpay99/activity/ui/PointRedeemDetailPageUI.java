package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.R;
import com.rt.qpay99.object.AddressObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PointRedeemDetailPageUI extends SRSBaseActivity {

    EditText edRName, edRMobile, edAddress1, edAddress2, edAddress3, edPostcode, edState;

    Button btnRedeem;
    List<AddressObj> addressList = new ArrayList<AddressObj>();

    String mName;

    int mQPoint,mProductId;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_point_redeem_detail_page_ui;
    }

    @Override
    protected void activityCreated() {

        getSupportActionBar().setTitle("Redemptions Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnRedeem = findViewById(R.id.btnRedeem);
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
                isValidData();
            }
        });

        Bundle extras = getIntent().getExtras();
        mName = extras.getString("mName");
        mProductId = extras.getInt("mProductId");
        mQPoint= extras.getInt("QPoints");
        edRName = findViewById(R.id.edRName);
        edRMobile = findViewById(R.id.edRMobile);
        edAddress1 = findViewById(R.id.edAddress1);

        edAddress2 = findViewById(R.id.edAddress2);
        edAddress3 = findViewById(R.id.edAddress3);
        edAddress3.setVisibility(View.GONE);
        edPostcode = findViewById(R.id.edPostcode);
        edState = findViewById(R.id.edState);


        addressList = SharedPreferenceUtil.getCUST_ADDRESS_LIST_PREFERENCE();
        if (addressList != null) {
            if (addressList.size() > 0) {
                edRName.setText(addressList.get(0).getName());
                edRMobile.setText(addressList.get(0).getMobileNumber());
                edAddress1.setText(addressList.get(0).getAddress1());
                edAddress2.setText(addressList.get(0).getAddress2());
                edPostcode.setText(addressList.get(0).getPasscode());
                edState.setText(addressList.get(0).getState());
            }
        }

    }

    void isValidData() {


        if (edRName.getText().length() < 4) {
            edRName.setError("Please fill in your name here");
            return;
        }

        if (edRMobile.getText().length() < 4) {
            edRMobile.setError("Please fill in your Mobile here");
            return;
        }

        if (edAddress1.getText().length() < 4) {
            edAddress1.setError("Please fill in");
            return;
        }

        if (edAddress2.getText().length() < 4) {
            edAddress2.setError("Please fill in");
            return;
        }

        if (edPostcode.getText().length() < 4) {
            edPostcode.setError("Please fill in");
            return;
        }

        if (edState.getText().length() < 4) {
            edState.setError("Please fill in");
            return;
        }
        saveAddress();

        new submitRedemptionLog().execute();
    }

    AddressObj address = new AddressObj();
    void saveAddress() {

        address.setName(edRName.getText().toString());
        address.setAddress1(edAddress1.getText().toString());
        address.setAddress2(edAddress2.getText().toString());
        address.setPasscode(edPostcode.getText().toString());
        address.setState(edState.getText().toString());
        address.setEmailAddess("");
        address.setMobileNumber(edRMobile.getText().toString());
        DLog.e(TAG,"mProductId " + mProductId);
        address.setId(mProductId);
        address.setName(mName);
        address.setQPoints(mQPoint);
        address.setAgentID(SharedPreferenceUtil.getsClientID());


        List<AddressObj> objs = new ArrayList<AddressObj>();
        addressList = SharedPreferenceUtil.getCUST_ADDRESS_LIST_PREFERENCE();
        if (!TextUtils.isEmpty(address.getName())) {
            if (addressList == null) {
                objs.add(address);
                SharedPreferenceUtil.editCUST_ADDRESS_LIST_PREFERENCE(objs);
            } else if (!addressList.contains(address)) {
                objs.add(address);
                SharedPreferenceUtil.editCUST_ADDRESS_LIST_PREFERENCE(objs);
            }
        }


    }

    private class submitRedemptionLog extends AsyncTask<Void, Void, String> {
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

            if(aVoid.equalsIgnoreCase("200")){
                QAlertDialog("Congratulations, you have successfully redeemed your QPoints. You will be contacted shortly with more details").show();
            }else{
                QAlertDialog("Ops, Please try again later.").show();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = Config.QPoint_ProductRedemption ;
//            url = "http://reload.dyndns.org:8020/rtweb/api/ProductRedemption/";
            DLog.e(TAG,"url " + url);
            Gson gson = new Gson();
            String json = gson.toJson(address);
            DLog.e(TAG,"json " + json);

            String r = httpPost(url,json);
            DLog.e(TAG,"" + r);
            return r;
        }
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

    public String httpPost2() {

        try{
            String mAddress;
            String mProduct;
            String mAgentName;
            String mDesc;

            mAddress = address.getAddress1()  + " " + address.getAddress2() + " " + address.getPasscode() + " " + address.getState();
            mProduct = mProductId + "-"  + mName + "-"  + mQPoint;
            mAgentName = SharedPreferenceUtil.getsClientID() + "-" + address.getName();
            HttpHandlerHelper sh = new HttpHandlerHelper();


            String param =  URLEncoder.encode(mAddress, "UTF-8") + "/" + URLEncoder.encode(mProduct, "UTF-8")  + "/" + "descr";

            String url = Config.QPoint_REDEMPTIONLOG + param;
            url = "http://reload.dyndns.org:8020/rtweb/api/ProductRedemption/Log/" + param;
            DLog.e(TAG, "url: " + url);
            String jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr);
        }catch (Exception e){

        }



        return null;
    }

    public  String httpPost(String url,  String data){
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
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            DLog.e(TAG,"" + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e(TAG,"UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e(TAG,"IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static HttpResponse makeRequest(String uri, String json) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            return new DefaultHttpClient().execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void initData() {
        DLog.e(TAG, "initData ");
        String msisdn = SharedPreferenceUtil.getsClientUserName().substring(1, SharedPreferenceUtil.getsClientUserName().length());
        DLog.e(TAG, "msisdn " + msisdn);
        edRName.setText(SharedPreferenceUtil.getsClientID() + " - " + SharedPreferenceUtil.getMerchantName());
        edRMobile.setText(msisdn);

    }

}

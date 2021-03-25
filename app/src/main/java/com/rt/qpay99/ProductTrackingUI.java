package com.rt.qpay99;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.rt.qpay99.activity.ui.SRSBaseActivity;
import com.rt.qpay99.util.DLog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ProductTrackingUI extends SRSBaseActivity {


    WebView wvTracking;


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_product_tracking_ui;
    }

    @Override
    protected void activityCreated() {

        mContext = this;
        wvTracking = findViewById(R.id.wvTracking);
        wvTracking.getSettings().setDomStorageEnabled(true);
        wvTracking.getSettings().setDatabaseEnabled(true);
        wvTracking.getSettings().setJavaScriptEnabled(true);
        wvTracking.getSettings().setBuiltInZoomControls(true);

        wvTracking.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        wvTracking.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        wvTracking.addJavascriptInterface(new CustomJavaScriptInterface(mContext), "Android");
    }

    @Override
    protected void initData() {
        DLog.e(TAG,"initData ");
        new gdexpressTrackingAsync().execute();
        DLog.e(TAG,"initData 2");
    }


    private class gdexpressTrackingAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DLog.e(TAG,"gdexpressTrackingAsync");
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

//            int startIndex = aVoid.indexOf("var strTD =");
//            int endIndex =  aVoid.indexOf("$(\"#trackinfo03\").append(strTD)");
//            DLog.e(TAG,"aVoid " + aVoid);
//            DLog.e(TAG,"startIndex " + startIndex);
//            DLog.e(TAG,"endIndex " + endIndex);
//            DLog.e(TAG,"aVoid.length() " + aVoid.length());
//            DLog.e(TAG,"(endIndex - startIndex) " + (endIndex - startIndex));
//            int subst = endIndex - startIndex;
//           try{
//               String newD = aVoid.substring(startIndex,subst);
//               DLog.e(TAG,"newD " + newD);
//           }catch (Exception e){
//               DLog.e(TAG,"e " + e.getMessage());
//           }


            wvTracking.loadData(aVoid, "text/html; charset=UTF-8", null);
//            wvTracking.loadUrl("javascript:Android.getIds(strTD);");
        }

        @Override
        protected String doInBackground(Void... voids) {
            DLog.e(TAG,"gdexpressTrackingAsync doInBackground");
          return  PosLajuTracking("en233873973my");
        }
    }

    public String PosLajuTracking(String trackingNumber) {
        String responseBody="" ;
        DLog.e(TAG,"gdexpressTracking");
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://www.poslaju.com.my/track-trace-v2/");
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList< NameValuePair >(3);
            nameValuePairs.add(new BasicNameValuePair("trackingNo03", trackingNumber));
            nameValuePairs.add(new BasicNameValuePair("hvtrackNoHeader03", null));
            nameValuePairs.add(new BasicNameValuePair("hvfromheader03", "0"));


            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                DLog.e(TAG, "works till here. 2");
                try {
                    HttpResponse response = httpclient.execute(httppost);

                    responseBody = EntityUtils.toString(response.getEntity());
                    responseBody =  responseBody.replace("#","%23");
                    DLog.e(TAG, "responseBody " + responseBody);


                    int startIndex = responseBody.indexOf("var strTD =");
                    int endIndex =  responseBody.indexOf("$(\"#trackinfo03\").append(strTD)");
                    DLog.e(TAG,"aVoid " + responseBody);
                    DLog.e(TAG,"startIndex " + startIndex);
                    DLog.e(TAG,"endIndex " + endIndex);
                    DLog.e(TAG,"aVoid.length() " + responseBody.length());
                    DLog.e(TAG,"(endIndex - startIndex) " + (endIndex - startIndex));
                    int subst = endIndex - startIndex;
                    try{
                        String newD = responseBody.substring(startIndex,subst);
                        DLog.e(TAG,"newD " + newD);
                    }catch (Exception e){
                        DLog.e(TAG,"e " + e.getMessage());
                    }


                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                DLog.e(TAG,"UnsupportedEncodingException " + e.getMessage());
                e.printStackTrace();
            }
        }catch (Exception e ){

            DLog.e(TAG,"Exception " + e.getMessage());

        }
        return responseBody;
    }

    public String gdexpressTracking(String trackingNumber) {
        String responseBody="" ;
        DLog.e(TAG,"gdexpressTracking");
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://web3.gdexpress.com/official/iframe/etracking_v3.php");
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList< NameValuePair >(3);
            nameValuePairs.add(new BasicNameValuePair("capture", trackingNumber));
            nameValuePairs.add(new BasicNameValuePair("redoc_gdex", "cnGdex"));
            nameValuePairs.add(new BasicNameValuePair("Submit", "Track"));


            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                DLog.e(TAG, "works till here. 2");
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseBody = EntityUtils.toString(response.getEntity());
                    DLog.e(TAG, "responseBody " + responseBody);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                DLog.e(TAG,"UnsupportedEncodingException " + e.getMessage());
                e.printStackTrace();
            }
        }catch (Exception e ){

            DLog.e(TAG,"Exception " + e.getMessage());

        }
        return responseBody;
    }


    class CustomJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        CustomJavaScriptInterface(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void getIds(final String myIds) {
            DLog.e(TAG,"----------------------- " + myIds);
            //Do somethings with the Ids

        }
    }
}



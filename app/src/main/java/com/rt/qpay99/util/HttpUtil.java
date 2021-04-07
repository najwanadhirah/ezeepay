package com.rt.qpay99.util;


import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    private static String TAG = "HttpUtil";

    public static String httpPostJB(String url, JsonObject data) {
        DLog.e("httpPost","data--->" + data);
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
            writer.write(data.toString());
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
            DLog.e("httpPost", "" + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e("httpPost", "UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e("httpPost", "IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static String httpPost(String url, String data) {
        DLog.e("httpPost","data--->" + data);
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
            DLog.e("httpPost", "" + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e("httpPost", "UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e("httpPost", "IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static String httpPost(String url, String data,String method) {
        DLog.e("httpPost",method + " data--->" + data);
        HttpURLConnection httpcon;
        String result = null;
        try {
            //Connect
            httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod(method);
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
            DLog.e("httpPost", "" + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e("httpPost", "UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e("httpPost", "IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static String httpGet(String url) {
        try {
            DLog.e(TAG,"HttpPostData");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = null;
            is = entity.getContent();
            String jsonStr =  convertStreamToString(is);
            DLog.e(TAG, "jsonStr " + jsonStr);
            return jsonStr;


        } catch (ClientProtocolException e) {
            DLog.e(TAG,"1" + e.getMessage());
        } catch (IOException e) {
            DLog.e(TAG,"2" + e.getMessage());
        } catch (Exception e) {
            DLog.e(TAG,"3" + e.getMessage());
        }
        finally {
            return  "";
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}

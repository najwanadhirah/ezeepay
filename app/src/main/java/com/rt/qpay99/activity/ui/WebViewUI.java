package com.rt.qpay99.activity.ui;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;

public class WebViewUI extends  SRSBaseActivity {

    private String redirect_url;
    WebView wv;
    TextView tvInfo;
    boolean isDirectDownload = false;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_web_view_ui;
    }

    @Override
    protected void activityCreated() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mContext = this;
        tvInfo = findViewById(R.id.tvInfo);
        wv = findViewById(R.id.wv);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    }

    @Override
    protected void initData() {


        Bundle extras = getIntent().getExtras();
        redirect_url = extras.getString("redirect_url");

        DLog.e(TAG,"" + redirect_url);

        try{
            isDirectDownload = extras.getBoolean("isDirectDownload");
            DLog.e(TAG,"isDirectDownload - " + isDirectDownload);
        }catch (Exception e ){

        }

        if(!isDirectDownload)
            tvInfo.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(redirect_url)){
            wv.loadUrl(redirect_url);
            wv.getSettings().setSupportMultipleWindows(true);
            wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            wv.getSettings().setAllowFileAccess(true);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setDisplayZoomControls(false);
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.getSettings().setUseWideViewPort(true);
            if(isDirectDownload)
                wv.setDownloadListener(new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                        request.setMimeType(mimeType);
                        //------------------------COOKIE!!------------------------
                        String cookies = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookies);
                        //------------------------COOKIE!!------------------------
                        request.addRequestHeader("User-Agent", userAgent);
                        request.setDescription("Downloading file...");
                        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                    }
                });
        }


    }
}

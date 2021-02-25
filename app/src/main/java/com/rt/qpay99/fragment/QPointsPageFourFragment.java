package com.rt.qpay99.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.rt.qpay99.R;

public class QPointsPageFourFragment extends Fragment {


    public QPointsPageFourFragment() {

    }


    private Context mContext;
    WebView wv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qpoints_page_four,
                container, false);
        mContext = this.getActivity();

        wv = rootView.findViewById(R.id.wvFB);

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
//        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        wv.getSettings().setBuiltInZoomControls(true);
        wv.loadUrl("https://www.facebook.com/qpay99");




        return rootView;
    }


}

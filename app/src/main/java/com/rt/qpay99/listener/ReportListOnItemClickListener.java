package com.rt.qpay99.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.rt.qpay99.Constants;
import com.rt.qpay99.activity.ui.CustomerTopupTxReportUI;
import com.rt.qpay99.activity.ui.ReportAgentSales;
import com.rt.qpay99.activity.ui.ReportUI;
import com.rt.qpay99.util.DLog;

/**
 * Created by User on 1/17/2017.
 */

public class ReportListOnItemClickListener implements
        AdapterView.OnItemClickListener {

    private Context mContext;
    private String TAG = "ReportListOnItemClickListener";

    public ReportListOnItemClickListener(Context mContext) {
        this.mContext = mContext;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        DLog.e(TAG, "ProductGridviewOnItemClickListener");

        String data =(String)parent.getAdapter().getItem(position);


        String item = data;
        DLog.e(TAG, "item " + item);

        Intent intent = new Intent();

        if(data.equalsIgnoreCase(Constants.REPORT_Sales)){
            intent = new Intent(mContext, ReportUI.class);
        }
        if(data.equalsIgnoreCase(Constants.REPORT_Received)){
            intent = new Intent(mContext, CustomerTopupTxReportUI.class);
            intent.putExtra("sCreditType", "RECEIVED");
        }
        if(data.equalsIgnoreCase(Constants.REPORT_Transferred)){
            intent = new Intent(mContext, CustomerTopupTxReportUI.class);
            intent.putExtra("sCreditType", "TRANSFERRED");
        }

        if(data.equalsIgnoreCase(Constants.REPORT_Sales_by_MSISDN)){
            intent = new Intent(mContext, ReportAgentSales.class);
            intent.putExtra("sCreditType", "TRANSFERRED");
        }


        mContext.startActivity(intent);
    }



}

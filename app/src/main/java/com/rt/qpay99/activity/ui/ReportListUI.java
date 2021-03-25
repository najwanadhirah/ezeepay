package com.rt.qpay99.activity.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.StringAdapter;
import com.rt.qpay99.component.ScrollTextView;
import com.rt.qpay99.listener.ReportListOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ReportListUI extends AppCompatActivity {

    private ListView lvReportList;
    private List<String> data = new ArrayList<String>();
    private StringAdapter mStringAdapter;
    private Context mContext;
    ScrollTextView scrolltext;

    private ReportListOnItemClickListener mReportListOnItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report_list_ui);

        mContext = this;


        scrolltext = (ScrollTextView) findViewById(R.id.scrolltext);
        scrolltext.setText(Config.MOVING_TEXT);

        scrolltext.startScroll();


        lvReportList = (ListView) findViewById(R.id.lvReportList);
        data.add(Constants.REPORT_Sales);
        data.add(Constants.REPORT_Transferred);
        data.add(Constants.REPORT_Received);
        data.add(Constants.REPORT_Sales_by_MSISDN);

        mStringAdapter  = new StringAdapter(mContext,data,true,false);

        lvReportList.setAdapter(mStringAdapter);
        mReportListOnItemClickListener = new ReportListOnItemClickListener(mContext);
        lvReportList.setOnItemClickListener(mReportListOnItemClickListener);


    }
}

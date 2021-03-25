package com.rt.qpay99.activity.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Constants;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.CustomerTopupTxAdapter;
import com.rt.qpay99.fragment.CustomDatePickerFragment;
import com.rt.qpay99.object.CustomerTopupTx;
import com.rt.qpay99.util.CalenderUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomerTopupTxReportUI extends FragmentActivity implements View.OnClickListener,
        CustomDatePickerFragment.DatePickedListener {

    private TextView nav_text, tvBalance;
    private Button nav_btn_right;
    private String sCreditType;
    private ListView lvCustomerTopupTx;

    private CustomerTopupTxAdapter mCustomerTopupTxAdapter;
    private Context mContext;

    private String TAG  = this.getClass().getName();

    private TextView tvStartDate,tvEndDate;
    private boolean isStarDateClick, isEndDateClick = false;
    private CustomDatePickerFragment newFragment = new CustomDatePickerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customer_topup_tx_report_ui);
        mContext = this;
        Bundle extras = getIntent().getExtras();

        sCreditType=extras.getString("sCreditType");
        nav_text = (TextView) findViewById(R.id.nav_text);

        if(sCreditType.equalsIgnoreCase("RECEIVED")){
            nav_text.setText(Constants.REPORT_Received);
        }
        if(sCreditType.equalsIgnoreCase("TRANSFERRED")){
            nav_text.setText(Constants.REPORT_Transferred);
        }

        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);


        tvStartDate.setText(CalenderUtil.getStringAddDate(new Date(),
                "yyyy/MM/dd", 0));
        tvStartDate.setOnClickListener(this);
        tvEndDate.setText(CalenderUtil.getStringAddDate(new Date(),
                "yyyy/MM/dd", 0));
        tvEndDate.setOnClickListener(this);

        DLog.e(TAG," sCreditType " + sCreditType);

        lvCustomerTopupTx = (ListView) findViewById(R.id.lvCustomerTopupTx);

        nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
        nav_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        new CheckUserTopupTxByTypeAsync().execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        DLog.e(TAG, " Click");
        switch (v.getId()) {
            case R.id.tvStartDate:
                DLog.e(TAG, "edStartDate Click");
                isStarDateClick = true;
                newFragment.setmListener(this);
                newFragment.show(getSupportFragmentManager(), TAG);
                break;
            case R.id.tvEndDate:
                DLog.e(TAG, "edEndDate Click");
                isEndDateClick = true;
                newFragment.setmListener(this);
                newFragment.show(getSupportFragmentManager(), TAG);
                break;
        }
    }

    private String sSDate, sEDate, sCustomerAccount;
    public class CheckUserTopupTxByTypeAsync extends
            AsyncTask<String, Void, List<CustomerTopupTx>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sSDate = tvStartDate.getText().toString();
            sEDate = tvEndDate.getText().toString();
        }



        @Override
        protected List<CustomerTopupTx> doInBackground(String... params) {
            RTWS cs = new RTWS();
            return cs.CheckUserTopupTxByType(SharedPreferenceUtil.getsClientUserName(),SharedPreferenceUtil.getsClientPassword(),sCreditType,sSDate,sEDate);
        }

        @Override
        protected void onPostExecute(List<CustomerTopupTx> customerTopupTxes) {
            super.onPostExecute(customerTopupTxes);

            DLog.e(TAG,"" + customerTopupTxes.size());
            mCustomerTopupTxAdapter = new CustomerTopupTxAdapter(mContext,customerTopupTxes);
            lvCustomerTopupTx.setAdapter(mCustomerTopupTxAdapter);

        }
    }

    @Override
    public void onDatePicked(Calendar time) {
        // TODO Auto-generated method stub
        DLog.e(TAG, "" + time.getTime());
        if (isStarDateClick) {
            tvStartDate.setText(CalenderUtil.getStringAddDate(time.getTime(),
                    "yyyy/MM/dd", 0));
            isStarDateClick = false;
            sSDate = tvStartDate.getText().toString();
        }

        if (isEndDateClick) {
            tvEndDate.setText(CalenderUtil.getStringAddDate(time.getTime(),
                    "yyyy/MM/dd", 0));
            isEndDateClick = false;
            sEDate = tvEndDate.getText().toString();
        }
    }
}

package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.adapter.BiListRecycleAdapter;
import com.rt.qpay99.object.BankIn;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class AgentBIList extends SRSBaseActivity {

    RecyclerView rcvBIList;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_agent_bilist;
    }

    @Override
    protected void activityCreated() {
        rcvBIList = findViewById(R.id.rcvBIList);
        rcvBIList.setItemAnimator(new DefaultItemAnimator());
        rcvBIList.setLayoutManager(new LinearLayoutManager(mContext ));

//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
         GetBankInListAysnc();
    }

    private ProgressDialog pd;
    private void GetBankInListAysnc() {
        new AsyncTask<Void, Void, List<BankIn>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
                SpannableString ss1 = new SpannableString("Requesting ...");
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
            protected List<BankIn> doInBackground(Void... params) {
                String sClientUserName = SharedPreferenceUtil.getsClientUserName();
                String sClientPassword = SharedPreferenceUtil.getsClientPassword();
                String sTS = FunctionUtil.getsDNReceivedID();
                String sEncKey = FunctionUtil.getsEncK(SharedPreferenceUtil.getsClientUserName() + "RichTech6318" + sTS);
                DLog.e(TAG, "" + sTS);
                DLog.e(TAG, "" + sEncKey);
                return rtWS.GetBankInList(sClientUserName, sClientPassword, sTS, sEncKey);

            }

            @Override
            protected void onPostExecute(List<BankIn> result) {
                super.onPostExecute(result);
                DLog.e(TAG,"" + result.size());
                if(result!=null){
                    rcvBIList.setAdapter(new BiListRecycleAdapter(mContext,new mRecyclerViewOnItemClick(),result));
                }

                if(pd!=null)
                    if(pd.isShowing())
                        pd.dismiss();
            }
        }.execute();

    }

    public class mRecyclerViewOnItemClick implements ListenerInterface.RecyclerViewClickListener {
        @Override
        public void recyclerViewListClicked(View v, int position) {

        }
    }


}

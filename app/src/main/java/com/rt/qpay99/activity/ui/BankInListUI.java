package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.R;
import com.rt.qpay99.adapter.BankInListAdapter;
import com.rt.qpay99.object.BankIn;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.List;

public class BankInListUI extends FragmentActivity {

    private ListView lvBankIn;
    private RTWS rtWS = new RTWS();
    private String TAG = this.getClass().getName();
    private Context mContext;
    private BankInListAdapter mBankInListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_in_list_ui);
        mContext = this;
        init();
        initPageData();


    }

    private ProgressDialog pd;
    private void UpdatePrintCountAsync() {
        new AsyncTask<Void, Void, List<BankIn>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
                SpannableString ss1 = new SpannableString("Pin requesting ...");
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
                if(pd!=null)
                    if(pd.isShowing())
                        pd.dismiss();

                if(result!=null){
                    mBankInListAdapter = new BankInListAdapter(mContext,result);
                    lvBankIn.setAdapter(mBankInListAdapter);
                    mBankInListAdapter.notifyDataSetChanged();

                }
            }


        }.execute();

    }

    public void ClosePage(View view) {
        this.finish();
    }

    private void initPageData() {
        UpdatePrintCountAsync();
    }

    private void init() {
        lvBankIn = (ListView) findViewById(R.id.lvBankIn);
    }
}

package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.rt.qpay99.util.AlertBoxUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.ws.RTWS;


public abstract class SRSBaseActivity extends AppCompatActivity {

    public String TAG = this.getClass().getName();
    public Context mContext;
    private ViewFlipper productViewFlipper;
    public Bundle extras;
    public String mName;
    public ProgressDialog pd;

    public AlertDialog dialog;
    public AlertDialog.Builder builder;

    public void setDialog(String title,String msg,boolean Cancelable){
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK",null);
        builder.setCancelable(Cancelable);
        dialog = builder.create();
        dialog.show();
    }


    public String sCustomerMobileNumber, dProductPrice, sProductID,
            sCustomerAccountNumber, sClientTxID, BILL;

    public RTWS rtWS= new RTWS();
    protected abstract int getLayoutResourceId();

    protected abstract void activityCreated();

    protected abstract void initData();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                DLog.e(TAG, "onOptionsItemSelected ");
                if (productViewFlipper != null){
                    if(productViewFlipper.getChildCount()==productViewFlipper.getDisplayedChild()){
                        finish();
                    }

                    if (productViewFlipper.getDisplayedChild() > 0) {
                        productViewFlipper.showPrevious();

                    } else {
                        onBackPressed();
//                    NavUtils.navigateUpFromSameTask(this);
                    }
                }
                else
                    onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mContext = this;
         extras = getIntent().getExtras();
        activityCreated();
        initData();
    }

    public class AlertDialogOnclickListender implements AlertBoxUtil.RTAlertDialogInterface {


        @Override
        public void BUTTON_POSITIVE_CLICK() {

        }

        @Override
        public void BUTTON_NEGATIVE_CLICK() {

        }

        @Override
        public void BUTTON_NEUTRAL_CLICK() {

        }
    }
}

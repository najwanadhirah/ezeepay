package com.rt.qpay99.activity.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.rt.qpay99.BasePermissionAppCompatActivity;
import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

public class TACUI extends BasePermissionAppCompatActivity {

    EditText edOTP;
    TextView tvMinuteTimer;
    String output;
    MyCount counter;
    long seconds;
    String sDeviceID;
    TextView tvTACVerify;
    LinearLayout lltac2;
    com.beardedhen.androidbootstrap.BootstrapButton btnSubmit, btnBack;
    int submitCount = 0;
    View root;
    ProgressDialog pd;
    String sTAC;
    RTWS rtWS = new RTWS();
    private String TAG = this.getClass().getName();
    private Context mContext;
    private IntentFilter mIntentFilter;
    private String sClientUserName, sClientPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = getLayoutInflater().inflate(R.layout.activity_tacui, null);
        setContentView(root);
        mContext = this;

        tvTACVerify = findViewById(R.id.tvTACVerify);
        String tacText = tvTACVerify.getText().toString();
        DLog.e(TAG, "" + tacText);
        tvTACVerify.setText(tacText + " " + SharedPreferenceUtil.getsClientUserName());

        edOTP = findViewById(R.id.edOTP);
        tvMinuteTimer = findViewById(R.id.tvMinuteTimer);
        counter = new MyCount(120000, 1000);
        counter.start();
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCount++;
                if (submitCount < 4)
                    DeviceVerifyTACAsync();
                else {
                    showBackButton();
                    btnSubmit.setEnabled(false);
                }

            }
        });

        setTitle("Verify " + SharedPreferenceUtil.getsClientUserName());

        lltac2 = findViewById(R.id.lltac2);
        sDeviceID = "1";


        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                DLog.e(TAG, "task onSuccess ");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                DLog.e(TAG, "task onFailure ");
            }
        });

    }

    private void DeviceVerifyTACAsync() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                sTAC = edOTP.getText().toString();
                pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
                SpannableString ss1 = new SpannableString("Please wait ...");
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
            protected Boolean doInBackground(Void... arg0) {

                sDeviceID = FunctionUtil.getDeviceIdOrAndroidId(mContext);
                DLog.e(TAG, "sDeviceID " + sDeviceID);
                SharedPreferenceUtil.editsDeviceID(sDeviceID);

//                return rtWS.DeviceVerifyTAC(SharedPreferenceUtil.getsClientUserName(), SharedPreferenceUtil.getsClientPassword(),
//                        sDeviceID, Config.sClientID, sTAC);
                String sTs = FunctionUtil.getsDNReceivedID();
                //msisdn = "60166572577";
//                String sEncKey = FunctionUtil.getsEncK(SharedPreferenceUtil.getsClientUserName(),+ "RichTech6318" + sTs);
                String sEncKey = FunctionUtil.getsEncK(sClientUserName + "RichTech6318" + sTs);
                return rtWS.DeviceVerifyTAC(SharedPreferenceUtil.getsClientUserName(),  SharedPreferenceUtil.getsClientPassword(),
                        sDeviceID,  Config.sClientID, sTAC,sTs,sEncKey);

            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                pd.dismiss();
                if (result) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Snackbar.make(root, "Invalid TAC, Please try again!!!", Snackbar.LENGTH_SHORT).show();
                }
            }

        }.execute();
    }

    void showBackButton() {
        btnBack.setVisibility(View.VISIBLE);
        lltac2.setVisibility(View.INVISIBLE);
    }

    public String formatTime(long millis) {
        output = "";
        seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours % 60;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD = String.valueOf(hours);

        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        if (hours < 10)
            hoursD = "0" + hours;

//        output = hoursD + " : " + minutesD + " : " + secondsD;
        output = minutesD + ":" + secondsD;

        return output;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        counter.cancel();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        DLog.e(TAG, "onResume");

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }

    public class MyCount extends CountDownTimer {
        Context mContext;

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        public void onTick(long millisUntilFinished) {
            tvMinuteTimer.setText(formatTime(millisUntilFinished));

            if (seconds == 0) {
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            }
        }

        public void onFinish() {
            DLog.e(TAG, "onFinish ===");
            showBackButton();
        }

    }


}
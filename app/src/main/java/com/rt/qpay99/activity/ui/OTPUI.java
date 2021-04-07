package com.rt.qpay99.activity.ui;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.poovam.pinedittextfield.PinField;
import com.poovam.pinedittextfield.SquarePinField;
import com.rt.qpay99.Constants;
import com.rt.qpay99.R;
import com.rt.qpay99.object.RTResponse;
import com.rt.qpay99.util.AlertBoxUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

import org.jetbrains.annotations.NotNull;

public class OTPUI extends SRSBaseActivity {

    Button btnSubmit, btnConfirmPassword;
    Toolbar toolbar;
    String otp, sClientUserName, sClientID;


    TextInputEditText edNewPassowrd, edCNewPassowrd;
    private View.OnClickListener changePasswordValidte = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pass = "";
            String cpass = "";
            try {
                pass = edNewPassowrd.getText().toString();
                cpass = edCNewPassowrd.getText().toString();
            } catch (Exception e) {

                e.printStackTrace();
            }
            if ((TextUtils.isEmpty(pass)) || TextUtils.isEmpty(cpass)) {
                Toast.makeText(mContext, "Please enter you new password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equalsIgnoreCase(cpass)) {
                Toast.makeText(mContext, "Password does not match!!", Toast.LENGTH_SHORT).show();
                return;
            }
             changePasswordAsync(pass);

        }
    };




    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otpui;
    }

    @Override
    protected void activityCreated() {

        toolbar = findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        toolbar.setTitle("OTP");

        mViewFlipper = findViewById(R.id.mViewFlipper);

        pBar = findViewById(R.id.pBar);
        pBar.setVisibility(View.GONE);

        edNewPassowrd = findViewById(R.id.edNewPassowrd);
        edCNewPassowrd = findViewById(R.id.edCNewPassowrd);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(false);
        btnSubmit.setBackground(mContext.getDrawable(R.drawable.rounder_border_textbox));
        btnSubmit.setTextColor(Color.BLACK);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new postDataAsync().execute();
            }
        });

        btnConfirmPassword = findViewById(R.id.btnConfirmPassword);
        btnConfirmPassword.setOnClickListener(changePasswordValidte);

        final SquarePinField linePinField = mViewFlipper.findViewById(R.id.squareField);
        linePinField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete(@NotNull String enteredText) {
                Toast.makeText(mContext, enteredText, Toast.LENGTH_SHORT).show();
                otp = enteredText;
                btnSubmit.setEnabled(true);
                btnSubmit.setTextColor(Color.WHITE);
                btnSubmit.setBackground(mContext.getDrawable(R.drawable.main_gradient_bg_button));
                return true; // Return true to keep the keyboard open else return false to close the keyboard
            }
        });


    }

    @Override
    protected void initData() {
        extras = getIntent().getExtras();
        if (extras != null) {
            sClientUserName = extras.getString("sClientUserName");
            mCategory = extras.getString("mCategory");
            sClientID = extras.getString("sClientID");
            try {
                int sd = Integer.parseInt(sClientID);
            } catch (Exception e) {
                sClientID = "0";
            }
        }

    }

    private void changePasswordAsync(final String sNewPassword) {
        new AsyncTask<Void, Void, RTResponse>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                pBar.setVisibility(View.VISIBLE);

            }

            @Override
            protected RTResponse doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                sTs = FunctionUtil.getsDNReceivedID();
                sEncKey = FunctionUtil.getsEncK(sClientUserName +"RichTech6318" + sTs);
                return rtWS
                        .ForgotPasswordUpdatePassword(
                                sClientUserName,
                                sClientUserName,
                                sNewPassword,sClientID,sTs,sEncKey,"");

            }

            @Override
            protected void onPostExecute(RTResponse result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pBar.setVisibility(View.GONE);

                if (result.isResultBoolean()) {
                    Toast.makeText(mContext,result.getsResponseMessage(),Toast.LENGTH_SHORT).show();
                    AlertBoxUtil mAlertBoxUtil = new AlertBoxUtil(mContext, new AlertDialogOnclickListender());
                    mAlertBoxUtil.showAlertDialog(SharedPreferenceUtil.getServerName(), result.getsResponseMessage(), "OK", "");
                } else
                    Toast.makeText(mContext, result.getsResponseMessage(),
                            Toast.LENGTH_SHORT).show();



            }

        }.execute();
    }

    private class AlertDialogOnclickListender implements AlertBoxUtil.RTAlertDialogInterface {


        @Override
        public void BUTTON_POSITIVE_CLICK() {
            finish();
        }

        @Override
        public void BUTTON_NEGATIVE_CLICK() {

        }

        @Override
        public void BUTTON_NEUTRAL_CLICK() {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                DLog.e(TAG, "onOptionsItemSelected ");
                if (mViewFlipper.getDisplayedChild() > 0) {
                    mViewFlipper.showPrevious();

                } else {
                    onBackPressed();
//                    NavUtils.navigateUpFromSameTask(this);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class postDataAsync extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Boolean resp) {
            super.onPostExecute(resp);
            pBar.setVisibility(View.GONE);
            if (resp) {
                pBar.setVisibility(View.GONE);
                mViewFlipper.showNext();
            } else {
                Toast.makeText(mContext, "Invalid OTP!!!", Toast.LENGTH_SHORT).show();
            }


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String sTs = FunctionUtil.getsDNReceivedID();
            //msisdn = "60166572577";
            String sEncKey = FunctionUtil.getsEncK(sClientUserName + "RichTech6318" + sTs);
            boolean b = rtWS.DeviceVerifyTAC(sClientUserName, sClientUserName, Constants.CAT_FORGOTPASSWORD, Integer.valueOf(sClientID), otp,sTs,sEncKey);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return b;
        }
    }
}

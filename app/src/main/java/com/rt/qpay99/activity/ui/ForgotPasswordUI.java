package com.rt.qpay99.activity.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.Permission.PermissionsUtil;
import com.rt.qpay99.R;
import com.rt.qpay99.ShareFunc.SendLocationAsync;
import com.rt.qpay99.object.RTResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.GpsUtils;
import com.rt.qpay99.util.HttpUtil;
import com.rt.qpay99.util.LocationUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordUI extends SRSBaseActivity {


    Button btnNext;
    EditText edMSISDN;
    String msisdn;
    String sDeviceID;
    String SITE_SECRET_KEY = "6LfvDY4aAAAAAOuDvYy2eUdxYh08ODNcrt5OKGYt";
    String SITE_KEY = "6LfvDY4aAAAAAPeAwQsDycEX2Lwa38Agz-KOsAgt";

    RequestQueue queue;
    String userResponseToken;
    CheckBox GoogleCaptcha;
    boolean isGPS= false;
    boolean isCont = false;
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_forgot_password_ui;
    }

    @Override
    protected void activityCreated() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);

        sDeviceID = FunctionUtil.getDeviceIdOrAndroidId(mContext);

        DLog.e(TAG, "------------------ > " + sDeviceID);

        GoogleCaptcha = findViewById(R.id.GoogleCaptcha);
        pBar = findViewById(R.id.pBar);
        pBar.setVisibility(View.GONE);
        btnNext = findViewById(R.id.btnNext);
        edMSISDN = findViewById(R.id.edMSISDN);
        if (Config.isDebug)

            edMSISDN.setText("0166572577");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DLog.e(TAG, "btnNext onClick ");
                if (ContextCompat.checkSelfPermission(
                        mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    new SendLocationAsync(new SendLocationListener(),mContext).execute( );

                } else  if (ActivityCompat.shouldShowRequestPermissionRationale(com.rt.qpay99.activity.ui.ForgotPasswordUI.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    setDialog("Location Permission","App require this permission to continue.",false,"Setting page","Cancel",new dialogClick());
                    return;
                } else {
                    PermissionsUtil.checkAndRequestPermissions(com.rt.qpay99.activity.ui.ForgotPasswordUI.this);
                    return;

                }
//                if(PermissionsUtil.hasPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION)){
//                    new SendLocationAsync(new SendLocationListener(),mContext).execute( );
////                    new ChangePassTaskAsync(new ChangePassResultListener()).execute(new String[]{edCNewPassowrd.getText().toString()});
//                }else{
//                    PermissionsUtil.checkAndRequestPermissions(ForgotPasswordUI.this);
//                    return;
//                }

                if(!isGPS){
                    DLog.e(TAG, "enable gps");
                    new GpsUtils(mContext).turnGPSOn(new GpsUtils.onGpsListener() {
                        @Override
                        public void gpsStatus(boolean isGPSEnable) {
                            // turn on GPS
                            isGPS = isGPSEnable;
                        }
                    });
                    return;
                }
                if (TextUtils.isEmpty(edMSISDN.getText())) {
                    edMSISDN.setError("Please enter valid Mobile No.");
                    return;
                }
                try {
                    msisdn = edMSISDN.getText().toString();
                    if (msisdn.substring(0, 2).equals("01")) {
                        msisdn = "6" + msisdn;
                    }
                } catch (Exception e) {
                    return;
                }

                SafetyNet.getClient(mContext).verifyWithRecaptcha(SITE_KEY)
                        .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                                // Indicates communication with reCAPTCHA service was
                                // successful.
                                DLog.e(TAG, "onSuccess");
                                userResponseToken = recaptchaTokenResponse.getTokenResult();
                                if (!userResponseToken.isEmpty()) {
                                    // Validate the user response token using the
                                    // reCAPTCHA siteverify API.
                                    DLog.e(TAG, "VALIDATION STEP NEEDED " + userResponseToken);
                                    new handleCaptchaResultAsync().execute();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DLog.e(TAG, "onFailure");
                                DLog.e(TAG, "TO CHECK ON FAILURE : "+ SITE_KEY);
                                DLog.e(TAG, " HERE INSIDE ON FAILURE");
                                if (e instanceof ApiException) {
                                    // An error occurred when communicating with the
                                    // reCAPTCHA service. Refer to the status code to
                                    // handle the error appropriately.
                                    ApiException apiException = (ApiException) e;
                                    int statusCode = apiException.getStatusCode();
                                    DLog.e(TAG, "gg check status code :  "+ statusCode);
                                    DLog.e(TAG, "Error on failure 1st : " + CommonStatusCodes
                                            .getStatusCodeString(statusCode));
                                } else {
                                    // A different, unknown type of error occurred.
                                    DLog.e(TAG, "Error on failure 2nd: " + e.getMessage());
                                }
                            }
                        });

            }
        });

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

    }

    private class dialogClick implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    void checkPermission(){
        if (ContextCompat.checkSelfPermission(
                mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            new SendLocationAsync(new SendLocationListener(),mContext).execute( );

        } else  if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            setDialog("GPS Permission","App require this permission to continue.",false);
        } else {
            PermissionsUtil.checkAndRequestPermissions(com.rt.qpay99.activity.ui.ForgotPasswordUI.this);
            return;

        }
    }

    private class LastKnowLocationResult implements LocationUtil.LastKnowLocationInterface {
        @Override
        public void onSuccess() {
            DLog.e(TAG, "LastKnowLocationResult onSuccess");
        }
        @Override
        public void onFailure() {
            DLog.e(TAG, "LastKnowLocationResult onFailure");
        }
    }

    private class handleCaptchaResultAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... voids) {
            String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + SITE_SECRET_KEY + "&response=" + userResponseToken;
          //  String url = "https://www.google.com/recaptcha/api/siteverify";
            DLog.e(TAG, "url " + url);
            String r = HttpUtil.httpPost(url, "");
            DLog.e(TAG, "" + r);
            return r;
        }


        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            if(resp==null)return;
            JSONObject o = null;
            pBar.setVisibility(View.GONE);
            try {
                DLog.e(TAG,"resp " + resp);
                o = new JSONObject(resp);
                Boolean b  = o.getBoolean("success");
                if(b){
                    new postDataAsync().execute();
                }else{
                    Toast.makeText(mContext,"Please try again later!!!",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void initData() {

    }

    private class postDataAsync extends AsyncTask<Void, Void, RTResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(RTResponse resp) {
            super.onPostExecute(resp);
            pBar.setVisibility(View.GONE);
            if (resp.isResultBoolean()) {
                Intent i = new Intent(mContext, OTPUI.class);
                i.putExtra("mCategory", Constants.CAT_FORGOTPASSWORD);
                i.putExtra("sClientUserName", msisdn);
                i.putExtra("sClientID", resp.getsResponseMessage());
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                com.rt.qpay99.activity.ui.ForgotPasswordUI.this.finish();

            } else {
//                Toast.makeText(mContext, "Please try again later!!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(mContext, OTPUI.class);
                i.putExtra("mCategory", Constants.CAT_FORGOTPASSWORD);
                i.putExtra("sClientUserName", "");
                i.putExtra("sClientID", "1");
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                com.rt.qpay99.activity.ui.ForgotPasswordUI.this.finish();
            }


        }

        @Override
        protected RTResponse doInBackground(Void... voids) {
            String sTs = FunctionUtil.getsDNReceivedID();
            //msisdn = "60166572577";
            String sEncKey = FunctionUtil.getsEncK(msisdn + "RichTech6318" + sTs);
            RTResponse c = rtWS.ForgotPasswordSendTac(msisdn, msisdn, sTs, sEncKey, "0", sDeviceID);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return c;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GPS_REQUEST) {
                DLog.e(TAG,"gps enabled");
                isGPS = true; // flag maintain before get location
            }
        }
    }

    private class SendLocationListener  implements SendLocationAsync.SendLocationAsyncListener {
        @Override
        public void onStart() {
            DLog.e(TAG,"onStart");
        }

        @Override
        public void onResult(String result) {
            DLog.e(TAG,"onResult");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case Constants.PERMISSIONS_REQUEST_LOCATION:
                break;

        }
    }

}

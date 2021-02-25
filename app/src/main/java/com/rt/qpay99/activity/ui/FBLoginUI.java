package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.R;
import com.rt.qpay99.object.RTResponse;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

public class FBLoginUI extends FragmentActivity {
    //CallbackManager callbackManager;

    private String TAG = this.getClass().getName();
    private Context mContext;

    String FBID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_fblogin_ui);


        TextView welcome =(TextView)findViewById(R.id.welcome) ;
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareToMessengerParams shareToMessengerParams =
//                        ShareToMessengerParams.newBuilder(contentUri, mimeType)
//                                .build();
            }
        });


//        callbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
//
//        loginButton.setReadPermissions(Arrays.asList(
//            "public_profile", "email", "user_birthday"));
//
//        loginButton.registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        DLog.e(TAG,"onSuccess ===============>" + loginResult.getAccessToken().getToken());
//                        DLog.e(TAG,"onSuccess ===============>" + loginResult.getAccessToken().getUserId());
//                        DLog.e(TAG,"onSuccess ===============>" + loginResult.getAccessToken().getApplicationId());
//                        DLog.e(TAG,"onSuccess ===============>" + loginResult.getAccessToken().getExpires());
//                        new GraphRequest(
//                                AccessToken.getCurrentAccessToken(),
//                                "/{user-id}",
//                                null,
//                                HttpMethod.GET,
//                                new GraphRequest.Callback() {
//                                    public void onCompleted(GraphResponse response) {
//                                        DLog.e("FacebookCallback","======================" +  response.toString());
//                                    }
//                                }
//                        ).executeAsync();
//                        GraphRequest request = GraphRequest.newMeRequest(
//                                loginResult.getAccessToken(),
//                                new GraphRequest.GraphJSONObjectCallback() {
//                                    @Override
//                                    public void onCompleted(JSONObject object, GraphResponse response) {
//                                        DLog.e("LoginActivity","======================" +  response.toString());
//                                        try{
//                                            Profile profile = Profile.getCurrentProfile();
//                                            DLog.e(TAG,"profile.getId()=" + profile.getId());
//                                            DLog.e(TAG,"profile.getName()=" + profile.getName());
//
//
//                                            Intent intent = new Intent(mContext, FBLoginUI.class);
//                                            FBID=object.getString("id");
//                                            DLog.e(TAG,"FBID=" + FBID);
//                                            DLog.e(TAG,"name=" + object.getString("name"));
//                                            DLog.e(TAG,"email=" + object.getString("email"));
//                                            FBUpdateFBIDAsync();
//                                            retriveFBInfo();
//
//                                        }catch (Exception ex){
//                                            DLog.e(TAG,"" + ex.getMessage());
//                                        }
//                                    }
//                                });
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id,name,email,gender,birthday");
//                        request.setParameters(parameters);
//                        request.executeAsync();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        DLog.e(TAG,"onCancel");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        DLog.e(TAG,"onError");
//                    }
//                });

    }

    void retriveFBInfo(){
//        new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/me/feed",
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        DLog.e("retriveFBInfo","======================" +  response.toString());
//                    }
//                }
//        ).executeAsync();


//        GraphRequest request2 = new GraphRequest(
//                AccessToken.getCurrentAccessToken(),
//                "/me/"+ FBID,
//                null,
//                HttpMethod.GET,
//                new GraphRequest.Callback() {
//                    public void onCompleted(GraphResponse response) {
//                        DLog.e("retriveFBInfo","======================" +  response.toString());
//                    }
//                }
//        );
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,gender,first_name");
//        request2.setParameters(parameters);
//        request2.executeAsync();
    }


    private ProgressDialog pd;
    private RTWS rtWS = new RTWS();
    private void FBUpdateFBIDAsync() {
        new AsyncTask<Void, Void, RTResponse>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
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
            protected RTResponse doInBackground(Void... arg0) {
                // TODO Auto-generated method stub
                String mobileNo = SharedPreferenceUtil.getsClientUserName();
                String sTS = FunctionUtil.getsDNReceivedID();
                String sEncKey = FunctionUtil.getsEncK(mobileNo + "RichTech6318" + sTS);
                return rtWS.FBUpdateFBID(FBID,String.valueOf(SharedPreferenceUtil.getsClientID()), mobileNo,SharedPreferenceUtil.getsClientPassword(),sTS,sEncKey);

            }

            @SuppressWarnings("unused")
            @Override
            protected void onPostExecute(RTResponse result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if(pd!=null)
                    pd.dismiss();
                if(result.isResultBoolean()){
                    transcationAlertDialog("" + result.getsResponseMessage()).show();

                }else{
                    transcationAlertDialog("" + result.getsResponseMessage()).show();
                }
            }

        }.execute();
    }

    private AlertDialog transcationAlertDialog(String msg) {
        return new AlertDialog.Builder(mContext)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

//    public boolean isLoggedIn() {
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        return accessToken != null;
//    }
}

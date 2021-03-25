package com.rt.qpay99.activity.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.gsm.GsmCellLocation;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

//import com.crashlytics.android.answers.Answers;
//import com.crashlytics.android.answers.LoginEvent;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Main4Activity;
import com.rt.qpay99.Permission.PermissionsUtil;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.object.AgentInfoObj;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.DeviceLoginResponse;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.service.MyFirebaseInstanceIDService;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import static com.rt.qpay99.SRSApp.mPackageInfo;


public class LoginBiometric  extends FragmentActivity {
    public LocationManager mLocationManager;
    RTWS rtWS = new RTWS();
    int sClientID;
    InputFilter[] filterArray = new InputFilter[1];
    String IP_Address;
    String Address = "";
    private Context mContext;
    private String TAG = this.getClass().getName();
    private ProgressDialog pd;
    private EditText edLoginMobile, edLoginPass;
    private String sClientUserName, sClientPassword, sDeviceID, sTAC;
    private TextView tvbuyPrinter, tvVersion, text_view;
    private List<ProductInfo> productInfos;
    private List<ProductInfo> topUps = new ArrayList<ProductInfo>();
    private List<ProductInfo> topUps_os = new ArrayList<ProductInfo>();
    private List<ProductInfo> payBills = new ArrayList<ProductInfo>();
    private List<ProductInfo> pins = new ArrayList<ProductInfo>();
    private List<ProductInfo> sims = new ArrayList<ProductInfo>();
    private List<ProductInfo> datas = new ArrayList<ProductInfo>();
    private List<ProductInfo> QPromoList = new ArrayList<ProductInfo>();
    private CheckBox saveLoginCheckBox;
    //UI VIEWS BIOMETRIC
    private Button authorize;
    private Button cancelButton;
    private TextView TouchText;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private static final String KEY_NAME = "com.rt.qpay99";
    private Cipher cipher;
    private Button encrypt;
    private Button decrypt;

    private  EditText input_text, password_text;
    private TextView output_text;
    private Button enc, dec;
    private String outputstring;
    private String AES = "AES";
    private String pwdtext;
    private String inptext;
    //private activitylayoutloginbiometric binding;

    TextView errorText;

    boolean isNotFirstWindowFocus = false;
    AgentInfoObj obj = new AgentInfoObj();
    boolean showPromo = false;

    KeyguardManager keyguardManager;

    private Activity mActivity;

    private com.beardedhen.androidbootstrap.BootstrapButton lupaPassword, btnDaftar;
    private com.beardedhen.androidbootstrap.AwesomeTextView  btnWhatsappAdmin;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Crashlytics.start(this);
        mContext = LoginBiometric.this;
//        binding = savedIdTouch.inflate(getLayoutInflater());
//        View view = binding.getRoot();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_layout_login_biometric);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(mContext,R.color.qpay));
        edLoginMobile =  findViewById(R.id.edLoginMobile);
        edLoginPass =  findViewById(R.id.edLoginPass);

        tvVersion =  findViewById(R.id.tvVersion);
        output_text = findViewById(R.id.output_text);
        tvVersion.setText("Version " + SRSApp.versionName);


        text_view = findViewById(R.id.text_view);
        text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ezeepayregister0103232663.wasap.my"));
                startActivity(i2);
            }
        });

        tvbuyPrinter =  findViewById(R.id.tvbuyPrinter);
        tvbuyPrinter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BuyPrinterUI.class);
                startActivity(intent);
            }
        });


        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, LoginUI.class);
                startActivity(i);
            }
        });


        encrypt = findViewById(R.id.encrypt);
        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inptext = edLoginMobile.getText().toString();
                    pwdtext = edLoginPass.getText().toString();
                    outputstring = encrypt(inptext, pwdtext);
                    output_text.setText(outputstring);

                    DLog.e(TAG, "CHECKING INPTEXT " + inptext);
                    DLog.e(TAG, "CHECKING PWDTEXT " + pwdtext);
                    DLog.e(TAG, "CHECKING OUTPUT ENCRYPT "+ outputstring);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decrypt = findViewById(R.id.decrypt);
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inptext = edLoginMobile.getText().toString();
                    pwdtext = edLoginPass.getText().toString();
                    outputstring = decrypt(outputstring, pwdtext);
                    output_text.setText(outputstring);

                    DLog.e(TAG, "CHECKING PWDTEXT " + pwdtext);
                    DLog.e(TAG, "CHECKING INPTEXT " + inptext);

                    DLog.e(TAG, "CHECKING OUTPUT DECRYPT "+ outputstring);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });





        authorize = findViewById(R.id.authorize);
        TouchText = findViewById(R.id.TouchText);
        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(LoginBiometric.this, executor,

                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        //error auth
                        TouchText.setText("Authentication error: " +errString);
                        Toast.makeText(LoginBiometric.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        DLog.e(TAG, "check name BIOMETRIC "+ sClientUserName);
                        DLog.e(TAG, " check password BIOMETRIC"+ sClientPassword);

                        TouchText.setText("Authentication success!");
                        Toast.makeText(LoginBiometric.this,"Authentication success!", Toast.LENGTH_SHORT).show();



                        inptext =  "6"+edLoginMobile.getText().toString();
                        pwdtext = edLoginPass.getText().toString();
//                        outputstring = encrypt(inptext,
//                                pwdtext);
//                        output_text.setText(outputstring);



                        DLog.e(TAG, "CHECKING INPTEXT inside id touch authorize " + inptext);
                        DLog.e(TAG, "CHECKING PWDTEXT inside button authorize " + pwdtext);

                        String TouchName = inptext + SharedPreferenceUtil.getBioName();
                        SharedPreferenceUtil.editBioName(TouchName);
                        DLog.e(TAG, " gg touch name : "+ TouchName);

                        String TouchPassword = pwdtext + SharedPreferenceUtil.getBioPassword();
                        SharedPreferenceUtil.editBioPassword(TouchPassword);
                        DLog.e(TAG, " gg touch password : "+ TouchPassword);

                        Intent intent = new Intent(mContext, LoginUI.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        //failed stop task
                        TouchText.setText("Authentication failed");
                        Toast.makeText(LoginBiometric.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });

        //setup title, description on auth dialog
        Context activity;
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint authentication")
//                .setNegativeButtonText("Cancel")
                .setDeviceCredentialAllowed(true)
//                .setNegativeButton()
////                .setNegativeButtonText(activity.getResources().getString("User App password"),
////                        activity.getMainExecutor(), new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialogInterface, int i) {
////                                if (mManagerIdentifyCallback != null) {
////                                    mManagerIdentifyCallback.onUsePassword();
////                                }
////                                mCancellationSignal.cancel();
////                            }
////                        })
                .build();

        //touch id function
//        idTouch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //show auth dialog
//                //biometricPrompt.authenticate(promptInfo);
//
//                Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ezeepayregister0103232663.wasap.my"));
//                startActivity(i2);
//            }
//        });

        authorize.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {




                //biometricPrompt.authenticate(promptInfo);

                if (!SRSApp.isNetworkConnected()) {
                    InvalidLoginAlertDialog("No Internet Connection.").show();
                    return;
                }

                sClientUserName = edLoginMobile.getText().toString();
                if (sClientUserName.matches("")) {
                    Toast.makeText(mContext, "You did not enter a Mobile No.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sClientUserName.length() < 8) {
                    Toast.makeText(mContext, "Please enter valid Mobile No.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sClientUserName.substring(0, 1).equalsIgnoreCase("0"))
                    sClientUserName = "6" + sClientUserName;

                sClientPassword = edLoginPass.getText().toString();
                if (sClientPassword.matches("")) {
                    Toast.makeText(mContext, "You did not enter a Password.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (PermissionsUtil.checkAllPermissionsGranted(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    int cellid = 0;
                    int celllac = 0;
                    try {

                        SRSApp.cellLocation = (GsmCellLocation) SRSApp.telephonyManager.getCellLocation();
                        if (SRSApp.cellLocation != null) {
                            cellid = SRSApp.cellLocation.getCid();
                            celllac = SRSApp.cellLocation.getLac();
                            DLog.e("CellLocation--------------------->", SRSApp.cellLocation.toString());
                            DLog.e("GSM CELL ID", String.valueOf(cellid));
                            DLog.e("GSM Location Code", String.valueOf(celllac));

                            String networkOperator = SRSApp.telephonyManager.getNetworkOperator();

                            if (!TextUtils.isEmpty(networkOperator)) {
                                int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                                int mnc = Integer.parseInt(networkOperator.substring(3));
                                DLog.e("mcc ", String.valueOf(mcc));
                                DLog.e("mnc ", String.valueOf(mnc));
                            }
                        }

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                    Bundle params = new Bundle();
                    params.putString("msisdn", SharedPreferenceUtil.getsClientUserName());
                    params.putString("mLoc", String.valueOf(celllac));
                    params.putString("deviceId", FunctionUtil.getDeviceIdOrAndroidId(mContext));
                    SRSApp.mFirebaseAnalytics.logEvent("LoginBiometric", params);
                    firebaseUI();

//                    new CheckLoginValidateAsync().execute();
                    DeviceLoginAsync();
                } else {
                    PermissionsUtil.requestPermissions(LoginBiometric.this, 1,
                            new String[]
                                    {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_PHONE_STATE,
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                    });
                }


            }

        });

        lupaPassword = findViewById(R.id.btnLupaPassword);
        btnDaftar = findViewById(R.id.btnDaftar);
        btnDaftar.setVisibility(View.GONE);
        //btnCareline = findViewById(R.id.btnCareline);


//        btnDaftar.setOnClickListener(new daftarSMSListener("Sila WhatsApp 0166572577 untuk matlumat lanjut atau tekan YES untuk hantar SMS DAFTAR NOW"));
        btnDaftar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/3aHhjN"));
                startActivity(i2);
            }
        });


        if (PermissionsUtil.checkAllPermissionsGranted(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {

        } else {
            PermissionsUtil.requestPermissions(this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            });
        }

        sDeviceID = "1";
        if (PermissionsUtil.checkAllPermissionsGranted(mContext, Manifest.permission.READ_PHONE_STATE)) {
            sDeviceID = FunctionUtil.getDeviceIdOrAndroidId(mContext);
        } else {
            PermissionsUtil.requestPermissions(this, READ_PHONE_STATE, new String[]{Manifest.permission.READ_PHONE_STATE});
        }

        if (PermissionsUtil.checkAllPermissionsGranted(mContext, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLocation();
        }


    }

    private String encrypt(String data, String edLoginPass ) throws Exception {
        SecretKeySpec key = generateKey(edLoginPass);
        Log.d("NIKHIL", "encrypt key:" + key.toString());
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes("UTF-8"));
        String encryptedvalue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedvalue;
    }

    private String decrypt(String data, String edLoginPass) throws Exception {
        SecretKeySpec key=generateKey(edLoginPass);

        Log.d("NIKHIL","encrypt key:"+key.toString());
        DLog.e(TAG, "CHECK INSIDE DECRYPT : " + data);
        Cipher c =Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedvalue =Base64.decode(data,Base64.DEFAULT);
        byte[] decvalue=c.doFinal(decodedvalue);
        String decryptedvalue=new String(decvalue,"UTF-8");
        return decryptedvalue;
    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    @Override
    protected void onResume() {

//        new versionCheck().execute();


        super.onResume();
    }

    boolean isForceUpdate = false;

    private static int READ_PHONE_STATE = 101;

    static int VERIFY_TAC = 801;

    private static final int PERMISSIONS_REQUEST = 100;


    private AlertDialog OptionUpdateAlertDialog(String msg) {

        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(true)
                .setNegativeButton("Not Now",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //SharedPreferenceUtil.editFirstLogin(false);
                            }
                        })
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                gotoPlayStore();
                            }
                        }).create();
    }

    void gotoPlayStore() {
        final String appPackageName = mContext.getPackageName();
        DLog.e(TAG, "appPackageName " + appPackageName);
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(
                    "https://play.google.com/store/apps/details?id=" + appPackageName));
            intent.setPackage("com.android.vending");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + appPackageName)));
        }

    }

    private AlertDialog ForceUpdateAlertDialog(String msg) {

        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)

                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                gotoPlayStore();
                            }
                        }).create();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(mContext, "Access Denied, Please reset the permission to login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DeviceLoginAsync() {
        new AsyncTask<Void, Void, DeviceLoginResponse>() {

            @Override
            protected void onPreExecute() {

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
            protected DeviceLoginResponse doInBackground(Void... arg0) {
                sDeviceID = FunctionUtil.getDeviceIdOrAndroidId(mContext);
                DLog.e(TAG, "sDeviceID " + sDeviceID);
                SharedPreferenceUtil.editsDeviceID(sDeviceID);
                return rtWS.DeviceLogin(sClientUserName, sClientPassword,
                        sDeviceID);

            }

            @Override
            protected void onPostExecute(DeviceLoginResponse result) {
                super.onPostExecute(result);
                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

                if (result == null) {
                    try {
//                        Answers.getInstance().logLogin(new LoginEvent()
//                                .putMethod("Digits")
//                                .putSuccess(false));

                        InvalidLoginAlertDialog(
                                "Please contact admin!")
                                .show();


                    } catch (Exception e) {
                        InvalidLoginAlertDialog(
                                "Tiada sambungan internet!!!")
                                .show();
                        return;
                    }
                    return;
                }

                try {
                    sClientID = result.getsClientID();
                }
                catch (Exception ex) {
                    sClientID = 0;
                }

                Config.sClientID = sClientID;
                String loginstatus = sClientUserName + "," + SharedPreferenceUtil.getLoginStatus();
                if(loginstatus.length()>90){
                    loginstatus = loginstatus.substring(0,90);
                }

                SharedPreferenceUtil.editLoginStatus(loginstatus);
                DLog.e(TAG,"loginstatus ------------------- " + loginstatus);
                SharedPreferenceUtil.editsClientPassword(sClientPassword);
                SharedPreferenceUtil.editsClientUserName(sClientUserName);
                if (FunctionUtil.isSet(SharedPreferenceUtil
                        .getsClientUserName())) {

                    try {
                        Config.sMasterID = result.getsMasterID();
                        SharedPreferenceUtil.editMerchantName(result
                                .getsMerchantName());
                    } catch (Exception e) {
                        Config.sMasterID = "1";
                        SharedPreferenceUtil.editMerchantName("");
                    }
                    SharedPreferenceUtil.editClientMasterId(Config.sMasterID);
                    DLog.e(TAG, "Config.sMasterID " + Config.sMasterID);
                    DLog.e(TAG, "getMessageInfo");
                    getMessageInfo("MESSAGE_TAB");

                }

                if (result.isDeviceLoginResult()) {
                    try {

                        biometricPrompt.authenticate(promptInfo);
//                        Answers.getInstance().logLogin(new LoginEvent()
//                                .putMethod("Digits")
//                                .putSuccess(true));

                    } catch (Exception ex) {

                    }
//                    if (saveLoginCheckBox.isChecked()) {
//                        SharedPreferenceUtil.editSaveLogin(true);
//                    }

//                    else
//                        SharedPreferenceUtil.editSaveLogin(false);
//
//                    if (!result.isbVerifyTac()) {
//                        GetAgentProductDiscount();
////                        getProductInfo(sClientID);
//                        // TACAlertDialog("Please enter TAC").show();
//                    }
//                    else {
//                        //TACAlertDialog("Please enter TAC").show();
//                        Intent tacVerify = new Intent(mContext, TACUI.class);
//                        startActivityForResult(tacVerify, VERIFY_TAC);
                   // }

                }
                else {
                    try {
//                        Answers.getInstance().logLogin(new LoginEvent()
//                                .putMethod("Digits")
//                                .putSuccess(false));

                    } catch (Exception ex) {

                    }
                    InvalidLoginAlertDialog(
                            "ID penguna dengan Kata Laluan tidak sepadan! \n\nMobile Number & Password not match!")
                            .show();
                }

            }

        }.execute();
    }

    private AlertDialog InvalidLoginAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
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

    private AlertDialog InvalidLoginTACAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(R.string.app_name)
                .setMessage(msg)

                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                TACAlertDialog("Please enter TAC").show();
                            }
                        }).create();
    }

    private AlertDialog TACAlertDialog(String msg) {
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        filterArray[0] = new InputFilter.LengthFilter(10);
        input.setFilters(filterArray);
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle("TAC Code")
                .setMessage(msg)
                .setView(input)
                .setCancelable(false)
                .setNegativeButton(R.string.title_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                DeviceVerifyTACAsync(input.getText().toString());
                            }
                        }).create();
    }

    private void DeviceVerifyTACAsync(final String tac) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
//                tvLoadingText.setText("Verifying ...");
                // llCustomPB.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... arg0) {
                sTAC = tac;
                //sDeviceID = FunctionUtil.getDeviceIdOrAndroidId(mContext);
                DLog.e(TAG, "sDeviceID " + sDeviceID);
                SharedPreferenceUtil.editsDeviceID(sDeviceID);
                return rtWS.DeviceVerifyTAC(sClientUserName, sClientPassword,
                        sDeviceID, sClientID, sTAC);

            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    GetAgentProductDiscount();
//                        getProductInfo(sClientID);
                } else {
                    InvalidLoginTACAlertDialog(
                            "TAC tidak sepadan! \n\nTAC not match!").show();
                }
            }

        }.execute();
    }

    private void gotoHomePage() {
        new UpdatePushNotificationIDAsync().execute();
        pins = SharedPreferenceUtil.getPRODUCT_PIN_PREFERENCE();
        payBills = SharedPreferenceUtil.getPRODUCT_PAYBILL_PREFERENCE();
        topUps_os = SharedPreferenceUtil.getPRODUCT_TOPUP_OVERSEA_PREFERENCE();
        topUps = SharedPreferenceUtil.getPRODUCT_TOPUP_PREFERENCE();
        Intent intent = new Intent(mContext, Main4Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // llCustomPB.setVisibility(View.GONE);
        mContext.startActivity(intent);
        finish();
    }


    private void getProductInfo(int sClientID) {
        DLog.e(TAG, "gotoHomePage");
        SRSApp.sClientUserName = sClientUserName;
        SRSApp.sClientPassword = sClientPassword;
        SharedPreferenceUtil.editsClientUserName(sClientUserName);
        SharedPreferenceUtil.editsClientPassword(sClientPassword);
        SharedPreferenceUtil.editsClientID(sClientID);
        // Retrive data
        Long expires = SharedPreferenceUtil.getSessionExpired();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        DLog.e(TAG, "currentDateandTime =======================>" + currentDate);
        if (currentDate.equalsIgnoreCase("20151231")) {
            expires = System.currentTimeMillis() - 100000;
            DLog.e(TAG, "RESET expires");
        }
        if (System.currentTimeMillis() > expires) {
            SharedPreferenceUtil.editgetSessionExpired(System.currentTimeMillis() + 604800000);
            new LoginBiometric.retrieveProductInfoAsync().execute();
        } else {
            gotoHomePage();
        }


    }

    public void showSettingsAlert(final Context c) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Please enable it.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        c.startActivity(intent);
                        System.exit(0);
                    }
                });

        if (alertDialog != null)
            alertDialog.show();
    }

    private void UpdatePushNotificationID() {
        MyFirebaseInstanceIDService myFirebaseInstanceIDService = new MyFirebaseInstanceIDService();
        myFirebaseInstanceIDService.onTokenRefresh();
        DLog.e(TAG, "UpdatePushNotificationID ================> "
                + Config.GCM_RegId);
        rtWS.UpdatePushNotificationID(sClientUserName, sClientPassword,
                SharedPreferenceUtil.getGcmRegisterId());

    }

    private void getMessageInfo(final String sMessageType) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... arg0) {
                return rtWS
                        .GetMessageInfo(
                                SharedPreferenceUtil.getsClientUserName(),
                                SharedPreferenceUtil.getsClientPassword(),
                                sMessageType);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String mText = SharedPreferenceUtil.getMovingText();
                if (FunctionUtil.isSet(result)) {
                    if (!result.equalsIgnoreCase(mText)) {
                        SharedPreferenceUtil.editMovingText(result);
                        mText = result;
                    }
                    Config.MOVING_TEXT = mText;
                }
            }

        }.execute();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        DLog.e(TAG, "" + requestCode);
        if (requestCode == VERIFY_TAC) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                GetAgentProductDiscount();
//                        getProductInfo(sClientID);
            } else {

            }
        }


    }

    private void getLocation() {
        if (SRSApp.lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            int permission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (permission == PackageManager.PERMISSION_GRANTED) {
                firebaseUI();
//                loginToFirebase();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST);
            }
        }
    }

    void firebaseUI() {
        DLog.e(TAG, "firebaseUI hit------------");

        if (FirebaseAuth.getInstance() == null) return;
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                Config.FirebaseStorageID, Config.FirebaseStoragePassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DLog.d(TAG, "Firebase Successful login");

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geofirelocation");
                    final GeoFire geoFire = new GeoFire(ref);

                    DLog.d(TAG, "requestLocationUpdates");
                    LocationRequest request = new LocationRequest();
                    request.setInterval(3600000);
                    request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(mContext);

                    int permission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        client.requestLocationUpdates(request, new LocationCallback() {
                            @Override
                            public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                Location location = locationResult.getLastLocation();
                                if (location != null) {
                                    DLog.e(TAG, "Config.VersionCheck_Status - " + Config.VersionCheck_Status);
                                    if (Config.VersionCheck_Status.equalsIgnoreCase("ACTIVE")) {
                                        DLog.e(TAG, "set location ------------------------------" + location.getProvider());
                                        Calendar cal = Calendar.getInstance();
                                        Config.Latitude = location.getLatitude();
                                        Config.Longitude = location.getLongitude();
                                        String loc = String.valueOf(location.getLatitude()) + "|" + String.valueOf(location.getLongitude());
                                        if (FunctionUtil.isSet(SharedPreferenceUtil.getsClientUserName())) {
                                            DLog.e(TAG, "postdata -------------- >");
                                            postdata(loc);
                                            new LoginBiometric.postDataAsync().execute();
                                        }
                                        geoFire.setLocation(SharedPreferenceUtil.getMerchantName() + "|" + SharedPreferenceUtil.getsClientUserName(),

                                                new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                                                    @Override
                                                    public void onComplete(String key, DatabaseError error) {
                                                        DLog.e(TAG, "key ------------" + key);
                                                        DLog.e(TAG, " geoFire.setLocation hit------------");
                                                        //                DLog.e(TAG,"error ------------"+ error.getMessage());
                                                    }
                                                });
                                    }
                                }
                            }
                        }, null);
                    }
                } else {
                    DLog.d(TAG, "Firebase authentication failed");
                }
            }
        });
    }

    void postdata(String loc) {

        obj.setAgentID(0);
        obj.setName(SharedPreferenceUtil.getMerchantName());
        obj.setHPNo(SharedPreferenceUtil.getsClientUserName());
        obj.setStatus("PENDING");
        obj.setState("");
        obj.setDistrict("");
        obj.setPrefixFilter("");
        obj.setParentAgentID(SharedPreferenceUtil.getsClientID());
        obj.setBanner("");
        obj.setCreatedBy("QPAY99");
        obj.setCreatedDate("");
        obj.setLastModifiedBy(sDeviceID);
        obj.setLastModifiedDate("");
        obj.setAddress("");
        obj.setEmail("v" + SRSApp.versionName);
        obj.setLocation(loc);
    }

    private void GetAgentProductDiscount() {

        new AsyncTask<String, Void, List<AgentProductDiscount>>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            protected List<AgentProductDiscount> doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                return rtWS.GetAgentProductDiscount(SharedPreferenceUtil.getsClientUserName(),
                        SharedPreferenceUtil.getsClientPassword());
            }

            @Override
            protected void onPostExecute(List<AgentProductDiscount> result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                SRSApp.DiscountRates = result;

                for (AgentProductDiscount p : result) {
                    SRSApp.hashmapDiscountRate.put(p.getProductName(), p);
                }
                getProductInfo(sClientID);


            }

        }.execute();

    }


    @Override
    public void onPause() {
        super.onPause();
        // llCustomPB.setVisibility(View.GONE);
    }

    private class versionCheck extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            tvLoadingText.setText("Version check....");
            //   llCustomPB.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            HttpHandlerHelper sh = new HttpHandlerHelper();
            String url = Config.VersionCheckURL + getString(R.string.app_name);
            DLog.e(TAG, "--------------- " + url);
            String jsonStr = sh.makeServiceCall(url);
            DLog.e(TAG, "Response from url: " + jsonStr);

            try {
                if (TextUtils.isEmpty(jsonStr)) return false;
                JSONArray jsonArray = new JSONArray(jsonStr);
                JSONObject json = jsonArray.getJSONObject(0);
                DLog.e(TAG, "" + json.toString());
                DLog.e(TAG, "AppVersion " + json.getDouble("AppVersion"));
                DLog.e(TAG, "AppName " + json.getString("AppName"));
                DLog.e(TAG, "Status " + json.getString("Status"));
                DLog.e(TAG, "ForceUpgrade " + json.getInt("ForceUpgrade"));

                isForceUpdate = false;
                if (json.getInt("ForceUpgrade") > 0) {
                    isForceUpdate = true;
                }

                Config.VersionCheck_Status = json.getString("Status");

                DLog.e(TAG, "versionCode " + mPackageInfo.versionCode);
                if (mPackageInfo.versionCode < json.getDouble("AppVersion")) {
                    DLog.e(TAG, "Need Update ");
                    return true;
                } else {
                    DLog.e(TAG, "NO Need Update ");
                    return false;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }



    public class retrieveProductInfoAsync extends
            AsyncTask<String, Void, List<ProductInfo>> {

        protected void onPreExecute() {

            pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
            SpannableString ss1 = new SpannableString("Sila tunggu ...");
            ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
            ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                    ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                    0, ss1.length(), 0);
            pd = new ProgressDialog(mContext);
            pd.setTitle(ss1);
            pd.setMessage("Pemuat turun data ...");
            pd.setCancelable(false);
            pd.show();
        }

        protected List<ProductInfo> doInBackground(String... arg0) {
            RTWS cs = new RTWS();
            return cs.getProductInfo();

        }

        protected void onPostExecute(List<ProductInfo> result) {
            pd.dismiss();
            for (ProductInfo product : result) {
//                if (product.getName().toLowerCase().substring(0,product.getName().length()-3)=="pin") {
                if (product.getName().toLowerCase().indexOf("pin") > 0) {
                    DLog.e("PIN", product.getName());
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        pins.add(product);
                } else if (product.getName().toLowerCase().indexOf("bill") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        payBills.add(product);
                    DLog.e("BILL", product.getName());
                } else if (product.getName().toLowerCase().indexOf("flexi") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        topUps_os.add(product);
                    DLog.e("FLEXI", product.getName());
                } else if (product.getName().toLowerCase().indexOf("sim") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        sims.add(product);
                    DLog.e("SIM", product.getName());
                } else if (product.getName().toLowerCase().indexOf("data") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        datas.add(product);
                    DLog.e("SIM", product.getName());
                } else if (product.getName().toLowerCase().indexOf("_qpromo") > 0) {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus()))
                        QPromoList.add(product);
                    DLog.e("QPromoList", product.getName());
                } else {
                    if (!"INACTIVE".equalsIgnoreCase(product.getStatus())) {
                        if (!product.getName().equalsIgnoreCase("QDRAW"))
                            topUps.add(product);
//                        if (product.getName().equalsIgnoreCase("MAXIS")) {
//                            Config.GST_RATE = product.getTax();
//                        }
                    }

                    DLog.e("ORTHES", product.getName());
                }
                SharedPreferenceUtil.editPRODUCT_PAYBILL_PREFERENCE(payBills);
                SharedPreferenceUtil.editPRODUCT_SIM_PREFERENCE(sims);
                SharedPreferenceUtil.editPRODUCT_DATA_PREFERENCE(datas);
                SharedPreferenceUtil.editPRODUCT_PIN_PREFERENCE(pins);
                SharedPreferenceUtil
                        .editPRODUCT_TOPUP_OVERSEA_PREFERENCE(topUps_os);
                SharedPreferenceUtil.editPRODUCT_QPROMO_PREFERENCE(QPromoList);
                SharedPreferenceUtil.editPRODUCT_TOPUP_PREFERENCE(topUps);
                SharedPreferenceUtil.editGetProductInfo(false);
            }
            new LoginBiometric.UpdatePushNotificationIDAsync().execute();
            gotoHomePage();

        }
    }


    private class UpdatePushNotificationIDAsync extends
            AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            DLog.e(TAG, "UpdatePushNotificationIDAsync");
            UpdatePushNotificationID();
            return null;
        }
    }

    public String httpPost(String url, String data) {
        HttpURLConnection httpcon;
        String result = null;
        try {
            //Connect
            httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();
            DLog.e(TAG, "" + result);

        } catch (UnsupportedEncodingException e) {
            DLog.e(TAG, "UnsupportedEncodingException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            DLog.e(TAG, "IOException " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private class postDataAsync extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);


        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = Config.AZUREMAIN_URL + Config.AGENT_URL;
            DLog.e(TAG, "url " + url);
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            DLog.e(TAG, "json " + json);
            String r = httpPost(url, json);
            DLog.e(TAG, "" + r);
            return r;
        }
    }



}

package com.rt.qpay99;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.rt.qpay99.activity.ui.FBShareUI;
import com.rt.qpay99.activity.ui.FeedbackFormUI;
import com.rt.qpay99.activity.ui.LuckyDrawPageUI;
import com.rt.qpay99.activity.ui.PointRedeemPageUI;
import com.rt.qpay99.activity.ui.ShakeAndWinUI;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.fragment.HomeFragment;
import com.rt.qpay99.fragment.MyAccountFragment_v2;
import com.rt.qpay99.object.AgentLoginHistoryObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main4Activity extends AppCompatActivity {

    private TextView mTextMessage;
    private String TAG=this.getClass().getName();
    private boolean isChangePassword;
    private RTWS rtWS=new RTWS();
    private Context mContext;
    BottomSheetDialog mBottomSheetDialog;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
//                case R.id.navigation_dashboard:
//                    fragment = new BuyProductFragmentUI();
//                    break;
//
//                case R.id.navigation_QPromo:
////                    fragment = new QPromoListFragment();
//                    break;
//
//                case R.id.navigation_reward:
//                    fragment = new QPointsGiftFragment();
//                    break;
                case R.id.navigation_notifications:
                    fragment = new MyAccountFragment_v2();
                    break;
            }
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();

            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main4);
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mContext = this;
        setTitle(getString(R.string.app_name));
//        getSupportActionBar().setSubtitle(SharedPreferenceUtil.getsClientUserName() + "      Version "
//                + SRSApp.versionName);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        getSupportActionBar().hide();

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);
        Fragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

        com.rt.qpay99.component.MovableFloatingActionButton fab =  findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog = new BottomSheetDialog(mContext);
                mBottomSheetDialog.setContentView(R.layout.view_bottomsheetdialog_mainpage);

                final Button btnFeedback, btnFBShare,btnRedeem,btnQPointShare,btnLuckyDraw,btnShake;
                btnFeedback = mBottomSheetDialog.findViewById(R.id.btnFeedback);
                btnFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mContext, FeedbackFormUI.class));
                        mBottomSheetDialog.dismiss();
                    }
                });

                btnFBShare = mBottomSheetDialog.findViewById(R.id.btnFBShare);
                btnFBShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mContext, FBShareUI.class));
                        mBottomSheetDialog.dismiss();
                    }
                });


                btnRedeem = mBottomSheetDialog.findViewById(R.id.btnRedeem);
                btnRedeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mContext, PointRedeemPageUI.class));
                        mBottomSheetDialog.dismiss();
                    }
                });

                btnLuckyDraw = mBottomSheetDialog.findViewById(R.id.btnLuckyDraw);
                btnLuckyDraw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.dismiss();
                        startActivity(new Intent(mContext, LuckyDrawPageUI.class));
                        mBottomSheetDialog.dismiss();
                    }
                });


                btnQPointShare = mBottomSheetDialog.findViewById(R.id.btnQPointShare);
                btnQPointShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.dismiss();
                        Toast.makeText(mContext, "Coming Soon!", Toast.LENGTH_SHORT).show();
                    }
                });

                btnShake = mBottomSheetDialog.findViewById(R.id.btnShake);
                btnShake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.dismiss();
                        startActivity(new Intent(mContext, ShakeAndWinUI.class));
//                        Toast.makeText(mContext, "Coming Soon!", Toast.LENGTH_SHORT).show();
                    }
                });


                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.show();
//                startActivity(new Intent(mContext, FeedbackFormUI.class));
            }
        });
        postdata();
        new postDataAsync().execute();
    }

    AgentLoginHistoryObj obj = new AgentLoginHistoryObj();
    void postdata() {
        obj.Channel = "ANDROID " + SRSApp.versionName;
        obj.DeviceID = FunctionUtil.getDeviceIdOrAndroidId(mContext);
        obj.IPAddress = "";
        obj.LoginStatus = SharedPreferenceUtil.getLoginStatus();
        obj.MSISDN = SharedPreferenceUtil.getsClientUserName();
        obj.setLocation(String.valueOf(Config.Latitude ) + "," + String.valueOf(Config.Longitude));
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
            String url = Config.AZUREMAIN_URL + Config.LoginHistory_URL;
            DLog.e(TAG, "url " + url);
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            DLog.e(TAG, "json " + json);
            String r = httpPost(url, json);
            DLog.e(TAG, "" + r);
            return r;
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

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_activity_v3, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_change_password) {
            DLog.e(TAG, "action_change_password");
            isChangePassword = true;
            changePasswordAlertDialog("Verify password").show();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog changePasswordAlertDialog(final String msg) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //input.setTransformationMethod(new MainActivity_v3.my);
        input.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(input, 0);
            }
        }, 50);

        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                // .setTitle("Verify Password")
                .setView(input)
                .setCancelable(true)
                .setMessage(msg)
                .setNegativeButton(R.string.title_cancel, null)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                VerifyChangePassword(input.getText().toString());
                            }
                        }).create();
    }

    void VerifyChangePassword(String msisdn) {
        if (SharedPreferenceUtil.getsClientPassword().equals(msisdn)) {
            DLog.e(TAG, "SUCCESS VerifyChangePassword");
            newPasswordAlertDialog("Please insert new password.").show();
        } else {
            changePasswordAlertDialog("Invalid password.\nPlease try again")
                    .show();
        }
    }

    private AlertDialog newPasswordAlertDialog(final String msg) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(new MyPasswordTransformationMethod());
        input.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(input, 0);
            }
        }, 50);

        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                // .setTitle("Verify Password")
                .setView(input)
                .setCancelable(false)
                .setMessage(msg)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                changePasswordAsync(input.getText().toString());
                            }
                        }).create();
    }

    private void changePasswordAsync(final String sNewPassword) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();

            }

            @Override
            protected Boolean doInBackground(Void... arg0) {
                // TODO Auto-generated method stub

                if (sNewPassword.length() == 8)
                    return rtWS.changePassword(
                            SharedPreferenceUtil.getsClientUserName(),
                            SharedPreferenceUtil.getsClientPassword(),
                            sNewPassword);

                return false;

            }

            @Override
            protected void onPostExecute(Boolean result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result) {
                    Toast.makeText(mContext, "Password updated",
                            Toast.LENGTH_SHORT).show();
                    SharedPreferenceUtil.editsClientPassword(sNewPassword);
                } else
                    Toast.makeText(
                            mContext,
                            "Password update failed, Please insert 8 digit ONLY",
                            Toast.LENGTH_SHORT).show();

            }

        }.execute();
    }


    public class MyPasswordTransformationMethod extends
            PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new MyPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitAlertDialog("Are you sure want to exit?").show();
        }
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            exitAlertDialog("Are you sure want to exit?").show();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private AlertDialog exitAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (PrintDataService.isPrinterConnected())
                                    PrintDataService.disconnect();
                                finish();
                            }
                        }).setNegativeButton("NO", null)
                .create();
    }

}

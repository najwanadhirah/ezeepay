package com.rt.qpay99;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.rt.qpay99.activity.ui.LoginUI;
import com.rt.qpay99.bluetooth.service.BluetoothService;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.fragment.MyAccountFragmentUI;
import com.rt.qpay99.fragment.PINFragmentUI;
import com.rt.qpay99.fragment.PayBillFragmentUI;
import com.rt.qpay99.fragment.SettingFragmentUI;
import com.rt.qpay99.fragment.SignUpFragment;
import com.rt.qpay99.fragment.TopupFragmentUI;
import com.rt.qpay99.fragment.WhatsHotFragment;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.rt.qpay99.SRSApp.sClientPassword;
import static com.rt.qpay99.SRSApp.sClientUserName;

public class MainActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static BluetoothService bt;
    public PrintDataService printDataService = null;
    /**
     * The {@link androidx.core.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link androidx.core.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link androidx.core.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    long userInteractionTime = 0;
    long MinutesInMillis = TimeUnit.MINUTES.toMillis(Config.INACTIVE_TIMES);
    boolean checkInActive = false;
    RTWS rtWS = new RTWS();
    private ProgressDialog progress;
    private Context mContext;
    private List<ProductInfo> productInfos;
    private List<ProductInfo> topUps = new ArrayList<ProductInfo>();
    private List<ProductInfo> topUps_os = new ArrayList<ProductInfo>();
    private List<ProductInfo> payBills = new ArrayList<ProductInfo>();
    private List<ProductInfo> pins = new ArrayList<ProductInfo>();
    private List<ProductInfo> sims = new ArrayList<ProductInfo>();
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private String TAG = this.getClass().getName();
    private boolean isChangePassword = false;
    private Locale myLocale;
    private BroadcastReceiver mSMSReceiver;
    private ProgressDialog pd;
    private BroadcastReceiver mBlueToothReceiver;

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#952B2B2B"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar.setTitle("QPay99.com");
            actionBar.setSubtitle(SharedPreferenceUtil.getsClientUserName() + "      Version "
                    + SRSApp.versionName);
        }


//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // actionBar.setDisplayShowHomeEnabled(false);
        // actionBar.setDisplayShowTitleEnabled(false);
        // actionBar.setDisplayUseLogoEnabled(false);
        mContext = this;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            if (i == 3) {
                actionBar.addTab(actionBar.newTab()
                        .setText(mSectionsPagerAdapter.getPageTitle(i))
                        .setIcon(R.drawable.ic_priority_high_white_24dp)
                        .setTabListener(this));
            } else
                actionBar.addTab(actionBar.newTab()
                        .setText(mSectionsPagerAdapter.getPageTitle(i))
                        .setTabListener(this));

        }

        hideSystemUi();

    }

    private AlertDialog showAlertDialog(final String mTitle, final String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle(mTitle)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_help:
                DLog.e(TAG, "action_help");
                String mobileNo = Config.Custotmer_care;
                showAlertDialog("SRS HELP",
                        "For asistance please call " + mobileNo).show();
                return true;
            case R.id.action_auto_lock:
                DLog.e(TAG, "action_auto_lock");
                if (SharedPreferenceUtil.isRequiredCheckPassword()) {
                    SharedPreferenceUtil.editIsRequiredVerify(false);
                    Toast.makeText(mContext, "Screen auto lock - OFF",
                            Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferenceUtil.editIsRequiredVerify(true);
                    Toast.makeText(mContext, "Screen auto lock - ON",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_change_password:
                DLog.e(TAG, "action_change_password");
                isChangePassword = true;
                changePasswordAlertDialog("Verify password").show();

                return true;

//		case R.id.action_change_language:
//			DLog.e(TAG, "action_change_language");
//			setLocale("en");
//			return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    DLog.e(TAG, "RESULT_OK");
                    // bt = new BluetoothService(mContext);
                    // bt.searchDevices();

                } else {
                    Toast.makeText(mContext, "Failed Connect", Toast.LENGTH_SHORT).show();
                }
                break;
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

    @Override
    public void onUserInteraction() {

        long uiDelta = (System.currentTimeMillis() - userInteractionTime);
        super.onUserInteraction();
        DLog.e(TAG, "Interaction " + userInteractionTime);
        DLog.e(TAG, "Interaction " + uiDelta);
        userInteractionTime = System.currentTimeMillis();
        if (checkInActive)
            if (uiDelta > MinutesInMillis) {
                DLog.e(TAG, "======================================>");
                if (SharedPreferenceUtil.isRequiredCheckPassword())
                    insertMSISDNAlertDialog("Please insert password").show();
                return;
            }
        checkInActive = true;
    }

    private AlertDialog insertMSISDNAlertDialog(final String msg) {
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
                                validate(input.getText().toString());
                            }
                        }).create();
    }

    void validate(String msisdn) {
        if (SharedPreferenceUtil.getsClientPassword().equals(msisdn)) {
            DLog.e(TAG, "SUCCESS VEFIRY");
            userInteractionTime = System.currentTimeMillis();

        } else {
            insertMSISDNAlertDialog("Invalid password.").show();
        }
    }

    @Override
    public void onUserLeaveHint() {
        long uiDelta = (System.currentTimeMillis() - userInteractionTime);

        super.onUserLeaveHint();
        DLog.e(TAG, "Last User Interaction = " + uiDelta);
        if (uiDelta < 100)
            DLog.e(TAG, "Home Key Pressed");
        else
            DLog.e(TAG, "We are leaving, but will probably be back shortly!");
    }

    private AlertDialog changePasswordAlertDialog(final String msg) {
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

    void VerifyChangePassword(String msisdn) {
        if (SharedPreferenceUtil.getsClientPassword().equals(msisdn)) {
            DLog.e(TAG, "SUCCESS VerifyChangePassword");
            newPasswordAlertDialog("Please insert new password.").show();
        } else {
            changePasswordAlertDialog("Invalid password.\nPlease try again")
                    .show();
        }
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

    public void setLocale(String lang) {
        // myLocale = new Locale(lang);
        // Resources res = getResources();
        // DisplayMetrics dm = res.getDisplayMetrics();
        // Configuration conf = res.getConfiguration();
        // conf.locale = myLocale;
        // res.updateConfiguration(conf, dm);
        // Intent refresh = new Intent(this, AndroidLocalize.class);
        // startActivity(refresh);

        Resources res = mContext.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);

    }

    private void SMSReceivedBroadcastReceiver() {
        if (mSMSReceiver == null) {
            mSMSReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    DLog.e(TAG, "RECEIVED SMSES");
                    // Retrieves a map of extended data from the intent.
                    final Bundle bundle = intent.getExtras();

                    try {

                        if (bundle != null) {

                            final Object[] pdusObj = (Object[]) bundle
                                    .get("pdus");

                            for (int i = 0; i < pdusObj.length; i++) {

                                SmsMessage currentMessage = SmsMessage
                                        .createFromPdu((byte[]) pdusObj[i]);
                                String phoneNumber = currentMessage
                                        .getDisplayOriginatingAddress();

                                String senderNum = phoneNumber;
                                String message = currentMessage
                                        .getDisplayMessageBody();

                                DLog.i("SmsReceiver", "senderNum: " + senderNum
                                        + "; message: " + message);

                                if (message.trim()
                                        .indexOf("kad ahli tidah sah") > -1) {
                                    DLog.e(TAG,
                                            "kad ahli tidah sah ==========================");
                                    mAlertDialog("Kad ahli tidak sah",
                                            "Pelanggan yang tidak ada kad ahli sila daftar sebelum hantar.")
                                            .show();
                                    return;
                                }

                                if (message.trim().indexOf("Remit MoneyPIN") > -1) {
                                    DLog.e(TAG,
                                            "kad ahli tidah sah ==========================");
                                    mAlertDialog("Remit MoneyPIN",
                                            "Minimum RM5.00").show();
                                    return;
                                }

                                // MM akan keluar MoneyPIN
                                if (message.trim().indexOf(
                                        "MM akan keluar MoneyPIN") > -1) {
                                    ConfirmMobileMoneyAlertDialog(message,
                                            senderNum).show();
                                    return;
                                }

                            }
                        }

                    } catch (Exception e) {
                        Log.e("SmsReceiver", "Exception smsReceiver" + e);

                    }

                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");

            filter.setPriority(1000);

            mContext.registerReceiver(mSMSReceiver, filter);
        }

    }

    private AlertDialog mAlertDialog(String mTitle, String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(mTitle)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create();
    }

    private AlertDialog ConfirmMobileMoneyAlertDialog(final String msg,
                                                      final String senderNum) {

        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle("IDMONEYPIN")
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String[] msgs = msg.split(":");
                                if (msgs.length > 1)
                                    sendSMS(msgs[1], senderNum);
                            }
                        }).create();
    }

    protected void unregisterSMSReceivedReceiver() {
        if (mSMSReceiver != null) {
            try {
                this.unregisterReceiver(mSMSReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSMSReceiver = null;
        }
    }

    protected void unregisterBluetoothReceivedReceiver() {
        if (mBlueToothReceiver != null) {
            try {
                this.unregisterReceiver(mBlueToothReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mBlueToothReceiver = null;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        DLog.e(TAG, "onResume");
        SMSReceivedBroadcastReceiver();
        mBlueToothBroadcastReceiver();
        hideSystemUi();

        /*SRSApp.sClientUserName=null;*/

        if (sClientUserName == null) {
            sClientUserName = SharedPreferenceUtil.getsClientUserName().toString();
            /*SessionExpiredAlertDialog("QPay99 Alert","Sesi tamat. Sila log masuk semula!!").show();*/
        }


        if (sClientUserName == null) {
            SessionExpiredAlertDialog("QPay99 Alert", "Sesi tamat. Sila log masuk semula!!").show();
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (bt != null)
            bt.unRegisterBTReceiver();
        super.onStop();
        DLog.e(TAG, "onStop");
        unregisterSMSReceivedReceiver();
        unregisterBluetoothReceivedReceiver();
    }

    @Override
    protected void onStart() {
       // mGoogleApiClient.connect();
        super.onStart();
    }


    private void sendSMS(final String msg, final String mobile) {
        try {
            DLog.e(TAG, "msg " + msg);
            DLog.e(TAG, "mobile " + mobile);
            String[] msgs = msg.split(" ");
            DLog.e(TAG, "msgs[1] " + msgs[1]);
            DLog.e(TAG, "msgs[2] " + msgs[2]);
            SmsManager smsManager = SmsManager.getDefault();
            String newMsg = msgs[1] + " " + msgs[2];
            smsManager.sendTextMessage(mobile, null, newMsg, null, null);
            Toast toast = Toast.makeText(mContext, "mobile: " + mobile
                    + ", message: " + newMsg, Toast.LENGTH_LONG);
            toast.show();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void InsertMobileMobilemessageAsync(final String msg) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    String sDate = new Date().toString();
                    RTWS rtWS = new RTWS();
                    rtWS.InsertMobileMobilemessage("", "", "PIN RECEIVED", msg,
                            sDate, String.valueOf(Config.sClientID),
                            SharedPreferenceUtil.getsClientUserName());
                } catch (Exception ex) {

                }
                return true;
            }

        }.execute();
    }

    private void hideSystemUi() {
        // Set flags for hiding status bar and navigation bar
        // mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        // | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        // | View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    /**
     * Shows StatusBar and NavigationBar
     */
    private void showSystemUi() {
        // Reset all flags
        // mViewPager.setSystemUiVisibility(0);
    }

    private AlertDialog SessionExpiredAlertDialog(String mTitle, String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(mTitle)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                new LogoutAsync().execute();
                            }
                        }).create();
    }

    private void mBlueToothBroadcastReceiver() {
        if (mBlueToothReceiver == null) {
            mBlueToothReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        //Device found
                        DLog.e(TAG, "Device found==================>");
                    } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                        //Device is now connected
                        DLog.e(TAG, "Device is now connected==================>");
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        //Done searching
                        DLog.e(TAG, "Done searching==================>");
                    } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                        //Device is about to disconnect
                        DLog.e(TAG, "Device is about to disconnect==================>");
                    } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                        //Device has disconnected
                        DLog.e(TAG, "Device has disconnected==================>");
                        PrintDataService.setPrinterDisconnected();
                    }
                }
            };
            IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
            IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            mContext.registerReceiver(mBlueToothReceiver, filter3);
            DLog.e(TAG, "mBlueToothReceiver =========================");
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_dummy,
                    container, false);
            TextView dummyTextView = (TextView) rootView
                    .findViewById(R.id.section_label);
            dummyTextView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            Bundle bundle = new Bundle();

            switch (position) {
                case 0:
                    fragment = new TopupFragmentUI();
                    break;
                case 1:
                    fragment = new PINFragmentUI();
                    break;

                case 2:
                    fragment = new PayBillFragmentUI();
                    break;

                case 3:
                    fragment = new SignUpFragment();
                    break;
                case 4:
                    fragment = new WhatsHotFragment();
                    break;
                case 5:
                    fragment = new MyAccountFragmentUI();
                    break;
                case 6:
                    fragment = new SettingFragmentUI();
                    break;

            }

            return fragment;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_whatshot).toUpperCase(l);
                case 5:
                    return getString(R.string.title_section5).toUpperCase(l);
                case 6:
                    return getString(R.string.title_section6).toUpperCase(l);


            }
            return null;
        }
    }

    public class MyPasswordTransformationMethod extends
            PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
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

    public class LogoutAsync extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
            pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
            SpannableString ss1 = new SpannableString("Please wait ...");
            ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
            ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                    ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")),
                    0, ss1.length(), 0);
            pd = new ProgressDialog(mContext);
            pd.setTitle(ss1);
            pd.setMessage("Logout ...");
            pd.setCancelable(false);
            pd.show();
        }

        protected Boolean doInBackground(String... arg0) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sClientPassword = "";
            sClientUserName = "";
            SharedPreferenceUtil.editsClientUserName(null);
            SharedPreferenceUtil.editsClientUserName(null);
            return true;
        }

        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            DLog.e(TAG, "logoutEnd ");
            Intent intent = new Intent(mContext, LoginUI.class);
            mContext.startActivity(intent);
            finish();
        }
    }

    private void UpdatePushNotificationID() {
        DLog.e(TAG, "UpdatePushNotificationID ================> "
                + Config.GCM_RegId);
        rtWS.UpdatePushNotificationID(sClientUserName, sClientPassword,
                Config.GCM_RegId);


    }

}

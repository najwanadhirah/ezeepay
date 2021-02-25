package com.rt.qpay99.listener;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.activity.ui.AgentInfoUI;
import com.rt.qpay99.activity.ui.BankInListUI;
import com.rt.qpay99.activity.ui.CheckCustomerTxByDNUI;
import com.rt.qpay99.activity.ui.CheckCustomerTxStatusUI;
import com.rt.qpay99.activity.ui.ReportListUI;
import com.rt.qpay99.activity.ui.ShareCreditUI;
import com.rt.qpay99.object.CheckBalanceResponse;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.NetworkUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAccountGridviewOnItemClickListener implements
        AdapterView.OnItemClickListener {
    private final int CHECK_BALANCE = 0;
    private final int CHECK_TRANSACTION = 1;
    private final int CHECK_REPORT = 2;
    private final int CHECK_BANK_IN = 3;
    private final int CHECK_BANK_IN_LIST = 4;
    private final int CHECK_AGENT_INFO = 5;
    private final int CHECK_DN = 6;
    private final int SHARE_CREDIT=7;
    //private final int CHECK_TOPUP_MOBILE_MONEY = 6;
//    private final int CHECK_UPDATE_FBAPPID = 7;
    private Context mContext;
    private String TAG = this.getClass().getName();
    private FragmentManager fm;
    private RTWS rt = new RTWS();
    private String sClientUserName, sClientPassword, CheckCustomerStatus;

    private TextView tv;
    private EditText ed;
    private AlertDialog ad;
    private RTWS rtWS = new RTWS();
    private ProgressDialog pd;
    private LayoutInflater inflater;
    private Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("ddMM");
    private SimpleDateFormat sdfMinSec = new SimpleDateFormat("mmss");

    private EditText edBankInDate, edBankInTime, edBankInAmount, edBankInCode;
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    public MyAccountGridviewOnItemClickListener(Context mContext,
                                                FragmentManager fm) {
        this.fm = fm;
        this.mContext = mContext;
        sClientUserName = SharedPreferenceUtil.getsClientUserName();
        sClientPassword = SharedPreferenceUtil.getsClientPassword();
        inflater = LayoutInflater.from(mContext);
    }

    private void updateLabel() {
        String myFormat = "ddMM"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edBankInDate.setText(sdf.format(myCalendar.getTime()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = null;

//        if (!SRSApp.gps.isGPSEnabled()) {
//            SRSApp.gps.showSettingsAlert(mContext);
//            return;
//        }

        DLog.e(TAG, "MyAccountGridviewOnItemClickListener");
        switch (position) {
            case CHECK_BALANCE:
                if(NetworkUtil.isOnline(mContext))
                    CheckBalanceAsync();
                else
                    Toast.makeText(mContext,"No internet connection.",Toast.LENGTH_SHORT).show();
                break;
            case CHECK_TRANSACTION:
                intent = new Intent(mContext, CheckCustomerTxStatusUI.class);
                mContext.startActivity(intent);
                break;
            case CHECK_REPORT:
                intent = new Intent(mContext, ReportListUI.class);
                mContext.startActivity(intent);
                break;
            case CHECK_BANK_IN:
                DLog.e(TAG, " Config.sMasterID " + Config.sMasterID);
                if (FunctionUtil.isSet(Config.sMasterID))
                    if (!Config.sMasterID.equalsIgnoreCase(""))
                        // block all E Biz Marketing downline
                        if (!Config.sMasterID.equalsIgnoreCase("382"))
                            getBankInAlertDialog().show();
                break;
            case CHECK_BANK_IN_LIST:
                intent = new Intent(mContext, BankInListUI.class);
                mContext.startActivity(intent);
                break;
            case CHECK_AGENT_INFO:
                intent = new Intent(mContext, AgentInfoUI.class);
                mContext.startActivity(intent);
                break;
            case CHECK_DN:
                intent = new Intent(mContext, CheckCustomerTxByDNUI.class);
                mContext.startActivity(intent);
                break;

//		case CHECK_TOPUP_MOBILE_MONEY:
//			topupMobileMoney().show();
//			break;

            case SHARE_CREDIT:
                intent = new Intent(mContext, ShareCreditUI.class);
                mContext.startActivity(intent);
                break;


        }

    }

    private AlertDialog topupMobileMoney() {
        View view = inflater.inflate(
                R.layout.adapter_layout_mobile_money_topup, null);
        final Spinner spinnerMMBank, spinnerMMGatewayNumber;
        final EditText edMMBankInAmount, edMMAccount, edMMTime;

        edMMBankInAmount = (EditText) view.findViewById(R.id.edMMBankInAmount);
        edMMAccount = (EditText) view.findViewById(R.id.edMMAccount);
        edMMTime = (EditText) view.findViewById(R.id.edMMTime);

        if (Config.isDebug) {
            // edMMBankInAmount.setText("100");
            // edMMAccount.setText("89190710");
            // edMMTime.setText("151020141251");
        }

        spinnerMMGatewayNumber = (Spinner) view
                .findViewById(R.id.spinnerMMGatewayNumber);
        List<String> gatewayList = new ArrayList<String>();
        if (Config.isDebug)
            gatewayList.add("0166572577");
        gatewayList.add("0124283123");
        gatewayList.add("0123676123");
        gatewayList.add("0125682123");
        gatewayList.add("0124702123");
        gatewayList.add("0162282008");
        gatewayList.add("0162613155");
        gatewayList.add("0162262269");
        gatewayList.add("0162107833");
        gatewayList.add("0192336123");
        gatewayList.add("0196536123");
        gatewayList.add("0193336123");
        gatewayList.add("0193866123");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, gatewayList);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMMGatewayNumber.setAdapter(dataAdapter);

        spinnerMMBank = (Spinner) view.findViewById(R.id.spinnerMMBank);
        List<String> MMBankList = new ArrayList<String>();
        MMBankList.add("MBB");
        MMBankList.add("CIMB");
        MMBankList.add("PBB");
        MMBankList.add("RHB");
        MMBankList.add("HLB");
        MMBankList.add("AMBANK");
        MMBankList.add("ALLIANCE");

        ArrayAdapter<String> MMBankListAdapter = new ArrayAdapter<String>(
                mContext, android.R.layout.simple_spinner_item, MMBankList);
        MMBankListAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMMBank.setAdapter(MMBankListAdapter);

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle(R.string.my_acc_topup_mobile_money)
                .setView(view)

                .setCancelable(true)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                final String sBank, sAmount, sGateway, sAccount, sBICode, sMasa;

                                sAmount = edMMBankInAmount.getText().toString();
                                sGateway = spinnerMMGatewayNumber
                                        .getSelectedItem().toString();
                                sBank = spinnerMMBank.getSelectedItem()
                                        .toString();

                                try {
                                    sAccount = edMMAccount.getText().toString();
                                    if (sAccount.length() != 8) {
                                        normalAlertDialog(
                                                "Sila masuk Merchant Code yang betul")
                                                .show();
                                        return;
                                    }

                                } catch (Exception e) {
                                    normalAlertDialog(
                                            "Sila masuk Merchant Code yang betul")
                                            .show();
                                    return;
                                }

                                try {
                                    sBICode = edMMTime.getText().toString();
                                    if (sBICode.length() != 12) {
                                        normalAlertDialog(
                                                "Sila masuk masa bank in dalam format yang betul")
                                                .show();
                                        return;
                                    }

                                } catch (Exception e) {
                                    normalAlertDialog(
                                            "Sila masuk masa bank in dalam format yang betul")
                                            .show();
                                    return;
                                }

                                try {
                                    //int amt = Integer.parseInt(sAmount);
                                    double amt = Double.parseDouble(sAmount);
                                    if (amt < 99) {
                                        normalAlertDialog(
                                                "Minimum Topup Amount RM100.")
                                                .show();
                                        return;
                                    }

                                } catch (Exception e) {
                                    normalAlertDialog(
                                            "Minimum Topup Amount RM100.")
                                            .show();
                                    return;
                                }

                                MMConfirmAlertDialog(
                                        "Transfer RM " + sAmount + "\n to "
                                                + sBank + "\n Merchant Code "
                                                + sAccount + "\n Masa "
                                                + sBICode
                                                + "\n Gateway number - "
                                                + sGateway, sAmount, sBank,
                                        sGateway, sAccount, sBICode).show();

                            }
                        }).setNegativeButton(R.string.title_cancel, null)
                .create();
    }

    private AlertDialog MMConfirmAlertDialog(final String msg,
                                             final String amount, final String bank, final String gateway,
                                             final String merchantCode, final String sBiCode) {

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String accBank, msg;
                                accBank = "";
                                msg = "ACC_BANK#TM#" + merchantCode + "#"
                                        + amount + "#" + sBiCode + "#online";
                                try {
                                    if (bank.equalsIgnoreCase("MBB")) {
                                        accBank = "TMBB";

                                    } else if (bank.equalsIgnoreCase("CIMB")) {
                                        accBank = "TCIMB";

                                    } else if (bank.equalsIgnoreCase("PBB")) {
                                        accBank = "TPBB";

                                    } else if (bank.equalsIgnoreCase("RHB")) {
                                        accBank = "TRHB";

                                    } else if (bank.equalsIgnoreCase("HLB")) {
                                        accBank = "THLB";

                                    } else if (bank.equalsIgnoreCase("AMBANK")) {
                                        accBank = "TAMBANK";

                                    } else if (bank
                                            .equalsIgnoreCase("ALLIANCE")) {
                                        accBank = "TABM";
                                    }

                                    msg = msg.replace("ACC_BANK", accBank);

                                    InsertMobileMobilemessageAsync(msg);

                                } catch (Exception e) {
                                    DLog.e(TAG, e.getMessage());
                                }
                                sendSMS(msg, gateway);
                            }
                        }).create();
    }

    private AlertDialog getBankInAlertDialog() {
        View view = inflater.inflate(R.layout.adapter_layout_bank_in, null);
        final Spinner spinnerBank;
        edBankInAmount = (EditText) view.findViewById(R.id.edBankInAmount);
        edBankInDate = (EditText) view.findViewById(R.id.edBankInDate);
        edBankInTime = (EditText) view.findViewById(R.id.edBankInTime);
        edBankInCode = (EditText) view.findViewById(R.id.edBankInCode);

        if (sClientUserName.substring(0, 2).equalsIgnoreCase("60")) {
            edBankInCode.setText(sClientUserName.substring(1, sClientUserName.length()));
        } else {
            edBankInCode.setText(sClientUserName);
        }

        spinnerBank = (Spinner) view.findViewById(R.id.spinnerBank);
        List<String> list = new ArrayList<String>();
        list.add("MBB");
        list.add("CIMB");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(dataAdapter);

        edBankInAmount = (EditText) view.findViewById(R.id.edBankInAmount);

        edBankInDate.setText(sdf.format(new Date()));
        edBankInDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myCalendar = Calendar.getInstance();
        String hours = String.format("%02d%02d",
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE));
        edBankInTime.setText(hours);

        edBankInTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String output = String.format("%02d%02d",
                                        selectedHour, selectedMinute);
                                // edBankInTime.setText("" + selectedHour + ":"
                                // + selectedMinute);
                                edBankInTime.setText(output);
                            }
                        }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle("Bank In Info")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                final String sBank, sAmount, sDate, sTime, sBiCode;

                                String c = sClientUserName;
                                DLog.e(TAG, "" + sClientUserName);
                                if (sClientUserName.substring(0, 2).equalsIgnoreCase("60")) {
                                    sBiCode = sClientUserName.substring(1, sClientUserName.length());
                                } else {
                                    sBiCode = sClientUserName;
                                }


                                if (spinnerBank.getSelectedItem().toString() == null)
                                    return;
                                if (edBankInAmount.getText() == null)
                                    return;
                                sBank = spinnerBank.getSelectedItem()
                                        .toString();
                                sAmount = edBankInAmount.getText().toString();
                                sDate = edBankInDate.getText().toString();
                                sTime = edBankInTime.getText().toString();

                                try {
                                    Double amt = Double.parseDouble(sAmount);
                                    if (amt < 99) {
                                        normalAlertDialog(
                                                "Minimum Bank In Amount RM100.")
                                                .show();
                                        return;
                                    }

                                } catch (Exception e) {
                                    normalAlertDialog(
                                            "Minimum Bank In Amount RM100.")
                                            .show();
                                    return;
                                }

                                SubmitBankInAsync(sBank, sAmount, sDate, sTime,
                                        sBiCode);


                            }
                        }).setNegativeButton(R.string.title_cancel, null)
                .create();
    }

    private void SubmitBankInAsync(final String sBank, final String sAmount,
                                   final String sDate, final String sTime, final String sBiCode) {
        new AsyncTask<Void, Void, Boolean>() {

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
                pd.setMessage("Submiting ...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                RTWS rt = new RTWS();
                return rt.SubmitBankIn(SharedPreferenceUtil.getsClientUserName(),
                        SharedPreferenceUtil.getsClientPassword(), sBank, sAmount, sDate, sTime,
                        sBiCode);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (pd != null)
                    pd.dismiss();
                if (result) {
                    normalAlertDialog("Transaction Successful.").show();
                } else {
                    normalAlertDialog(
                            "Transaction Failed. Sila Cuba Sebentar Lagi.")
                            .show();
                }

            }
        }.execute();
    }

    private AlertDialog CheckTransactionAlertDialog(final String msg) {
        // AlertDialog.Builder builder = new AlertDialog.Builder(
        // new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        View view = inflater.inflate(R.layout.adapter_layout_input, null);
        inflater.inflate(R.layout.adapter_layout_input, null);
        tv = (TextView) view.findViewById(R.id.tvInput);
        ed = (EditText) view.findViewById(R.id.edInput);
        ed.setInputType(InputType.TYPE_CLASS_PHONE);

        // return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
        // R.style.AlertDialogCustom))
        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle("Check Transaction")
                .setView(view)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).setNegativeButton(R.string.title_cancel, null)
                .create();
    }

    private void CheckBalanceAsync() {
        new AsyncTask<Void, Void, CheckBalanceResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext);
                SpannableString ss1 = new SpannableString("Please wait ...");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                        ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ss1.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#008000")),
                        0, ss1.length(), 0);
                pd = new ProgressDialog(mContext);
                pd.setTitle(ss1);
                pd.setMessage("Check balance ...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected CheckBalanceResponse doInBackground(Void... params) {

                return rt.CheckBalance(
                        SharedPreferenceUtil.getsClientUserName(),
                        SharedPreferenceUtil.getsClientPassword());
            }

            @Override
            protected void onPostExecute(CheckBalanceResponse result) {
                super.onPostExecute(result);
                if (pd != null)
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                if (result.getsResponseStatus() != null) {
                    if (result.getsResponseStatus().equalsIgnoreCase(
                            "QUERY_SUCCESS")) {
                        normalAlertDialog(
                                "Your Account (" + SharedPreferenceUtil.getsClientUserName()
                                        + ") balance is RM "
                                        + result.getdBalance()).show();
                    }
                }
            }
        }.execute();

    }

    private AlertDialog normalAlertDialog(final String msg) {

        return new AlertDialog.Builder(mContext)
                // .setTitle(mName)
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

    private AlertDialog CajRM1AlertDialog(final String msg, final String sBank,
                                          final String sAmount, final String sDate, final String sTime,
                                          final String sBiCode) {

        return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                R.style.AlertDialogCustom))
                // .setTitle(mName)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SubmitBankInAsync(sBank, sAmount, sDate, sTime,
                                        sBiCode);
                            }
                        }).create();
    }

    private void sendSMS(String msg, String gatewayNo) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(gatewayNo, null, msg, null, null);
            // Toast.makeText(getApplicationContext(), "SMS Sent!",
            // Toast.LENGTH_LONG).show();

            final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
                    .setTitle("Sila tunggu...").setMessage("SMS Sent");

            dialog.setPositiveButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                        }
                    });
            final AlertDialog alert = dialog.create();
            alert.show();
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    alert.setMessage("SMS Sent");
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setText(
                            "Please wait " + (millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    alert.dismiss();
                }
            }.start();

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
                    rtWS.InsertMobileMobilemessage("", "", "TOPUP SEND", msg,
                            sDate, String.valueOf(Config.sClientID),
                            SharedPreferenceUtil.getsClientUserName());
                } catch (Exception ex) {

                }
                return true;
            }

        }.execute();
    }


}
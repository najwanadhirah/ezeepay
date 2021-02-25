package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.rt.qpay99.R;
import com.rt.qpay99.object.RTResponse;
import com.rt.qpay99.util.AlertBoxUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;


public class ShareCreditUI extends SRSBaseActivity {


    Button btnSubmit, btnCancel;
    EditText edName, edMobileNo, edAmount;

    ImageButton btnContact;

    String shareName, shareMobile, shareAmount;

    String msisdn, mAmount, mName;
    static int PICK_CONTACT_REQUEST = 101;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_share_credit_ui;
    }

    @Override
    protected void activityCreated() {
        mContext = this;

        edName = (EditText) findViewById(R.id.edName);
        edMobileNo = (EditText) findViewById(R.id.edMobileNo);
        edAmount = (EditText) findViewById(R.id.edAmount);

        extras = getIntent().getExtras();
        if(extras!=null){
            shareName = extras.getString("shareName");
            shareMobile = extras.getString("shareMobile");
            shareAmount = extras.getString("shareAmount");

            if (!TextUtils.isEmpty(shareName)) {
                edName.setText(shareName);
            }

            if (!TextUtils.isEmpty(shareMobile)) {
                edMobileNo.setText(shareMobile);
            }

            if (!TextUtils.isEmpty(shareAmount)) {
                //edName.setText(shareAmount);
            }
        }


        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Share Credit");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
        btnContact = (ImageButton) findViewById(R.id.btnContact);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, PICK_CONTACT_REQUEST);
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msisdn = edMobileNo.getText().toString();
                mAmount = edAmount.getText().toString();
                mName = edName.getText().toString();

                if (TextUtils.isEmpty(edName.getText())) {
                    edName.requestFocus();
                    edName.setError("Please enter valid Name");
                    return;
                }

                DLog.e(TAG, "name " + mAmount);
                if (TextUtils.isEmpty(edAmount.getText())) {
                    edAmount.requestFocus();
                    edAmount.setError("Please enter valid Amount");
                    return;
                }

                if (TextUtils.isEmpty(edMobileNo.getText())) {
                    edAmount.requestFocus();
                    edAmount.setError("Please enter valid Mobile No.");
                    return;
                } else {
                    if (msisdn.length() < 10) {
                        edAmount.requestFocus();
                        edAmount.setError("Please enter valid Mobile No.");
                        return;
                    }
                }
                ShareCreditAsync();
            }
        });
    }

    @Override
    protected void initData() {

    }


    private RTWS rtWS = new RTWS();
    String sTs, sEncKey;
    ProgressDialog pd;

    private void ShareCreditAsync() {
        new AsyncTask<Void, Void, RTResponse>() {
            @Override
            protected RTResponse doInBackground(Void... params) {
                sTs = FunctionUtil.getsDNReceivedID();
                sEncKey = FunctionUtil.getsEncK(SharedPreferenceUtil.getsClientUserName() + "RichTech6318" + sTs);
                return rtWS.ShareCredit(SharedPreferenceUtil.getsClientUserName(), SharedPreferenceUtil.getsClientPassword(),
                        msisdn, mAmount, sTs, sEncKey);

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext);
                SpannableString ss1 = new SpannableString("SRS Mobile");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
                        ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ss1.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#008000")),
                        0, ss1.length(), 0);
                pd = new ProgressDialog(mContext);
                pd.setMessage("Connecting database ... ");
                pd.setTitle(ss1);
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected void onPostExecute(RTResponse result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (pd != null) {
                        if (pd.isShowing())
                            pd.dismiss();
                    }
                    AlertBoxUtil mAlertBoxUtil = new AlertBoxUtil(mContext, new AlertDialogOnclickListender());
                    edName.setText("");
                    edMobileNo.setText("");
                    edAmount.setText("");
                    mAlertBoxUtil.showAlertDialog("QPAY99", result.getsResponseMessage(), "OK", "");

                }

            }
        }.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    Cursor cursor = null;
                    try {
                        String phoneNo = "";
                        String name = "";

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        phoneNo = cursor.getString(phoneIndex);
                        name = cursor.getString(nameIndex);
                        DLog.e("Name and Contact number is", name + "," + phoneNo);
                        DLog.e("Name and Contact number is", name + "," + phoneNo);
                        String newPhone = phoneNo.replace(" ","");
                        newPhone = newPhone.replace("+","");
                        edMobileNo.setText(newPhone.replace("-",""));
                        edName.setText(name);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            DLog.e("Failed", "Not able to pick contact");
        }
    }


}

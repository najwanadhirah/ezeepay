package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rt.qpay99.Config;
import com.rt.qpay99.Constants;
import com.rt.qpay99.R;
import com.rt.qpay99.object.AddressObj;
import com.rt.qpay99.object.RequestInputResponse;
import com.rt.qpay99.util.AlertBoxUtil;
import com.rt.qpay99.util.AnimationUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentProcessUI extends SRSBaseActivity {

    ImageView imgStep1, imgStep2, imgStep3, imgRemovedAdd, imgCProduct;
    View stepline1, stepline2;

    TextView tvAddUserName, tvAddMobileNumber, tvAddAddress, tvAddAddress1, tvAddAddress2, tvEmaillAddress;

    LinearLayout lladdress, lladdaddress;

    String mFirebaseDBId;

    ViewFlipper productViewFlipper;

    Button btnNext, btnCancel, btnProcessCheckOut, btnCheckOutCancel, btnBackTOHome;
    AnimationUtil mAnimationUtil;
    BottomSheetDialog mBottomSheetDialog;
    AddressObj address = new AddressObj();

    TextView tvCPhoneModal1, tvcPhoneColor1, tvCPhoneGrade1, tvCMemory1, tvCPrice1, tvTotalPrice1;
    ImageView imgCProduct1;
    Button btnProcessCheckOut1, btnCheckOutCancel1;

    String imgURL;

    TextView tvCPhoneModal, tvcPhoneColor, tvCPhoneGrade, tvCMemory, tvTotalPrice, tvSuccessMsg,
            tvCPrice, tvCUserName, tvCMobileNumber, tvCAddress, tvCAddress1, tvCAddress2, tvCEmaillAddress;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_payment_process_ui;
    }

    @Override
    protected void activityCreated() {
        DLog.e(TAG, "activityCreated");
        getSupportActionBar().setTitle("Delivery Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgCProduct = findViewById(R.id.imgCProduct);
        tvSuccessMsg = findViewById(R.id.tvSuccessMsg);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvCPhoneModal = findViewById(R.id.tvCPhoneModal);
        tvcPhoneColor = findViewById(R.id.tvcPhoneColor);
        tvCPhoneGrade = findViewById(R.id.tvCPhoneGrade);
        tvCMemory = findViewById(R.id.tvCMemory);
        tvTotalPrice1 = findViewById(R.id.tvTotalPrice1);


        imgCProduct1 = findViewById(R.id.imgCProduct1);
        tvCPrice1 = findViewById(R.id.tvCPrice1);
        tvCPhoneModal1 = findViewById(R.id.tvCPhoneModal1);
        tvcPhoneColor1 = findViewById(R.id.tvcPhoneColor1);
        tvCPhoneGrade1 = findViewById(R.id.tvCPhoneGrade1);
        tvCMemory1 = findViewById(R.id.tvCMemory1);
        btnProcessCheckOut1 = findViewById(R.id.btnProcessCheckOut1);
        btnCheckOutCancel1 = findViewById(R.id.btnCheckOutCancel1);

        tvCPrice = findViewById(R.id.tvCPrice);
        tvCUserName = findViewById(R.id.tvCUserName);
        tvCMobileNumber = findViewById(R.id.tvCMobileNumber);
        tvCAddress = findViewById(R.id.tvCAddress);
        tvCAddress1 = findViewById(R.id.tvCAddress1);
        tvCAddress2 = findViewById(R.id.tvCAddress2);
        tvCEmaillAddress = findViewById(R.id.tvCEmaillAddress);


        lladdress = findViewById(R.id.lladdress);
        lladdaddress = findViewById(R.id.lladdaddress);

        imgStep1 = findViewById(R.id.imgStep1);
        imgStep2 = findViewById(R.id.imgStep2);
        imgStep3 = findViewById(R.id.imgStep3);
        imgRemovedAdd = findViewById(R.id.imgRemovedAdd);

        tvAddUserName = findViewById(R.id.tvAddUserName);
        tvAddMobileNumber = findViewById(R.id.tvAddMobileNumber);
        tvAddAddress = findViewById(R.id.tvAddAddress);
        tvAddAddress1 = findViewById(R.id.tvAddAddress1);
        tvAddAddress2 = findViewById(R.id.tvAddAddress2);
        tvEmaillAddress = findViewById(R.id.tvEmaillAddress);

        stepline1 = findViewById(R.id.stepline1);
        stepline2 = findViewById(R.id.stepline2);

        productViewFlipper = findViewById(R.id.productViewFlipper);

        btnNext = findViewById(R.id.btnNext);
        btnCancel =  findViewById(R.id.btnCancel);
        btnProcessCheckOut = findViewById(R.id.btnProcessCheckOut);
        btnCheckOutCancel = findViewById(R.id.btnCheckOutCancel);
        btnBackTOHome =  findViewById(R.id.btnBackTOHome);

        mAnimationUtil = new AnimationUtil(productViewFlipper);


        addAddress();
    }

    int imgId;
    String phoneModal = "", dProductPrice = "", phoneMemory = "", phoneGrade = "", phoneColor = "";
    String sClientTxID = UUID.randomUUID().toString();

    String mProduct;

    void checkHeaderText() {
        int displayId = productViewFlipper.getDisplayedChild();
        DLog.e(TAG, "--------------- displayId " + displayId);
        if (displayId == 0) {
            getSupportActionBar().setTitle("Delivery Address");
        }

        if (displayId == 1) {
            getSupportActionBar().setTitle("Shipping");
        }

        if (displayId == 2) {
            getSupportActionBar().setTitle("Confirmation");
        }

        if (displayId == 3) {
            getSupportActionBar().setTitle("Payment Confirmation");
        }

    }


    void addAddress() {


//        address.setName("KIM WEI");
//        address.setMobileNumber("+60126112184");
//        address.setPasscode("52100");
//        address.setState("Wilayah Persekutuan");
//        address.setAddress1("77 Jalan 18");
//        address.setAddress2("Desa Jaya, Kepong");

        lladdress.setVisibility(View.GONE);
        List<AddressObj> objs = new ArrayList<AddressObj>();
        addressList = SharedPreferenceUtil.getCUST_ADDRESS_LIST_PREFERENCE();
        if (!TextUtils.isEmpty(address.getName())) {
            lladdress.setVisibility(View.VISIBLE);
            if (addressList == null) {
                objs.add(address);
                SharedPreferenceUtil.editCUST_ADDRESS_LIST_PREFERENCE(objs);
            } else if (!addressList.contains(address)) {
                objs.add(address);
                SharedPreferenceUtil.editCUST_ADDRESS_LIST_PREFERENCE(objs);
            }
        }


//        List<AddressObj> stored = SharedPreferenceUtil.getCUST_ADDRESS_LIST_PREFERENCE();
//        if ((stored != null) && (stored.size()) > 0) {
//            if(!TextUtils.isEmpty(address.getName())){
//                lladdaddress.setVisibility(View.VISIBLE);
//                if (!stored.contains(address)) {
//                    objs.add(address);
//                    SharedPreferenceUtil.editCUST_ADDRESS_LIST_PREFERENCE(objs);
//                }
//            }
//
//        } else {
//            lladdaddress.setVisibility(View.INVISIBLE);
//            objs.add(address);
//            SharedPreferenceUtil.editCUST_ADDRESS_LIST_PREFERENCE(objs);
//        }

        addressList = SharedPreferenceUtil.getCUST_ADDRESS_LIST_PREFERENCE();
        if (addressList != null) {
            if (addressList.size() > 0) {
                lladdress.setVisibility(View.VISIBLE);
                tvAddUserName.setText(addressList.get(0).getName());
                tvAddMobileNumber.setText(addressList.get(0).getMobileNumber());
                tvAddAddress.setText(addressList.get(0).getAddress1());
                tvAddAddress1.setText(addressList.get(0).getAddress2());
                tvAddAddress2.setText(addressList.get(0).getPasscode() + " " + addressList.get(0).getState());
                tvEmaillAddress.setText(addressList.get(0).getEmailAddess());
                return;
            }
        }


    }

    List<AddressObj> addressList = new ArrayList<AddressObj>();

    @Override
    protected void initData() {
        DLog.e(TAG, "initData");

        btnBackTOHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgRemovedAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAddressAlertDialog("Remove the address?").show();
            }
        });

        checkHeaderText();

        Bundle extras = getIntent().getExtras();
        mProduct = extras.getString("mProduct");
        phoneMemory = "NA";
        phoneGrade = "NA";
        phoneColor = "NA";

        mFirebaseDBId =  extras.getString("mFirebaseDBId");
        mName =  extras.getString("mName");


        DLog.e(TAG,"mName " + mName);
        DLog.e(TAG,"mFirebaseDBId " + mFirebaseDBId);

        if (mProduct.equalsIgnoreCase(Constants.CAT_PHONE)) {
            phoneModal = extras.getString("phoneModal");
            dProductPrice = extras.getString("dProductPrice");
            phoneMemory = extras.getString("phoneMemory");
            phoneGrade = extras.getString("phoneGrade");
            phoneColor = extras.getString("phoneColor");

        } else if (mProduct.equalsIgnoreCase(Constants.CAT_POWERBANK)) {
            phoneModal = extras.getString("phoneModal");
            dProductPrice = extras.getString("dProductPrice");
            phoneColor = extras.getString("phoneColor");
            imgId = extras.getInt("imgId");
            imgURL = extras.getString("imgURL");
        } else if (mProduct.equalsIgnoreCase(Constants.CAT_PRINTER)) {
            phoneModal = extras.getString("phoneModal");
            dProductPrice = extras.getString("dProductPrice");
            imgId = extras.getInt("imgId");
            imgURL = extras.getString("imgURL");
        } else {
            phoneModal = extras.getString("phoneModal");
            dProductPrice = extras.getString("dProductPrice");
            imgId = extras.getInt("imgId");
            imgURL = extras.getString("imgURL");
        }


//        phoneModal = "iPhone 6 Plus";
//        dProductPrice = "1610";
//        phoneMemory = "64Gb";
//        phoneGrade = "A";
//        phoneColor = "Silver";

        DLog.e(TAG, "phoneModal " + phoneModal);
        DLog.e(TAG, "dProductPrice " + dProductPrice);
        DLog.e(TAG, "phoneMemory " + phoneMemory);
        DLog.e(TAG, "phoneGrade " + phoneGrade);
        DLog.e(TAG, "phoneColor " + phoneColor);

        btnCheckOutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitAlertDialog(
                        "Do you want to cancel this transaction?").show();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitAlertDialog(
                        "Do you want to cancel this transaction?").show();
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepline1.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
//                imgStep2.setImageDrawable(getResources().getDrawable(R.drawable.button_step2_active));


                tvCPhoneModal.setText(phoneModal);
                tvcPhoneColor.setText(phoneColor);
                tvCMemory.setText(phoneMemory);
                tvCPrice.setText("RM  " + dProductPrice + ".00");
                tvCPhoneGrade.setText(phoneGrade);
                tvTotalPrice.setText("RM  " + dProductPrice + ".00");




                DLog.e(TAG, "-------------> mProduct" + mProduct);
                if (mProduct.equalsIgnoreCase(Constants.CAT_PHONE)) {
                    imgCProduct1.setImageDrawable(getResources().getDrawable(ImageUtil.setPhoneImages(phoneModal, phoneColor, mContext)));
                    imgCProduct.setImageDrawable(getResources().getDrawable(ImageUtil.setPhoneImages(phoneModal, phoneColor, mContext)));
                } else if (mProduct.equalsIgnoreCase(Constants.CAT_POWERBANK)) {

                    ImageUtil.setLocalImage(mName,mFirebaseDBId, imgCProduct,mContext);
                    ImageUtil.setLocalImage(mName,mFirebaseDBId, imgCProduct1,mContext);


                } else if (mProduct.equalsIgnoreCase(Constants.CAT_PRINTER)) {
                    ImageUtil.setLocalImage(mName,mFirebaseDBId, imgCProduct,mContext);
                    ImageUtil.setLocalImage(mName,mFirebaseDBId, imgCProduct1,mContext);

                } else {
                    ImageUtil.setLocalImage(mName,mFirebaseDBId, imgCProduct,mContext);
                    ImageUtil.setLocalImage(mName,mFirebaseDBId, imgCProduct1,mContext);
//                    DLog.e(TAG, "------------->" + imgId);
//                    DLog.e(TAG, "------------->" + imgURL);
//                    Picasso.get()
//                            .load(imgURL)
//                            .fit()
//                            .into(imgCProduct1);
//                    imgCProduct1.setAdjustViewBounds(true);
//                    Picasso.get()
//                            .load(imgURL)
//                            .fit()
//                            .into(imgCProduct);
//                    imgCProduct.setAdjustViewBounds(true);
                }

                tvTotalPrice1.setText("RM  " + dProductPrice + ".00");

                tvCPhoneModal1.setText(phoneModal);
                tvcPhoneColor1.setText(phoneColor);
                tvCMemory1.setText(phoneMemory);
                tvCPrice1.setText("RM  " + dProductPrice + ".00");
                tvCPhoneGrade1.setText(phoneGrade);

                if (addressList != null) {
                    if (addressList.size() > 0) {
                        tvCUserName.setText(addressList.get(0).getName());
                        tvCMobileNumber.setText(addressList.get(0).getMobileNumber());
                        tvCAddress.setText(addressList.get(0).getAddress1());
                        tvCAddress1.setText(addressList.get(0).getAddress2());
                        tvCAddress2.setText(addressList.get(0).getPasscode() + " " + addressList.get(0).getState());
                        tvCEmaillAddress.setText(addressList.get(0).getEmailAddess());
                        mAnimationUtil.showNext();
                        checkHeaderText();
                    }
                }


            }
        });


        btnProcessCheckOut1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepline1.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
                stepline2.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
//                imgStep2.setImageDrawable(getResources().getDrawable(R.drawable.button_step2_active));
//                imgStep3.setImageDrawable(getResources().getDrawable(R.drawable.button_step4_active));
                mAnimationUtil.showNext();
                checkHeaderText();
            }
        });

        btnCheckOutCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitAlertDialog(
                        "Do you want to cancel this transaction?").show();
            }
        });


        btnProcessCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBuyAsync();

            }
        });


        lladdaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog = new BottomSheetDialog(mContext);
                mBottomSheetDialog.setContentView(R.layout.view_bottomsheetdialog_add_address);
                Button btnBSCancel = (Button) mBottomSheetDialog.findViewById(R.id.btnBSCancel);
                Button btnBSAddAddress = (Button) mBottomSheetDialog.findViewById(R.id.btnBSAddAddress);


                final TextView edName, edAddress1, edAddress2, edZipCode, edState, edPhonenumber, edEmail;
                edName = (TextView) mBottomSheetDialog.findViewById(R.id.edName);
                edAddress1 = (TextView) mBottomSheetDialog.findViewById(R.id.edAddress1);
                edAddress2 = (TextView) mBottomSheetDialog.findViewById(R.id.edAddress2);
                edZipCode = (TextView) mBottomSheetDialog.findViewById(R.id.edZipCode);
                edState = (TextView) mBottomSheetDialog.findViewById(R.id.edState);
                edPhonenumber = (TextView) mBottomSheetDialog.findViewById(R.id.edPhonenumber);
                edEmail = (TextView) mBottomSheetDialog.findViewById(R.id.edEmail);

                if (Config.isDebug) {
                    edName.setText("TEST USER");
                    edAddress1.setText("818 8th floor");
                    edAddress2.setText("kelana center point");
                    edZipCode.setText("47301");
                    edState.setText("Selangor");
                    edPhonenumber.setText("0123456789");
                    edEmail.setText("lkimwei.ch@Gmail.com");


                }


                btnBSCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });

                btnBSAddAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(edName.getText())) {
                            edName.requestFocus();
                            edName.setError("Please insert name");
                            return;
                        }

                        if (TextUtils.isEmpty(edAddress1.getText())) {
                            edAddress1.requestFocus();
                            edAddress1.setError("Please insert Address1");
                            return;
                        }

                        if (TextUtils.isEmpty(edAddress2.getText())) {
                            edAddress2.requestFocus();
                            edAddress2.setError("Please insert Address2");
                            return;
                        }

                        if (TextUtils.isEmpty(edZipCode.getText())) {
                            edZipCode.requestFocus();
                            edZipCode.setError("Please insert ZipCode");
                            return;
                        }

                        if (TextUtils.isEmpty(edState.getText())) {
                            edState.requestFocus();
                            edState.setError("Please insert State");
                            return;
                        }

                        if (TextUtils.isEmpty(edPhonenumber.getText())) {
                            edPhonenumber.requestFocus();
                            edPhonenumber.setError("Please insert Phone Number");
                        } else {
                            if (edPhonenumber.getText().length() < 10) {
                                edPhonenumber.requestFocus();
                                edPhonenumber.setError("Invalid Mobile Length");
                            }
                        }

                        if (TextUtils.isEmpty(edEmail.getText())) {
                            edEmail.requestFocus();
                            edEmail.setError("Please insert Email Address");
                            return;
                        }

                        try {
                            address.setName(edName.getText().toString());
                            address.setAddress1(edAddress1.getText().toString());
                            address.setAddress2(edAddress2.getText().toString());
                            address.setPasscode(edZipCode.getText().toString());
                            address.setState(edState.getText().toString());
                            address.setEmailAddess(edEmail.getText().toString());
                            address.setMobileNumber(edPhonenumber.getText().toString());
                            addAddress();
                            mBottomSheetDialog.dismiss();
                        } catch (Exception e) {
                            DLog.e(TAG, "" + e.getMessage());
                        }

                    }
                });


                mBottomSheetDialog.setCancelable(true);
                mBottomSheetDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                DLog.e(TAG, "onOptionsItemSelected ");
                int displayedChild = productViewFlipper.getDisplayedChild();
                if (productViewFlipper != null) {
                    if (displayedChild == 1) {
                        stepline1.setBackgroundColor(mContext.getResources().getColor(R.color.DarkGray));
                        stepline2.setBackgroundColor(mContext.getResources().getColor(R.color.DarkGray));
//                        imgStep2.setImageDrawable(getResources().getDrawable(R.drawable.button_step2_default));
//                        imgStep3.setImageDrawable(getResources().getDrawable(R.drawable.button_step4_default));
                    }
                }

                if (displayedChild == 2) {
                    stepline1.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
                    stepline2.setBackgroundColor(mContext.getResources().getColor(R.color.DarkGray));
//                    imgStep2.setImageDrawable(getResources().getDrawable(R.drawable.button_step2_active));
//                    imgStep3.setImageDrawable(getResources().getDrawable(R.drawable.button_step4_default));
                }

                if (displayedChild == 3) {
                    stepline1.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
                    stepline2.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
//                    imgStep2.setImageDrawable(getResources().getDrawable(R.drawable.button_step2_active));
//                    imgStep3.setImageDrawable(getResources().getDrawable(R.drawable.button_step4_active));
                }

                if (displayedChild == 3) {
                    finish();
                }

                if (productViewFlipper.getDisplayedChild() > 0) {
                    productViewFlipper.showPrevious();

                } else {
                    onBackPressed();
//                    NavUtils.navigateUpFromSameTask(this);
                }
                checkHeaderText();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private AlertDialog DeleteAddressAlertDialog(String msg) {

        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle("SRS Mobile")
                .setMessage(msg)
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
                                tvAddUserName.setText("");
                                tvAddMobileNumber.setText("");
                                tvAddAddress.setText("");
                                tvAddAddress1.setText("");
                                tvAddAddress2.setText("");
                                lladdress.setVisibility(View.GONE);
                                SharedPreferenceUtil.editCUST_ADDRESS_LIST_PREFERENCE(null);
                            }
                        }).create();
    }

    private String sCustomerMobileNumber, sProductID, strCat, sProductName, sRemark, sTs, sEncKey,
            sCustomerAccountNumber, BILL, Description, sOtherParameter, CATEGORY, BillNo;
    ProgressDialog pd;

    private void RequestBuyAsync() {
        new AsyncTask<Void, Void, RequestInputResponse>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(mContext);
                SpannableString ss1 = new SpannableString("Please wait ...");
                ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
                ss1.setSpan(new StyleSpan(Typeface.BOLD), 0,
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
            protected RequestInputResponse doInBackground(Void... params) {
                RTWS rt = new RTWS();
                sTs = FunctionUtil.getsDNReceivedID();
                String msisdn = SharedPreferenceUtil.getsClientUserName().substring(1, SharedPreferenceUtil.getsClientUserName().length());
                sEncKey = FunctionUtil.getsEncK(SharedPreferenceUtil.getsClientUserName() + "RichTech6318" + sTs);
                sCustomerMobileNumber = msisdn;
                sCustomerAccountNumber = msisdn;

                sProductName = FunctionUtil.getPhoneBrand(phoneModal);
                sRemark = addressList.get(0).getName() + "," + addressList.get(0).getAddress1() + "," + addressList.get(0).getAddress2() + "," + addressList.get(0).getPasscode() + " " + addressList.get(0).getState() + "," +
                        addressList.get(0).getMobileNumber() + "," + addressList.get(0).getEmailAddess();

                sOtherParameter = phoneModal + "," + phoneColor + ",Grade " + phoneGrade + "," + phoneMemory;
                if (mProduct.equalsIgnoreCase(Constants.CAT_POWERBANK)) {
                    sOtherParameter = phoneColor;
                }

                //sClientTxID = FunctionUtil.getStrCurrentDateTime();


                DLog.e(TAG, "sCustomerAccountNumber " + addressList.get(0).getMobileNumber());
                DLog.e(TAG, "sCustomerMobileNumber " + addressList.get(0).getMobileNumber());
                DLog.e(TAG, "dProductPrice" + dProductPrice);
                DLog.e(TAG, "sProductName " + sProductName);
                DLog.e(TAG, "sRemark " + sRemark);
                DLog.e(TAG, "sClientTxID " + sClientTxID);
                DLog.e(TAG, "sOtherParameter " + sOtherParameter);
                DLog.e(TAG, "sTs " + sTs);
                DLog.e(TAG, "sEncKey " + sEncKey);

                RequestInputResponse result = rt.RequestBuy(
                        sCustomerAccountNumber, sCustomerMobileNumber,
                        dProductPrice, sProductName, sRemark, sClientTxID,
                        sOtherParameter, sTs, sEncKey, "", "");
                return result;

            }

            @Override
            protected void onPostExecute(RequestInputResponse result) {
                super.onPostExecute(result);
                pd.dismiss();
                if (result != null) {
                    if (Config.WS_SUBMIT_SUCCESS.equalsIgnoreCase(result
                            .getsResponseStatus())) {
                        DLog.e(TAG, result.getsResponseStatus());
                        stepline2.setBackgroundColor(mContext.getResources().getColor(R.color.primary));
//                        imgStep3.setImageDrawable(getResources().getDrawable(R.drawable.button_step4_active));
                        mAnimationUtil.showNext();
                        tvSuccessMsg.setText("You've sucessfully purchased " + phoneModal);
                        return;
                    } else {
                        try {
                            AlertBoxUtil mAlertBoxUtil = new AlertBoxUtil(mContext, new AlertDialogOnclickListender());
                            mAlertBoxUtil.showAlertDialog(getString(R.string.app_name), result.getsResponseStatus(), "", "OK");
                            return;
                        } catch (Exception e) {
                            DLog.e(TAG, "Err " + e.getMessage());
                        }
                    }
                }

                AlertBoxUtil mAlertBoxUtil = new AlertBoxUtil(mContext, new AlertDialogOnclickListender());
                mAlertBoxUtil.showAlertDialog(getString(R.string.app_name), "Service Error. Please try again later.", "", "OK");


            }
        }.execute();

    }

    void endTask() {
        this.finish();

    }

    private class AlertDialogOnclickListender implements AlertBoxUtil.RTAlertDialogInterface {


        @Override
        public void BUTTON_POSITIVE_CLICK() {
            endTask();
        }

        @Override
        public void BUTTON_NEGATIVE_CLICK() {

        }

        @Override
        public void BUTTON_NEUTRAL_CLICK() {

        }
    }

    private AlertDialog ExitAlertDialog(String msg) {
        return new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom))
                .setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setNegativeButton(R.string.title_cancel, null)
                .setPositiveButton(R.string.title_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        }).create();
    }

}

package com.rt.qpay99.activity.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.object.CustomerTxStatusInfo;
import com.rt.qpay99.object.RequestReloadPinObject;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareViaUI extends FragmentActivity {

    Bitmap b;
    private com.rt.qpay99.CustomView.ZoomableImageView imgDraw;
    private String TAG = this.getClass().getName();
    private Context mContext;
    private CustomerTxStatusInfo mCustomerTxStatusInfo = new CustomerTxStatusInfo();
    private RequestReloadPinObject mmRequestReloadPinObject = new RequestReloadPinObject();
    private String mName = "";
    private Button btnShare;
    private String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_via_ui);
        mContext = this;

        imgDraw =  findViewById(R.id.imgDraw);
        mCustomerTxStatusInfo = SharedPreferenceUtil.getShareVia();
        mmRequestReloadPinObject = SharedPreferenceUtil.getShareViaRelaodPin();
        mName = getIntent().getStringExtra("mName");
        mType = getIntent().getStringExtra("mType");
        drawImages();

        btnShare =  findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmap(b, "Share");
            }
        });

        Answers.getInstance().logCustom(new CustomEvent("SHARE VIA")
                .putCustomAttribute("mName", mName)
        );


    }

    private void shareBitmap(Bitmap bitmap, String fileName) {
        try {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            DLog.e(TAG,"SDK_INT " + Build.VERSION.SDK_INT);
            DLog.e(TAG,"LOLLIPOP_MR1 " + Build.VERSION_CODES.LOLLIPOP_MR1);

            File file = new File(mContext.getCacheDir(), "images");
            file.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(file + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            DLog.e(TAG,"FileProvider2");
            File imagePath = new File(mContext.getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(mContext, "rob.payezqp.com.fileprovider", newFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            }

        } catch (Exception e) {
            DLog.e(TAG,"" + e.getMessage());
            e.printStackTrace();
        }

    }

//    private void shareBitmap(Bitmap bitmap, String fileName) {
//        try {
//            File file = new File(this.getCacheDir(), fileName + ".png");
//            FileOutputStream fOut = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//            fOut.flush();
//            fOut.close();
//            file.setReadable(true, false);
//            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            intent.setType("image/png");
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    void drawImages() {
        int startX = 20;
        int startY = 120;
        int mWidth = 480;

        b = Bitmap.createBitmap(mWidth, 800, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        c.drawColor(Color.parseColor("#f1f1f1"), PorterDuff.Mode.OVERLAY);


        Paint paint = new Paint();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.ic_receipt_qpay, options);
        Float centreX = (float) (mWidth - background.getWidth()) / 2;
//        c.drawBitmap(background, centreX, 10, paint);

        //startY = startY + background.getHeight()+30;

        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        c.drawText(mContext.getResources().getString(R.string.app_name) + " Coupon", startX, startY, paint);
        paint.setTextSize(30);
        c.drawText(SharedPreferenceUtil.getMerchantName(), startX, startY + 40, paint);
        paint.setTextSize(20);

        if (FunctionUtil.isSet(mType))
            c.drawText("** " + mType + " **", startX, startY + 70, paint);
        else
            c.drawText("** Duplicate Copies **", startX, startY + 70, paint);


        paint.setTextAlign(Paint.Align.LEFT);
        c.drawText("Mechant ID :", startX, startY + 115, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        c.drawText(SharedPreferenceUtil.getsClientUserName(), mWidth - startX, startY + 115, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        Time now = new Time();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        c.drawText("Transaction Date :", startX, startY + 135, paint);
        paint.setTextAlign(Paint.Align.RIGHT);

        try{
            if (FunctionUtil.isSet(mType))
                c.drawText(currentDateandTime, mWidth - startX, startY + 135, paint);
            else
                c.drawText(mCustomerTxStatusInfo.getDateTime(), mWidth - startX, startY + 135, paint);
        }catch (Exception e){

        }


        if (mName.indexOf("PIN") > -1) {
            paint.setTextAlign(Paint.Align.LEFT);
            c.drawText("Serial No.:", startX, startY + 155, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            c.drawText(mmRequestReloadPinObject.getsSerialNumber(), mWidth - startX, startY + 155, paint);
            paint.setTextAlign(Paint.Align.LEFT);
//            c.drawText("Transaction No. :", startX, startY + 175, paint);
//            paint.setTextAlign(Paint.Align.RIGHT);
//            if (FunctionUtil.isSet(mType))
//                c.drawText(mmRequestReloadPinObject.getsBatchID(), mWidth - startX, startY + 175, paint);
//            else
//                c.drawText(mCustomerTxStatusInfo.getDN(), mWidth - startX, startY + 175, paint);
//            paint.setTextAlign(Paint.Align.LEFT);


        } else {
            paint.setTextAlign(Paint.Align.LEFT);
            c.drawText("Status :", startX, startY + 155, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            c.drawText(mCustomerTxStatusInfo.getStatus(), mWidth - startX, startY + 155, paint);
            //c.drawText(mCustomerTxStatusInfo.getCode(), mWidth - startX, startY + 175, paint);
        }

        if (mName.indexOf("PIN") > -1) {
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.CENTER);  // centers horizontally

            c.drawText(mName.replace("PIN", "") + " RM" + mmRequestReloadPinObject.getsAmount(), mWidth / 2, startY + 245, paint);

            paint.setTextAlign(Paint.Align.LEFT);  // centers horizontally
            c.drawText("PIN :", startX, startY + 285, paint);
            c.drawText(FunctionUtil.getPINFormat(mmRequestReloadPinObject.getsReloadPin()), startX, startY + 325, paint);
            paint.setTextSize(20);
            c.drawText("Pin expired Date : " + mmRequestReloadPinObject.getsExpiryDate(), startX, startY + 345, paint);
            c.drawText("Topup Instruction :", startX, startY + 365, paint);
            try{
                c.drawText(mmRequestReloadPinObject.getsDescription(), startX, startY + 385, paint);
            }catch (Exception e){

            }

        } else {
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(40);
            c.drawText(mCustomerTxStatusInfo.getsReloadMSISDN(), mWidth / 2, startY + 245, paint);
            c.drawText(mName + " RM" + mCustomerTxStatusInfo.getAmount(), mWidth / 2, startY + 285, paint);

        }


        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        if (mName.equalsIgnoreCase("CELCOM") || mName.equalsIgnoreCase("CELCOMPIN")) {
            c.drawText("CELCOM Careline : +603 36308888 atau 1111", mWidth / 2, startY + 430, paint);
        }
        if (mName.equalsIgnoreCase("MAXIS") || mName.equalsIgnoreCase("MAXISPIN")) {
            c.drawText("MAXIS Careline : 1300-820-120 atau 123", mWidth / 2, startY + 430, paint);
        }

        if (mName.equalsIgnoreCase("DIGI") || mName.equalsIgnoreCase("DIGIPIN")) {

            c.drawText("DiGi Careline : +6016 2211 800", mWidth / 2, startY + 430, paint);
        }

        if (mName.equalsIgnoreCase("UMOBILE") || mName.equalsIgnoreCase("UMOBILEPIN")) {
            c.drawText("UMOBILE Careline : +6018 388 1318", mWidth / 2, startY + 430, paint);
        }

        if (mName.equalsIgnoreCase("TUNETALK") || mName.equalsIgnoreCase("TUNETALKPIN")) {
            c.drawText("TUNETALK Careline : +603-79490000 atau 13100", mWidth / 2, startY + 430, paint);
        }

        if (mName.equalsIgnoreCase("XOX") || mName.equalsIgnoreCase("XOXPIN")) {
            c.drawText("XOX Careline : +603-7962 8000 atau 12273", mWidth / 2, startY + 430, paint);
        }


        c.drawText("Kupon Prepaid yg dibeli TIDAK boleh tukar ganti.", mWidth / 2, startY + 455, paint);
        c.drawText("--------------------------------------------------------------", mWidth / 2, startY + 490, paint);

//        c.drawText("QPay99 Careline : " + Config.Custotmer_care, mWidth / 2, startY + 525, paint);
        //canvas.drawText(text, width / 2, (height - paint.ascent()) / 2, paint);
        c.drawText("9AM - 6PM (Monday - Friday)", mWidth / 2, startY + 550, paint);
        c.drawText("Thank You", mWidth / 2, startY + 575, paint);
//        c.drawText("www.QPay99.com", mWidth / 2, startY + 605, paint);
        c.drawText("V" + SRSApp.versionName, mWidth / 2, startY + 625, paint);


        final Bitmap mBitmap = b;
        imgDraw.setImageBitmap(b);


        try {
            UpdatePrintCountAsync();
        }catch (Exception e){

        }
    }

    RTWS rtWS = new RTWS();

    private void UpdatePrintCountAsync() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (mName.indexOf("PIN") > -1) {
                    DLog.e(TAG, "========================================= UpdatePrintCount");
                    String sClientUserName = SharedPreferenceUtil.getsClientUserName();
                    String sTS = FunctionUtil.getsDNReceivedID();
                    String sEncKey = FunctionUtil.getsEncK2(sClientUserName + "RichTech6318" + sTS);
                    rtWS.UpdatePrintCount(SharedPreferenceUtil.getsClientUserName(), mmRequestReloadPinObject.getsLocalMOID(), sTS, sEncKey);

                }
                return true;
            }
        }.execute();

    }
}

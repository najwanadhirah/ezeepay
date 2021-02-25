package com.rt.qpay99.activity.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.rt.qpay99.Permission.PermissionsUtil;
import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.ImageUtil;

public class BuyPrinterUI extends AppCompatActivity {

    private com.beardedhen.androidbootstrap.BootstrapButton btnBuyNow;
    private String TAG = this.getClass().getName();
    private Context mContext;

    private ImageView imgsrsmobileprinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_printer_ui);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setDisplayShowCustomEnabled(true);
        mContext=this;
//        btnBuyNow =(com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btnBuyNow);
//        btnBuyNow.setOnClickListener(new sendSMSListener());
//
//        imgsrsmobileprinter = (ImageView) findViewById(R.id.imgsrsmobileprinter);
//        imgsrsmobileprinter.setImageDrawable(ImageUtil.ResizeImage(R.drawable.srsmobileprinter,mContext));

    }




}

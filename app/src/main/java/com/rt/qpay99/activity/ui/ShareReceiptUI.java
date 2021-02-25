package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.rt.qpay99.R;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.util.DLog;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareReceiptUI extends AppCompatActivity {

    private WebView mWebView;
    private String mStringUrl;

    private Button btnShare;
    ProgressDialog dialog;

    private String TAG = this.getClass().getName();

    //private ImageView imgDraw;
    private com.rt.qpay99.CustomView.ZoomableImageView imgDraw;

    String mPath = "";

    private PrintDataService printDataService = null;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_receipt_ui);
        mContext= this;
        mStringUrl  = "http://qpay99.com/images/samplereceit3.png";
        mWebView = (WebView)findViewById(R.id.wv);

        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //mWebView.loadUrl(mStringUrl);

        //imgDraw = (ImageView) findViewById(R.id.imgDraw);
        imgDraw = (com.rt.qpay99.CustomView.ZoomableImageView) findViewById(R.id.imgDraw);

        dialog = new ProgressDialog(this); // this = YourActivity

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getHeight();


        int startX = 20;
        int startY = 150;
        int mWidth = 480;

        Bitmap b = Bitmap.createBitmap(mWidth, 800, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        c.drawColor(Color.parseColor("#f1f1f1"), PorterDuff.Mode.OVERLAY);


        Paint paint = new Paint();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.ic_receipt_qpay, options);
        Float centreX = (float) (mWidth  - background.getWidth()) / 2;
//        c.drawBitmap(background, centreX,10, paint);

        //startY = startY + background.getHeight()+30;

        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        c.drawText("Demo Coupon" , startX, startY, paint);
        paint.setTextSize(30);
        c.drawText("Demo Account", startX, startY+40, paint);
        paint.setTextSize(20);
        //c.drawText("Mechant ID :" +  FunctionUtil.countSpacingForShare("Mechant ID :","+60166572577") + "+60166572577", startX, startY+65, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        c.drawText("Mechant ID :", startX, startY+65, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        c.drawText("+60123456789", mWidth-startX, startY+65, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        Time now = new Time();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        //c.drawText("Date :" + FunctionUtil.countSpacingForShare("Date :",currentDateandTime) + currentDateandTime , startX, startY+90, paint);
        c.drawText("Date :", startX, startY+90, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        c.drawText(currentDateandTime, mWidth-startX, startY+90, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        //c.drawText("Serial No.:" + FunctionUtil.countSpacingForShare("Serial No.:","100000001") + "100000001", startX, startY+115, paint);
        c.drawText("Serial No.:", startX, startY+115, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        c.drawText("100000001", mWidth-startX, startY+115, paint);
        paint.setTextAlign(Paint.Align.LEFT);


        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);  // centers horizontally

        c.drawText("SAMPLE RM5", mWidth/2, startY+185, paint);

        paint.setTextAlign(Paint.Align.LEFT);  // centers horizontally
        c.drawText("PIN :", startX, startY+225, paint);
        c.drawText("1234 5678 9012 3456", startX, startY+275, paint);
        paint.setTextSize(20);
        c.drawText("Pin expired Date : 2020-01-01", startX, startY+305, paint);
        c.drawText("Topup Instruction :", startX, startY+335, paint);
        c.drawText("Key in *000*<16-digit reload PIN>#, press CALL", startX, startY+360, paint);
        paint.setTextAlign(Paint.Align.CENTER);  // centers horizontally
        c.drawText("--------------------------------------------------------------", startX, startY+390, paint);
        paint.setTextAlign(Paint.Align.LEFT);  // centers horizontally


        paint.setTextAlign(Paint.Align.CENTER);  // centers horizontally
        c.drawText("Customer Careline : +60123456789", mWidth/2, startY+425, paint);
        //canvas.drawText(text, width / 2, (height - paint.ascent()) / 2, paint);
        c.drawText("9AM - 6PM (Monday - Friday)", mWidth/2, startY+450, paint);
        c.drawText("Thank You", mWidth/2, startY+475, paint);


        final Bitmap mBitmap = b;
        imgDraw.setImageBitmap(b);

        try {
            File file = new File(this.getCacheDir(), "TEST11" + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Log.e("","WEB PATH ====> file://" + file.getAbsolutePath());
            mPath = file.getAbsolutePath();
            //mWebView.loadUrl("file://" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmap(mBitmap,"TESTB");
            }
        });

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
}

package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;

import java.io.File;
import java.io.FileOutputStream;

public class FBShareUI extends SRSBaseActivity {

    Button bhtShareFB;
    ImageView imgFBShare;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fbshare_ui;
    }

    @Override
    protected void activityCreated() {
        getSupportActionBar().setTitle("Share to Facebook and WIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgFBShare = findViewById(R.id.imgFBShare);
        bhtShareFB = findViewById(R.id.bhtShareFB);


    }

    @Override
    protected void initData() {
        bhtShareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FBShareAsymc().execute();
            }
        });
    }


    private class FBShareAsymc extends AsyncTask<Void, Void, String> {

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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd != null)
                if (pd.isShowing())
                    pd.dismiss();
        }

        @Override
        protected String doInBackground(Void... voids) {
            shareBitmap(((BitmapDrawable) imgFBShare.getDrawable()).getBitmap(), "");
            return "";
        }
    }


    private void shareBitmap(Bitmap bitmap, String fileName) {
        try {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            DLog.e(TAG, "SDK_INT " + Build.VERSION.SDK_INT);
            DLog.e(TAG, "LOLLIPOP_MR1 " + Build.VERSION_CODES.LOLLIPOP_MR1);

            File file = new File(mContext.getCacheDir(), "images");
            file.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(file + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            DLog.e(TAG, "FileProvider2");
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
            DLog.e(TAG, "" + e.getMessage());
            e.printStackTrace();
        }

    }


}

package com.rt.qpay99.Helper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

public class PhotoLoader implements Target {
    private final String name;
    private ImageView imageView;

    private String TAG = this.getClass().getName();

    public PhotoLoader(String name, ImageView imageView) {
        this.name = name;
        this.imageView = imageView;
    }

    @Override
    public void onPrepareLoad(Drawable arg0) {
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + name);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            DLog.e(TAG,"" +  file.getAbsolutePath());
            SharedPreferenceUtil.editSharedPreferencedvalue(name,file.getAbsolutePath());
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, ostream);
            ostream.close();

            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

//    @Override
//    public void onBitmapFailed(Drawable arg0) {
//    }

}

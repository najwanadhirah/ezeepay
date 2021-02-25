package com.rt.qpay99.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Helper.PhotoLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class FirebaseUtil {

    private String TAG = this.getClass().getName();

    public void getImagesAsync(Context mContext, final String imgName , final String firebaseId, final ImageView coverImageView) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                HttpHandlerHelper sh = new HttpHandlerHelper();
                String url = "https://srs-mobile.firebaseio.com/BuyProduct/" + firebaseId + "/.json";
                DLog.e(TAG, "url : " + url);
                String jsonStr = sh.makeServiceCall(url);
                DLog.e(TAG, "Response from url: " + jsonStr);
                return jsonStr;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String imgURL = "";
                try {
                    JSONObject json = new JSONObject(s);
                    imgURL = json.getString("imgURL");
                    DLog.e(TAG, "imgURL " + imgURL);
                    Picasso.get()
                            .load(imgURL)
                            .into(new PhotoLoader(imgName, coverImageView));
                    coverImageView.setAdjustViewBounds(true);
                    SharedPreferenceUtil.editgetSessionExpired(System.currentTimeMillis() + 604800000);

                } catch (Exception e) {

                }
            }
        }.execute();
    }
}

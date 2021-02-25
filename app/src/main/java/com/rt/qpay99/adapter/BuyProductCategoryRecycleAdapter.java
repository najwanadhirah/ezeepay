package com.rt.qpay99.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Helper.PhotoLoader;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class BuyProductCategoryRecycleAdapter extends RecyclerView.Adapter<BuyProductCategoryRecycleAdapter.MyViewHolder> {


    private List<ProductInfo> list;
    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    private ArrayList<ProductInfo> p;


    ListenerInterface.RecyclerViewClickListener mListener;

    public BuyProductCategoryRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.mImageColor = mImageColor;
    }

    public BuyProductCategoryRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor, ArrayList<ProductInfo> p) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.mImageColor = mImageColor;
        this.p = p;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items_buyproduct, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            holder.titleTextView.setText(p.get(position).getName());
            final String imgName = p.get(position).getName();
//           holder.coverImageView.setImageResource(mThumbIds[position]);
            holder.titleDescr.setText(p.get(position).getDescription());
            holder.titleDescr.setSelected(true);
            holder.titleTextView.setSelected(true);

            String firebaseId = p.get(position).getFirebaseDBId();
            Long expires = SharedPreferenceUtil.getSessionExpired();

            DLog.e(TAG,"expires " + expires);
            DLog.e(TAG,"System.currentTimeMillis() " + System.currentTimeMillis());

            if (System.currentTimeMillis() < expires) {
                DLog.e(TAG, "get from SharedPreferenceUtil----------------->");
                String str_url = SharedPreferenceUtil.getSharedPreferencedValue(imgName);
                File imgFile = new File(str_url);
                if (imgFile.exists()) {
                    DLog.e(TAG, "imgFile.exists() ----------------->" + imgName);
                    DLog.e(TAG, "str_url " + str_url);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.coverImageView.setImageBitmap(myBitmap);
                    return;
                }
            }

            DLog.e(TAG,"downloadImagesAsync --------------------->");
            downloadImagesAsync(imgName,firebaseId, holder.coverImageView);



        } catch (Exception e) {

        }
    }

    private void downloadImagesAsync(final String imgName ,final String firebaseId, final ImageView coverImageView) {
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


    @Override
    public int getItemCount() {
        return p.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        public TextView titleTextView;
        public ImageView coverImageView;
        public TextView titleDescr;
        public ImageView likeImageView;
        public ImageView shareImageView;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.titleTextView);
            titleDescr = v.findViewById(R.id.titleDescr);
            coverImageView = v.findViewById(R.id.coverImageView);
            cv = itemView.findViewById(R.id.card_view);
            cv.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            DLog.e(TAG, " onClick " + getLayoutPosition());
            mListener.recyclerViewListClicked(v, getLayoutPosition());

        }
    }
}


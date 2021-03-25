package com.rt.qpay99.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Helper.PhotoLoader;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.object.RedeemObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class PointRedeemRecycleAdapter extends RecyclerView.Adapter<PointRedeemRecycleAdapter.MyViewHolder> {


    private List<ProductInfo> list;
    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    private List<RedeemObj> p;


    ListenerInterface.RecyclerViewClickListener mListener;

    public PointRedeemRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, List<RedeemObj> RedeemObjs) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.p = RedeemObjs;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items_redeem_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            holder.tvmName.setText(p.get(position).getmName());
            holder.tvDescr.setText(p.get(position).getDescription());
            holder.tvPoint.setText(p.get(position).getRedeemPoints()  + " QPoints");

            final String imgName = "R" + p.get(position).getFirebaseDBId();
            DLog.e(TAG,"p.get(position).getFirebaseDBId() " + p.get(position).getFirebaseDBId());

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

//            DLog.e(TAG,"getmName " + p.get(position).getmName());
//            if(p.get(position).getmName().equalsIgnoreCase("Printer (S)")){
//                DLog.e(TAG,"getmName2 " + p.get(position).getmName());
//                holder.coverImageView.setImageResource(R.drawable.srsprinter_s);
//            }else  if(p.get(position).getmName().equalsIgnoreCase("Printer (B)")){
//                holder.coverImageView.setImageResource(R.drawable.srsprinter_b);
//
//            }else  if(p.get(position).getmName().toLowerCase().indexOf("paper roll") >-1){
//                holder.coverImageView.setImageResource(R.drawable.ic_paperroll_icon);
//            }
//
//            else{
//                holder.coverImageView.setImageResource(R.drawable.ic_receipt_icon2);
//            }






        } catch (Exception e) {
            DLog.e(TAG,"onBindViewHolder Err " + e.getMessage());
        }
    }

    private int setImages(String name) {
        int imgRes=0;
        try{
            if(FunctionUtil.isSet(name)){
                name = name.replace(".","");
                name = name.replace("4","");
                String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
                imgRes = mContext.getResources().getIdentifier(uri, null,
                        mContext.getPackageName());
                if (imgRes == 0)
                    return R.drawable.ic_no_image;
            }else
                return R.drawable.ic_no_image;

        }catch (Exception e){
            return R.drawable.ic_no_image;
        }
        return imgRes;

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
        public TextView tvmName;
        public ImageView coverImageView;
        public TextView tvDescr;
        public TextView tvPoint;
        public Button btnRedeem;


        public MyViewHolder(View v) {
            super(v);
            tvmName = v.findViewById(R.id.tvmName);
            tvDescr = v.findViewById(R.id.tvDescr);
            tvPoint = v.findViewById(R.id.tvPoint);
            btnRedeem = v.findViewById(R.id.btnRedeem);
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


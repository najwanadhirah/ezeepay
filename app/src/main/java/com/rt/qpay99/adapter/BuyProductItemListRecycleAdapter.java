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

import com.rt.qpay99.Constants;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.Helper.PhotoLoader;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class BuyProductItemListRecycleAdapter<T>  extends RecyclerView.Adapter<BuyProductItemListRecycleAdapter<T> .MyViewHolder> {


    private List<ProductInfo> list;
    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    private ArrayList<T> t;


    ListenerInterface.RecyclerViewClickListener mListener ;


    public BuyProductItemListRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor, ArrayList<T> t) {
        this.mContext = mContext;
        this.mListener  = itemClickListener;
        this.mImageColor = mImageColor;
        this.t = t;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_buyproduct_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        if(!t.isEmpty() && t.get(0) instanceof ProductInfo){
            DLog.e(TAG, " instanceof ProductInfo");
            List<ProductInfo> p = (List<ProductInfo>)t;

            try{
                holder.titleTextView.setText(p.get(position).getName());
                final String imgName = p.get(position).getName();
                holder.titleTextView.setSelected(true);
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



            }catch (Exception e){

            }

        }else{
            DLog.e(TAG, " isnot ProductInfo");
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


    private int setImages(String name) {
        int imgRes=0;
        try{
            if(FunctionUtil.isSet(name)){
                name = name.replace(".","");
                name = name.replace("4","");

                if(name.toLowerCase().indexOf("digi")>-1)
                    name="DIGI";

                if(name.toLowerCase().indexOf("celcom")>-1)
                    name="CELCOM";

                if(name.toLowerCase().indexOf("hotlink")>-1)
                    name="maxis";



                String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
                if(mImageColor.equalsIgnoreCase(Constants.IMG_TOPUP)){
                    uri = "drawable/ic_" + name.toString().toLowerCase() + "_w_icon";
                    imgRes = mContext.getResources().getIdentifier(uri, null,
                            mContext.getPackageName());
                    if (imgRes == 0){
                        uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
                    }

                }

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


    @Override
    public int getItemCount() {
        DLog.e(TAG,"getItemCount ------------------>" + t.size());
        return t.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        public ImageView coverImageView;
        public TextView titleTextView;
        public TextView titleDescr;
        public ImageView likeImageView;
        public ImageView shareImageView;


        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            titleDescr = (TextView) v.findViewById(R.id.titleDescr);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            cv.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            DLog.e(TAG," onClick " + getLayoutPosition());
            mListener.recyclerViewListClicked(v, getLayoutPosition());

        }
    }
}


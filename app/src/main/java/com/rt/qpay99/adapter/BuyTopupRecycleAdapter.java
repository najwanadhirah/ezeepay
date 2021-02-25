package com.rt.qpay99.adapter;

import android.content.Context;
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
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class BuyTopupRecycleAdapter extends RecyclerView.Adapter<BuyTopupRecycleAdapter.MyViewHolder> {


    ListenerInterface.RecyclerViewClickListener mListener;
    private List<ProductInfo> list;
    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    private List<ProductInfo> p;
    private boolean showList = false;

    public BuyTopupRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.mImageColor = mImageColor;
    }

    public BuyTopupRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor, List<ProductInfo> p) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.mImageColor = mImageColor;
        this.p = p;
        Collections.sort(this.p, new Comparator<ProductInfo>() {
            public int compare(ProductInfo one, ProductInfo other) {
                return one.getName().compareTo(other.getName());
            }
        });
    }

    public BuyTopupRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor, List<ProductInfo> p, boolean showList) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.mImageColor = mImageColor;
        this.p = p;
//        Collections.sort(this.p, new Comparator<ProductInfo>() {
//            public int compare(ProductInfo one, ProductInfo other) {
//                return one.getName().compareTo(other.getName());
//            }
//        });
        this.showList = showList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.recycle_items_buytopup, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items_buytopup, parent, false);

        if (showList)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items_buytopup_linearlayout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            holder.titleTextView.setText(p.get(position).getName());
            final String imgName = p.get(position).getName();
            AgentProductDiscount apd = SRSApp.hashmapDiscountRate.get(imgName);
            if (apd != null) {
                String discountType = "% off";
                String discount = "";


                if (apd.getDiscountType().equalsIgnoreCase("FIXEDAMOUNT")) {
                    discountType = "/ bill off";
                }
                DLog.e(TAG, " " + imgName);
                DLog.e(TAG, "" + apd.getDiscountRate() + " " + apd.getDiscountType());
                holder.titleDescr.setText(apd.getDiscountRate() + " " + discountType);
                holder.titleDescr.setVisibility(View.GONE);
            }


//            holder.titleDescr.setText(p.get(position).getDescription());
//            holder.titleDescr.setSelected(true);
            holder.titleTextView.setSelected(true);

            String firebaseId = p.get(position).getFirebaseDBId();
            Long expires = SharedPreferenceUtil.getSessionExpired();

            DLog.e(TAG, "expires " + expires);
            DLog.e(TAG, "System.currentTimeMillis() " + System.currentTimeMillis());
            holder.coverImageView.setImageResource(ImageUtil.setProductImages(imgName,"",mContext));


        } catch (Exception e) {

        }
    }

    private int setImages(String name) {
        int imgRes = 0;
        try {
            if (FunctionUtil.isSet(name)) {
                name = name.replace(".", "");
                name = name.replace("4", "");
                String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
                imgRes = mContext.getResources().getIdentifier(uri, null,
                        mContext.getPackageName());
                if (imgRes == 0)
                    return R.drawable.ic_no_image;
            } else
                return R.drawable.ic_no_image;

        } catch (Exception e) {
            return R.drawable.ic_no_image;
        }
        return imgRes;

    }

    private void downloadImagesAsync(final String imgName, final String firebaseId, final ImageView coverImageView) {
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
        public TextView titleTextView;
        public ImageView coverImageView;
        public TextView titleDescr;
        public ImageView likeImageView;
        public ImageView shareImageView;
        CardView cv;

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


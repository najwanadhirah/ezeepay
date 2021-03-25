package com.rt.qpay99.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class QPromoListRecycleAdapter extends RecyclerView.Adapter<QPromoListRecycleAdapter.MyViewHolder> {


    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    private List<ProductInfo> p;


    ListenerInterface.RecyclerViewClickListener mListener;


    public QPromoListRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, List<ProductInfo> list) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.p = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items_qpromolist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            holder.tvProductName.setText(p.get(position).getName());
            final String imgName = p.get(position).getName();
            AgentProductDiscount apd = SRSApp.hashmapDiscountRate.get(imgName);

            if (apd != null) {
                String discountType = "% off";
                String discount = "";


                if (apd.getDiscountType().equalsIgnoreCase("FIXEDAMOUNT")) {
                    discountType = "/ bill off";
                }
                DLog.e(TAG," " + imgName);
                DLog.e(TAG, "" + apd.getDiscountRate() + " " + apd.getDiscountType());
                holder.tvDiscountRate.setText(apd.getDiscountRate() + " " + discountType);

            }


            holder.tvProductName.setSelected(true);
            String firebaseId = p.get(position).getFirebaseDBId();
            Long expires = SharedPreferenceUtil.getSessionExpired();

            DLog.e(TAG,"expires " + expires);
            DLog.e(TAG,"System.currentTimeMillis() " + System.currentTimeMillis());

        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return p.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        public TextView tvProductName;
        public ImageView coverImageView;
        public TextView tvDiscountRate;
        public TextView tvBuyNow;

        public MyViewHolder(View v) {
            super(v);
            tvProductName = v.findViewById(R.id.tvProductName);
            tvDiscountRate = v.findViewById(R.id.tvDiscountRate);
            coverImageView = v.findViewById(R.id.coverImageView);
            tvBuyNow = v.findViewById(R.id.tvBuyNow);
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

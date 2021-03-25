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
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;

import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class MyAccountRecycleAdapter extends RecyclerView.Adapter<MyAccountRecycleAdapter.MyViewHolder> {


    private List<ProductInfo> list;
    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    private List<ProductInfo> p;


    ListenerInterface.RecyclerViewClickListener mListener;

    public MyAccountRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.mImageColor = mImageColor;
    }

    public MyAccountRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items_buytopup_linearlayout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            holder.titleTextView.setText(menuItems[position]);
            holder.coverImageView.setImageResource(mThumbIds[position]);


        } catch (Exception e) {

        }
    }

    // references to our images
    private Integer[] mThumbIds = { R.drawable.ic_check_balance,
            R.drawable.ic_check_transaction, R.drawable.ic_report,
            R.drawable.ic_bank, R.drawable.ic_bank,R.drawable.ic_agentinfo,
            R.drawable.ic_dn,R.drawable.ic_bank,R.drawable.ic_fb_icon, R.drawable.ic_agentinfo

    };

    private Integer[] menuItems = { R.string.my_acc_check_bal,
            R.string.my_acc_check_transaction, R.string.my_acc_report,
            R.string.my_acc_bank_in,R.string.my_acc_bank_in_list, R.string.my_acc_agent_info, R.string.my_acc_DN,R.string.my_acc_share_credit,
            R.string.my_acc_update_fb_appid,R.string.verify_acc

    };





    @Override
    public int getItemCount() {
        return mThumbIds.length;
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


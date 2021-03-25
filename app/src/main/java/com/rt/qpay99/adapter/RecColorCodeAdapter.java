package com.rt.qpay99.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.object.ColorCodeObj;
import com.rt.qpay99.object.GlobalObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.ImageUtil;

import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class RecColorCodeAdapter<T> extends RecyclerView.Adapter<RecColorCodeAdapter<T>.MyViewHolder> {


    private List<T> list;
    private Context mContext;
    private String TAG = this.getClass().getName();
    int selectedPos = 0;

    ListenerInterface.RecyclerViewClickListener mListener;

    public RecColorCodeAdapter(List<T> Data, Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener) {
        list = Data;
        this.mContext = mContext;
        this.mListener = itemClickListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items_color_code, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.itemView.setSelected(selectedPos == position);
        DLog.e(TAG,"" + position);
        if(selectedPos == position)
            holder.llBorder.setBackground(mContext.getResources().getDrawable(R.drawable.circle_color_bg_onselect));
        else
            holder.llBorder.setBackgroundColor(Color.TRANSPARENT);

        try {
            if (!list.isEmpty() && list.get(position) instanceof ColorCodeObj) {
                DLog.e(TAG, "ColorCodeObj");
                ColorCodeObj o = (ColorCodeObj) list.get(position);
                int d = ImageUtil.getCircleColor(o.getColorName());
                holder.tvName.setText(o.getColorName());
                holder.imgProduct.setBackgroundResource(d);


            }else if(!list.isEmpty() && list.get(position) instanceof GlobalObj) {
                GlobalObj obj = (GlobalObj) list.get(position);
                int d = ImageUtil.getCircleColor(obj.getmName());
                holder.imgProduct.setBackgroundResource(d);
                holder.tvName.setText(obj.getmName());

            }else{
                DLog.e(TAG,"is not ColorCodeObj" );
            }

        } catch (Exception e) {
            DLog.e(TAG,"Err " + e.getMessage() );
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout cv;
        LinearLayout llBorder;
        public TextView tvName;
        public ImageView imgProduct;


        public MyViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
            imgProduct = (ImageView) v.findViewById(R.id.imgProduct);
            cv = (LinearLayout) v.findViewById(R.id.card_view);
            llBorder = (LinearLayout) v.findViewById(R.id.llBorder);
            cv.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            DLog.e(TAG, " onClick " + getLayoutPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
            mListener.recyclerViewListClicked(v, getLayoutPosition());

        }
    }
}


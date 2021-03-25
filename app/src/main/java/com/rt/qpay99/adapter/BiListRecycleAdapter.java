package com.rt.qpay99.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.object.BankIn;
import com.rt.qpay99.util.DLog;

import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class BiListRecycleAdapter extends RecyclerView.Adapter<BiListRecycleAdapter.MyViewHolder> {



    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    ListenerInterface.RecyclerViewClickListener mListener;

    List<BankIn> p;
    public BiListRecycleAdapter(Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, List<BankIn> p) {
        this.mContext = mContext;
        this.mListener = itemClickListener;
        this.mImageColor = mImageColor;
        this.p = p;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_bi_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {

            holder.tvBIAmount.setText("RM " + p.get(position).getAmount());
            holder.tvBIDate.setText("BI Date "  + p.get(position).getDateBI());
            holder.tvBITime.setText("BI Time "  + p.get(position).getTime());
            holder.tvBICreatedTS.setText( p.get(position).getCreated());

            String status = p.get(position).getStatus();
            if(status.equalsIgnoreCase("PENDING")){
                holder.tvBIStatus.setTextColor(mContext.getResources().getColor(R.color.dark_yellow));
            }

            if(status.equalsIgnoreCase("REJECTED")){
                holder.tvBIStatus.setTextColor(mContext.getResources().getColor(R.color.soild_dark_red));
            }

            holder.tvBIStatus.setText(p.get(position).getStatus());
            holder.tvBIBank.setText(p.get(position).getBank());


        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return p.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        public TextView tvBIAmount;
        public TextView tvBIDate;
        public TextView tvBITime;
        public TextView tvBICreatedTS;
        public TextView tvBIStatus;
        public TextView tvBIBank;


        public MyViewHolder(View v) {
            super(v);
            tvBIBank = v.findViewById(R.id.tvBIBank);
            tvBIAmount = v.findViewById(R.id.tvBIAmount);
            tvBIDate = v.findViewById(R.id.tvBIDate);
            tvBITime = v.findViewById(R.id.tvBITime);
            tvBICreatedTS = v.findViewById(R.id.tvBICreatedTS);
            tvBIStatus = v.findViewById(R.id.tvBIStatus);
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


package com.rt.qpay99.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rt.qpay99.Constants;
import com.rt.qpay99.Interface.ListenerInterface;
import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.ImageUtil;

import java.util.List;

/**
 * Created by User on 4/27/2017.
 */

public class RecPinAdapter extends RecyclerView.Adapter<RecPinAdapter.MyViewHolder> {


    private List<String> list;
    private Context mContext;
    private String TAG = this.getClass().getName();
    private String mImageColor;
    private String mProduct;
    int selectedPos=-1;

    ListenerInterface.RecyclerViewClickListener mListener ;
    public RecPinAdapter(List<String> Data, String mProduct, Context mContext, ListenerInterface.RecyclerViewClickListener itemClickListener, String mImageColor) {
        list = Data;
        this.mContext = mContext;
        this.mListener  = itemClickListener;
        this.mImageColor = mImageColor;
        this.mProduct = mProduct;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items_pin, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        if(selectedPos == position)
            holder.llrecycle.setBackground(mContext.getResources().getDrawable(R.drawable.rounder_corner_red));
        else
            holder.llrecycle.setBackground(mContext.getResources().getDrawable(R.drawable.cardviewtext_bg));

       try{
           holder.titleTextView.setText("RM " + list.get(position));
           holder.coverImageView.setImageResource(ImageUtil.setProductImages(mProduct,"",mContext));

//           holder.cv.setCardBackgroundColor(mContext.getResources().getColor(ImageUtil.setBGColor(mProduct)));

       }catch (Exception e){

       }
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
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        public TextView titleTextView;
        public ImageView coverImageView;
        public ImageView likeImageView;
        public ImageView shareImageView;
        public LinearLayout llrecycle;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
            llrecycle= (LinearLayout) v.findViewById(R.id.llrecycle);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            cv.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            DLog.e(TAG," onClick " + getLayoutPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
            mListener.recyclerViewListClicked(v, getLayoutPosition());

        }
    }
}


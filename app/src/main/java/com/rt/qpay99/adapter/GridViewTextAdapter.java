package com.rt.qpay99.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;

import java.util.ArrayList;
import java.util.List;

public class GridViewTextAdapter extends BaseAdapter {

    private List<String> data = new ArrayList<String>();
    private Context mContext;
    private final LayoutInflater inflater;
    private Resources resources;
    private double GST_Tax;

    private String TAG = this.getClass().getName();
    private String mName;

    public GridViewTextAdapter(Context mContext, List<String> product, double GST_Tax) {
        this.data = product;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.GST_Tax = GST_Tax;
    }

    public GridViewTextAdapter(Context mContext, List<String> product, double GST_Tax, String mName) {
        this.data = product;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.GST_Tax = GST_Tax;
        this.mName = mName;
    }

    @Override
    public int getCount() { // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        vHolder holder = new vHolder();
        if (convertView == null) {
            holder = new vHolder();
            convertView = inflater.inflate(
                    R.layout.adapter_layout_gridview_pin_price, null);

            holder.mTitle = convertView
                    .findViewById(R.id.tvPriceTAG);
            //holder.tvPriceTAGWithoutGST =(TextView) convertView.findViewById(R.id.tvPriceTAGWithoutGST);
            holder.mDiscount = convertView.findViewById(R.id.sDiscount);
            convertView.setTag(holder);

        } else {
            holder = (vHolder) convertView.getTag();
        }
        String priceTag = data.get(position);
        //holder.tvPriceTAGWithoutGST.setText("Credit RM" + priceTag);

        AgentProductDiscount p = SRSApp.hashmapDiscountRate.get(mName);

        if (p != null) {
            String discountType = "% off";
            String discount = "";


            if (p.getDiscountType().equalsIgnoreCase("FIXEDAMOUNT")) {
                discountType = "/ bill off";
            }
            DLog.e(TAG," " + mName);
            DLog.e(TAG, "" + p.getDiscountRate() + " " + p.getDiscountType());
            holder.mDiscount.setText(p.getDiscountRate() + " " + discountType);
            holder.mDiscount.setVisibility(View.INVISIBLE);

        }


        holder.mTitle.setText("RM " + FunctionUtil.getPriceWithGST(priceTag, GST_Tax));
        if (priceTag.equalsIgnoreCase("25K")) {
            holder.mTitle.setText("25K");
        }

        if (priceTag.equalsIgnoreCase("100rp")) {
            holder.mTitle.setText("100rp");
        }

        if (priceTag.equalsIgnoreCase("200rp")) {
            holder.mTitle.setText("200rp");
        }

        return convertView;
    }

    private class vHolder {
        TextView mTitle;
        //TextView tvPriceTAGWithoutGST;
        public TextView mDiscount;
    }

}

package com.rt.qpay99.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rt.qpay99.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 1/17/2017.
 */

public class StringAdapter extends BaseAdapter {

    private List<String> data = new ArrayList<String>();
    private Context mContext;
    private LayoutInflater inflater;
    private Resources res;
    private boolean showArrow=true;
    private boolean showIcon=true;


    public StringAdapter(Context mContext, List<String> data, boolean showArrow, boolean showIcon) {
        this.data = data;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.showArrow = showArrow;
        this.showIcon = showIcon;

    }


    @Override
    public int getCount() {
        if(data==null)
            return 0;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        if(data==null)
            return null;
        return  data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vHolder holder = new vHolder();
        if (convertView == null) {
            holder = new vHolder();
            convertView = inflater.inflate(R.layout.adapter_listview_string_data, null);
        holder.imgProduct = (ImageView) convertView
                .findViewById(R.id.imgProduct);
        holder.imgArrow = (ImageView) convertView
                .findViewById(R.id.imgArrow);
        holder.mProductName = (TextView) convertView
                .findViewById(R.id.tvProduct);
        convertView.setTag(holder);
        } else {
            holder = (vHolder) convertView.getTag();
        }

        data.get(position);
        holder.mProductName.setText(data.get(position).toString());

        if(!showIcon)
            holder.imgProduct.setVisibility(View.GONE);

        if(!showArrow)
            holder.imgArrow.setVisibility(View.INVISIBLE);

        return convertView;
    }

    private class vHolder {
        TextView mProductName;
        ImageView imgProduct;
        ImageView imgArrow;
    }
}

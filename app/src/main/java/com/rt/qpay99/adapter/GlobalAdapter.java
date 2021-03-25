package com.rt.qpay99.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rt.qpay99.Constants;
import com.rt.qpay99.R;
import com.rt.qpay99.object.GlobalObj;

import java.util.ArrayList;
import java.util.List;

public class GlobalAdapter<T> extends BaseAdapter {

    List<T> data = new ArrayList<>();

    private final LayoutInflater inflater;

    // private Resources resources;
    private String TAG = this.getClass().getName();
    private String mType;
    private Context mContext;
    String mCat = "";

    public GlobalAdapter(Context mContext, List<T> data, String mType) {

        this.data = data;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.mType = mType;

    }


    public GlobalAdapter(Context mContext, List<T> data, String mType, String mCat) {

        this.data = data;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.mType = mType;
        this.mCat = mCat;
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

    private int selectedItem;

    public void setSelectedItem(int position) {
        selectedItem = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vHolder holder = new vHolder();
        if (convertView == null) {
            holder = new vHolder();

            convertView = inflater.inflate(R.layout.adapter_buyphone_string_data, null);
            holder.ly = (LinearLayout) convertView.findViewById(R.id.llly);
            holder.tvStrName = (TextView) convertView
                    .findViewById(R.id.tvStrName);
            holder.tvStrInfo = (TextView) convertView
                    .findViewById(R.id.tvStrInfo);
            convertView.setTag(holder);
        } else {
            holder = (vHolder) convertView.getTag();
        }

        GlobalObj obj = (GlobalObj)data.get(position);
        holder.tvStrName.setText(obj.getmName());

        if (mType.equalsIgnoreCase(Constants.IPHONE))
            holder.tvStrInfo.setText("Grade");

        if (mType.equalsIgnoreCase(Constants.IPHONE_MEMORY))
            holder.tvStrInfo.setText("Memory");

        if (mType.equalsIgnoreCase(Constants.IPHONE_COLOR))
            holder.tvStrInfo.setText("Color");

        holder.ly.setBackgroundResource(R.color.lightGray);
        if (position == selectedItem) {
            holder.ly.setBackgroundResource(R.drawable.border_primary);
        }

        if(!TextUtils.isEmpty(mCat)){
            if(mCat.equalsIgnoreCase("SIMPACK")){
                holder.tvStrInfo.setText("Buy");
            }
        }


        return convertView;
    }

    private class vHolder {
        TextView tvStrName;
        TextView tvStrInfo;
        LinearLayout ly;

    }

}

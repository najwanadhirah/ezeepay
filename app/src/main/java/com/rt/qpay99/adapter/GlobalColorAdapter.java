package com.rt.qpay99.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rt.qpay99.R;
import com.rt.qpay99.object.GlobalObj;
import com.rt.qpay99.util.DLog;

import java.util.ArrayList;
import java.util.List;

public class GlobalColorAdapter<T> extends BaseAdapter {

    List<T> data = new ArrayList<>();

    private final LayoutInflater inflater;

    // private Resources resources;
    private String TAG = this.getClass().getName();
    private String mType;
    private Context mContext;

    public GlobalColorAdapter(Context mContext, List<T> data, String mType) {

        this.data = data;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.mType = mType;

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

            convertView = inflater.inflate(R.layout.adapter_round_color_code, null);
            holder.ly = (LinearLayout) convertView.findViewById(R.id.llly);
            holder.imgColor = (ImageView) convertView
                    .findViewById(R.id.imgColor);

            convertView.setTag(holder);
        } else {
            holder = (vHolder) convertView.getTag();
        }

        GlobalObj obj = (GlobalObj)data.get(position);
        holder.imgColor.setImageResource(setImages(obj.getmName()));

        holder.ly.setBackgroundResource(R.color.lightGray);
        if (position == selectedItem) {
            holder.ly.setBackgroundResource(R.color.DarkGray);
        }


        return convertView;
    }

    private int setImages(String name) {
        try{
            name= name.toLowerCase();
             name = name.replace(" ","");
            //circle_color_jetblack
            String uri = "drawable/circle_color_" + name.toLowerCase();

            DLog.e(TAG,"------" + uri);
            int imgRes = mContext.getResources().getIdentifier(uri, null,
                    mContext.getPackageName());
            if (imgRes == 0)
                return R.drawable.circle_color;
            return imgRes;
        }catch (Exception e){
            return R.drawable.circle_color;
        }
    }


    private class vHolder {
        ImageView imgColor;
        LinearLayout ly;

    }

}

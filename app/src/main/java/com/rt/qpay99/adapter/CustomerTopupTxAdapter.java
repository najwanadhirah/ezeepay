package com.rt.qpay99.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.object.CustomerTopupTx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 1/18/2017.
 */

public class CustomerTopupTxAdapter extends BaseAdapter {

    private Context mContext;
    private  List<CustomerTopupTx> data = new ArrayList<CustomerTopupTx>();
    private final LayoutInflater inflater;

    public CustomerTopupTxAdapter(Context mContext,
                                  List<CustomerTopupTx> data) {
        this.data = data;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

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
        vHolder holder = new vHolder();
        if (convertView == null) {
            holder = new vHolder();
            convertView = inflater.inflate(
                    R.layout.adapter_customertopuptx, null);

            holder.tvAmount = (TextView) convertView
                    .findViewById(R.id.tvAmount);
            holder.tvRemark = (TextView) convertView
                    .findViewById(R.id.tvRemark);
            holder.tvNewBalance = (TextView) convertView
                    .findViewById(R.id.tvNewBalance);
            holder.tvType = (TextView) convertView
                    .findViewById(R.id.tvType);
            holder.tvCreatedTS = (TextView) convertView
                    .findViewById(R.id.tvCreatedTS);
            convertView.setTag(holder);

        } else {
            holder = (vHolder) convertView.getTag();
        }

        CustomerTopupTx tx = data.get(position);
        holder.tvAmount.setText("Amount : " + tx.getAmount());
        holder.tvRemark.setText("Remark :" + tx.getRemarks());
        holder.tvNewBalance.setText("Balance " + tx.getNewBalance());
        holder.tvType.setText("Type : " + tx.getType());
        holder.tvCreatedTS.setText("Date " + tx.getCreatedTS());

        return convertView;
    }

    private class vHolder {
        TextView tvAmount;
        TextView tvRemark;
        TextView tvNewBalance;
        TextView tvType;
        TextView tvCreatedTS;
    }
}

package com.rt.qpay99.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.object.AgentSales;

public class AgentSalesAdapter extends BaseAdapter {

	private List<AgentSales> data = new ArrayList<AgentSales>();
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;

	public AgentSalesAdapter(Context mContext, List<AgentSales> product) {
		this.data = product;
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
		// TODO Auto-generated method stub
		vHolder holder = new vHolder();
		if (convertView == null) {
			holder = new vHolder();
			convertView = inflater.inflate(R.layout.adapter_layout_agent_sales,
					null);

			holder.mAgentSalesProduct = (TextView) convertView
					.findViewById(R.id.tvAgentSalesProduct);
			holder.mAgentTotalSales = (TextView) convertView
					.findViewById(R.id.tvAgentTotalSales);

			convertView.setTag(holder);

		} else {
			holder = (vHolder) convertView.getTag();
		}

		AgentSales product = data.get(position);
		holder.mAgentSalesProduct.setText(product.getProductName());
		holder.mAgentTotalSales.setText(product.getTotalSales());

		return convertView;
	}

	private class vHolder {
		TextView mAgentSalesProduct;
		TextView mAgentTotalSales;

	}

}

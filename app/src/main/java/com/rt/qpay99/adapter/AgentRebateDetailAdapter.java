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
import com.rt.qpay99.object.AgentProductRebate;

public class AgentRebateDetailAdapter extends BaseAdapter {

	private List<AgentProductRebate> data = new ArrayList<AgentProductRebate>();
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;

	public interface BtnClickListener {
		public abstract void onBtnClick(int position);
	}

	private BtnClickListener listener = null;

	public AgentRebateDetailAdapter(Context mContext,
			List<AgentProductRebate> product) {
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
			convertView = inflater.inflate(
					R.layout.adapter_layout_agent_rebate_detail, null);

			holder.mDenomination = (TextView) convertView
					.findViewById(R.id.tvRebateDenomination);
			holder.mRebateType = (TextView) convertView
					.findViewById(R.id.tvRebateRebateType);
			holder.mRebateRate = (TextView) convertView
					.findViewById(R.id.tvRebateRebateRate);

			convertView.setTag(holder);

		} else {
			holder = (vHolder) convertView.getTag();
		}

		AgentProductRebate product = data.get(position);
		// holder.mProductID.setText(String.valueOf(product.getProductID()));
		// holder.mProductName.setText(product.getProductName());
		holder.mDenomination.setText(product.getDenomination());
		holder.mRebateType.setText(product.getRebateType());
		holder.mRebateRate.setText(product.getRebateRate());
		// holder.mLastUpdated.setText(product.getLastUpdated());

		return convertView;
	}

	private class vHolder {
		TextView mProductID;
		TextView mProductName;
		TextView mDenomination;
		TextView mRebateType;
		TextView mRebateRate;

	}

}

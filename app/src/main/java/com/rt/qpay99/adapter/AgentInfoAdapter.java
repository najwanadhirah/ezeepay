package com.rt.qpay99.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.util.DLog;

import java.util.ArrayList;
import java.util.List;

public class AgentInfoAdapter extends BaseAdapter {

	private List<AgentProductDiscount> data = new ArrayList<AgentProductDiscount>();
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;
	private String TAG = this.getClass().getName();

	public interface BtnClickListener {
		public abstract void onBtnClick(int position);
	}

	private BtnClickListener listener = null;

	public AgentInfoAdapter(Context mContext,
			List<AgentProductDiscount> product, BtnClickListener listener) {
		this.data = product;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		this.listener = listener;
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
					R.layout.adapter_layout_agent_product, null);

			holder.mProductId =  convertView
					.findViewById(R.id.tvAgentProductId);
			holder.mProductName =  convertView
					.findViewById(R.id.tvAgentProductName);
			holder.mDiscountType =  convertView
					.findViewById(R.id.tvAgentDiscountType);
			holder.mDiscountRate =  convertView
					.findViewById(R.id.tvAgentDiscountRate);

			holder.llrow = convertView.findViewById(R.id.llrow);



			convertView.setTag(holder);

		} else {
			holder = (vHolder) convertView.getTag();
		}



		AgentProductDiscount product = data.get(position);
		holder.mProductId.setText(String.valueOf(product.getProductId()));
		holder.mProductName.setText(product.getProductName());
		holder.mDiscountType.setText(product.getDiscountType());
		holder.mDiscountRate.setText(product.getDiscountRate());
		// holder.mLastUpdated.setText(product.getLastUpdated());

		DLog.e(TAG,"" +product.getDiscountRate());


		if(position % 2 == 0 ){
			holder.llrow.setBackgroundColor(mContext.getResources().getColor(R.color.color_disable));
		}else{
			holder.llrow.setBackgroundColor(mContext.getResources().getColor(R.color.solid_white));
		}


		return convertView;
	}

	private class vHolder {
		LinearLayout llrow;
		TextView mProductId;
		TextView mProductName;
		TextView mDiscountType;
		TextView mDiscountRate;
		TextView mLastUpdated;

	}

}

package com.rt.qpay99.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.object.BankIn;

import java.util.ArrayList;
import java.util.List;

public class BankInListAdapter extends BaseAdapter {

	private List<BankIn> data = new ArrayList<BankIn>();
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;

	public interface BtnClickListener {
		public abstract void onBtnClick(int position);
	}

	private BtnClickListener listener = null;

	public BankInListAdapter(Context mContext,
							 List<BankIn> product) {
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
					R.layout.adapter_layout_bankinlist, null);

			holder.mCreated = convertView
					.findViewById(R.id.tvCreated);
			holder.mDateBI = convertView
					.findViewById(R.id.tvDateTime);
			holder.mAmount =  convertView
					.findViewById(R.id.tvAmount);
			holder.mStatus =  convertView
					.findViewById(R.id.tvStatus);

			holder.mBank =  convertView
					.findViewById(R.id.tvBank);


			holder.mRecipientReference=(TextView) convertView
					.findViewById(R.id.tvRecipientReference);
			// holder.mLastUpdated = (TextView) convertView
			// .findViewById(R.id.tvAgentLastUpdate);


			convertView.setTag(holder);

		} else {
			holder = (vHolder) convertView.getTag();
		}




		BankIn product = data.get(position);
		holder.mBank.setText(product.getBank());
		holder.mAmount.setText(product.getAmount());
		holder.mDateBI.setText(product.getDateBI() + " - " + product.getTime());
		//holder.mTime.setText(product.getTime());
		holder.mStatus.setText(product.getStatus());
		holder.mRecipientReference.setText(product.getRecipientReference());
		holder.mCreated.setText(product.getCreated().toString());

		if(product.getStatus().equalsIgnoreCase("REJECTED")){
			convertView.setBackgroundResource(R.color.light_red);
		}

		if(product.getStatus().equalsIgnoreCase("PENDING")){
			convertView.setBackgroundResource(R.color.bootstrap_brand_warning);
		}


		return convertView;
	}

	private class vHolder {
		TextView mBank;
		TextView mAmount;
		TextView mDateBI;
		TextView mTime;
		TextView mStatus;
		TextView mRecipientReference;
		TextView mCreated;
		Button mButton;
	}

}

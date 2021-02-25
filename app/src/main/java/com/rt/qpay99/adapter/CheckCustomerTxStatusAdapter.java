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
import com.rt.qpay99.object.CustomerTxStatusInfo;
import com.rt.qpay99.util.FunctionUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CheckCustomerTxStatusAdapter extends BaseAdapter {

	private List<CustomerTxStatusInfo> data = new ArrayList<CustomerTxStatusInfo>();
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;

	public CheckCustomerTxStatusAdapter(Context mContext,
			List<CustomerTxStatusInfo> product) {
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
					R.layout.adapter_check_cust_tx_status_v2, null);

			holder.mProduct = (TextView) convertView
					.findViewById(R.id.tvChkProduct);
			holder.mMobileNo = (TextView) convertView
					.findViewById(R.id.tvMSISDN);
			holder.mAmount = (TextView) convertView
					.findViewById(R.id.tvChkAmount);
			holder.mStatus = (TextView) convertView
					.findViewById(R.id.tvChkStatus);

			holder.mImgTopup =  convertView.findViewById(R.id.imgTopup);
//			holder.mDN = (TextView) convertView.findViewById(R.id.tvChkDN);
//			holder.mCode = (TextView) convertView.findViewById(R.id.tvChkCode);
			holder.mDateTime = (TextView) convertView
					.findViewById(R.id.tvChkDatetime);

			holder.mPrintCount = (TextView) convertView
					.findViewById(R.id.tvPrintCount);

			convertView.setTag(holder);

		} else {
			holder = (vHolder) convertView.getTag();
		}

//		if(FunctionUtil.IsOdd(position)){
//			convertView.setBackgroundColor(R.color.trans_grey);
//		}else{
//			convertView.setBackgroundColor(R.color.wallet_bright_foreground_holo_light);
//		}
		convertView.setBackgroundResource(R.color.bootstrap_gray_lighter);
		CustomerTxStatusInfo p = data.get(position);

		if(p.getRetry()==0){
			//holder.mMobileNo.setTextColor(mContext.getResources().getColor(R.color.green));
			if(p.getProduct().contains("PIN"))
				convertView.setBackgroundResource(R.color.light_green2);
		}

		holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.solid_black));
		if(p.getStatus().equalsIgnoreCase("REFUNDED")){
//			convertView.setBackgroundResource(R.color.bootstrap_brand_danger);
			holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.bootstrap_brand_danger));
		}

		if(p.getStatus().equalsIgnoreCase("COMPLETED")){
//			convertView.setBackgroundResource(R.color.bootstrap_brand_primary);
			holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.bootstrap_brand_primary));
		}

		if(p.getStatus().equalsIgnoreCase("PENDING")){
//			convertView.setBackgroundResource(R.color.bootstrap_brand_warning);
			holder.mStatus.setTextColor(mContext.getResources().getColor(R.color.bootstrap_brand_warning));
		}


//		holder.mImgTopup.setImageResource(setImages(p.getProduct()));
		Picasso.get()
				.load(FunctionUtil.setImages(mContext,p.getProduct())).resize(50, 50).centerCrop()
				.into(holder.mImgTopup);

		holder.mMobileNo.setText(p.getsReloadMSISDN());
		holder.mProduct.setText(p.getProduct());
		holder.mAmount.setText(p.getRetailPrice());
		holder.mStatus.setText(p.getStatus());
//		holder.mDN.setText(p.getDN());
//		holder.mCode.setText(p.getCode());
		holder.mDateTime.setText(p.getDateTime());
		if(p.getProduct().indexOf("PIN")>-1)
			holder.mPrintCount.setText("" + p.getRetry());
		return convertView;
	}

	private int setImages(String name) {
		int imgRes=0;
		try{
			if(FunctionUtil.isSet(name)){
				name = name.replace(".","");
				name = name.replace("4","");
				String uri = "drawable/ic_" + name.toString().toLowerCase() + "_icon";
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

	private class vHolder {
		ImageView mImgTopup;
		TextView mMobileNo;
		TextView mProduct;
		TextView mAmount;
		TextView mStatus;
//		TextView mDN;
		TextView mCode;
		TextView mDateTime;
		TextView mPrintCount;

	}

}

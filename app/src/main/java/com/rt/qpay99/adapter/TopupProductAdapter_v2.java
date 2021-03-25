package com.rt.qpay99.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.ProductPrice;
import com.rt.qpay99.util.DLog;

import java.util.List;

public class TopupProductAdapter_v2 extends BaseAdapter {
	private Context mContext;
	private final LayoutInflater inflater;
	private Resources resources;
	private List<String> denominationArray,DenominationDescriptionArray;
	List<ProductPrice> data;
	private String TAG = this.getClass().getName();
	private String mName;

	public TopupProductAdapter_v2(Context mContext, List<ProductPrice> product, List<String> denominationArray, List<String> DenominationDescriptionArray, String mName) {
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		this.denominationArray = denominationArray;
		this.DenominationDescriptionArray = DenominationDescriptionArray;
		this.data = product;
		this.mName = mName;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MenuViewHolder holder = null;
		if (convertView == null) {
			holder = new MenuViewHolder();
			convertView = inflater.inflate(
					R.layout.adapter_layout_gridview_international, null);
			holder.mPriceTag =  convertView.findViewById(R.id.sMenuText);
			holder.mDiscount = convertView.findViewById(R.id.sDiscount);
			convertView.setTag(holder);

		} else {
			holder = (MenuViewHolder) convertView.getTag();
		}

		ProductPrice product = data.get(position);
		holder.mPriceTag.setText(product.getPriceTag());

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




		if ((mName.toUpperCase().indexOf("FLEXI") > -1)
				|| mName.toUpperCase().equals("CELCOMADDON")
				|| mName.toUpperCase().equals("CELCOMMAGIC")) {
			DLog.e(TAG, "FLEXI CEHCK");

			if(DenominationDescriptionArray!=null){
				if (denominationArray.size() == DenominationDescriptionArray.size()) {
					DLog.e(TAG, "FLEXI SAME SIZE");
					for (int i = 0; i <= denominationArray.size() - 1; i++) {
						DLog.e(TAG, "get(i)" + denominationArray.get(i));
						DLog.e(TAG, "getPriceValue-" + product.getPriceValue());
						if (denominationArray.get(i).equalsIgnoreCase(product.getPriceValue())) {
							holder.mPriceTag.setText(DenominationDescriptionArray.get(i));
							data.get(position).setPriceTag(DenominationDescriptionArray.get(i));
							break;
						}
					}
				}
			}else{
				holder.mPriceTag.setText(product.getPriceValue());
				data.get(position).setPriceTag(product.getPriceValue());
			}

		}

		if (!denominationArray.contains(product.getPriceValue())) {
			holder.mPriceTag.setEnabled(false);
			holder.mPriceTag.setTextColor(Color.GRAY);
		}

		return convertView;
	}



	private class MenuViewHolder {
		public TextView mPriceTag;
		public TextView mDiscount;

	}


}

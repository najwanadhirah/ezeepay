package com.rt.qpay99.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentManager;

import com.rt.qpay99.util.DLog;

public class PricePinOnItemClickListener implements
		AdapterView.OnItemClickListener {
	private Context mContext;
	private String TAG = this.getClass().getName();
	private FragmentManager fm;

	public PricePinOnItemClickListener(Context mContext) {

		this.mContext = mContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		DLog.e(TAG, "PricePinOnItemClickListener");
		// List<ProductInfo> p = ((List<ProductInfo>)
		// parent.getAdapter().getItem(
		// position));
		// ProductInfo product = new ProductInfo();
		// product = p.get(position);
		//
		// DLog.e(TAG, "P " + p.get(position).getDenomination().toString());
		//
		// Intent intent = new Intent(mContext, PinDetailUI.class);
		// intent.putExtra("Denomination", product.getDenomination());
		// intent.putExtra("Description", product.getDescription());
		// intent.putExtra("Instruction", product.getInstruction());
		// intent.putExtra("Keyword", product.getKeyword());
		// intent.putExtra("Name", product.getName());
		// intent.putExtra("pId", String.valueOf(product.getpId()));
		// mContext.startActivity(intent);
	}
}
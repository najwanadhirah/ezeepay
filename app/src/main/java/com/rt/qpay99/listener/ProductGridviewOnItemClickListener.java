package com.rt.qpay99.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentManager;

import com.rt.qpay99.activity.ui.ProductDetailUI_v2;
import com.rt.qpay99.activity.ui.ProductInternationalDetailUI_v2;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;

import java.util.List;

public class ProductGridviewOnItemClickListener implements
		AdapterView.OnItemClickListener {
	private Context mContext;
	private String TAG = "ProductGridviewOnItemClickListener";
	private FragmentManager fm;
	private Intent intent;

	public ProductGridviewOnItemClickListener(Context mContext,
			FragmentManager fm) {
		this.fm = fm;
		this.mContext = mContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		// if (SRSApp.printerMacAdd.length() < 1) {
		// BluetoothService.getBOundedDevices(mContext);
		// return;
		// }

//		if (!SRSApp.gps.isGPSEnabled()) {
//			SRSApp.gps.showSettingsAlert(mContext);
//			return;
//		}

		DLog.e(TAG, "ProductGridviewOnItemClickListener");
		List<ProductInfo> p = ((List<ProductInfo>) parent.getAdapter().getItem(
				position));
		ProductInfo product = new ProductInfo();
		product = p.get(position);

		if(product==null)return;

//		DLog.e(TAG, "P " + p.get(position).getDenomination().toString());

		intent = new Intent(mContext, ProductDetailUI_v2.class);


		if(product.getName().indexOf("FLEXI")>0){
			intent = new Intent(mContext, ProductInternationalDetailUI_v2.class);
		}

		if(product.getName().indexOf("DATA")>0){
			intent = new Intent(mContext, ProductInternationalDetailUI_v2.class);
		}


		DLog.e(TAG,"" + product.getDenominationDescription());
        intent.putExtra("Tax", product.getTax());
		intent.putExtra("Denomination", product.getDenomination());
		intent.putExtra("DenominationDescription", product.getDenominationDescription());
		intent.putExtra("Description", product.getDescription());
		intent.putExtra("Instruction", product.getInstruction());
		intent.putExtra("Keyword", product.getKeyword());
		intent.putExtra("Name", product.getName());
		intent.putExtra("pId", String.valueOf(product.getpId()));
		intent.putExtra("MaxLen", product.getMaxLen());
		intent.putExtra("MinLen", product.getMinLen());

		mContext.startActivity(intent);
	}
}
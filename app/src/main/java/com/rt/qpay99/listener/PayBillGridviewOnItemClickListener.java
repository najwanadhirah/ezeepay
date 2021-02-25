package com.rt.qpay99.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentManager;

import com.rt.qpay99.SRSApp;
import com.rt.qpay99.activity.ui.QueryAcountUI;
import com.rt.qpay99.activity.ui.UltilitiesBillDetailUI;
import com.rt.qpay99.bluetooth.service.BluetoothService;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class PayBillGridviewOnItemClickListener implements
		AdapterView.OnItemClickListener {
	private Context mContext;
	private String TAG = "PayBillGridviewOnItemClickListener";
	private FragmentManager fm;


	public PayBillGridviewOnItemClickListener(Context mContext,
			FragmentManager fm) {
		this.fm = fm;
		this.mContext = mContext;
	}

	private PrintDataService printDataService = null;

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		DLog.e(TAG, "PayBillGridviewOnItemClickListener");

		List<ProductInfo> p = ((List<ProductInfo>) parent.getAdapter().getItem(
				position));
		ProductInfo product = new ProductInfo();
		product = p.get(position);

		DLog.e(TAG, "P " + p.get(position).getDenomination().toString());

		Intent intent = new Intent(mContext, UltilitiesBillDetailUI.class);


		String productName =product.getName();
		if((productName.equalsIgnoreCase("UMOBILEBILL"))
				||(productName.equalsIgnoreCase("CELCOMBILL"))
				||(productName.equalsIgnoreCase("DIGIBILL"))
				||(productName.equalsIgnoreCase("MAXISBILL"))
				||(productName.equalsIgnoreCase("XOXBILL"))
				||(productName.equalsIgnoreCase("REDONEBILL"))
				||(productName.equalsIgnoreCase("REDTONEBILL"))) {
			intent.putExtra("CATEGORY", "POSTPAID");
		}else{

			intent.putExtra("CATEGORY", "UTILITIES");
		}


		if((productName.equalsIgnoreCase("ASTROBILL"))
				|| (productName.equalsIgnoreCase("AIRPAHANGBILL"))
				){
			intent = new Intent(mContext, QueryAcountUI.class);
		}


		intent.putExtra("Denomination", product.getDenomination());
		intent.putExtra("Description", product.getDescription());
		intent.putExtra("Instruction", product.getInstruction());
		intent.putExtra("Keyword", product.getKeyword());
		intent.putExtra("Name", product.getName());
		intent.putExtra("MaxLen", product.getMaxLen());
		intent.putExtra("MinLen", product.getMinLen());
		intent.putExtra("pId", String.valueOf(product.getpId()));

		DLog.e(TAG, "SharedPreferenceUtil.isRequiredPrinter()"
				+ SharedPreferenceUtil.isRequiredPrinter());

		DLog.e(TAG,
				"SRSApp.printerMacAdd.length() "
						+ SRSApp.printerMacAdd.length());

		DLog.e(TAG,
				"PrintDataService.isPrinterConnected() "
						+ PrintDataService.isPrinterConnected());

		if (SharedPreferenceUtil.isRequiredPrinter()) {
			if (SRSApp.printerMacAdd.length() < 1) {
				BluetoothService.setmContext(mContext);
				BluetoothService.getBOundedDevices(mContext);
				return;
			} else {
				if (!PrintDataService.isPrinterConnected()) {
					printDataService = new PrintDataService(mContext,
							SRSApp.printerMacAdd);
					if (!printDataService.connect()) {
						BluetoothService.setmContext(mContext);
						BluetoothService.getBOundedDevices(mContext);
					}
					return;
				}
			}
		}

//		if (!SRSApp.gps.isGPSEnabled()) {
//			SRSApp.gps.showSettingsAlert(mContext);
//			return;
//		}

		mContext.startActivity(intent);
	}
}
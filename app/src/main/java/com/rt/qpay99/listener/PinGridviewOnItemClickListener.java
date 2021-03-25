package com.rt.qpay99.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;

import androidx.fragment.app.FragmentManager;

import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.activity.ui.IDMoneyPinUI;
import com.rt.qpay99.activity.ui.PinDetailUI;
import com.rt.qpay99.bluetooth.service.BluetoothService;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.List;

public class PinGridviewOnItemClickListener implements
		AdapterView.OnItemClickListener {
	private Context mContext;
	private String TAG = "PayBillGridviewOnItemClickListener";
	private FragmentManager fm;
	private PrintDataService printDataService = null;

	public PinGridviewOnItemClickListener(Context mContext, FragmentManager fm) {
		this.fm = fm;
		this.mContext = mContext;
	}

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

		Intent intent;
		intent = new Intent(mContext, PinDetailUI.class);
		if (product.getName().equalsIgnoreCase("IDMONEYPIN")) {
			intent = new Intent(mContext, IDMoneyPinUI.class);
		}
        intent.putExtra("Tax", product.getTax());
		intent.putExtra("Denomination", product.getDenomination());
		intent.putExtra("Description", product.getDescription());
		intent.putExtra("Instruction", product.getInstruction());
		intent.putExtra("Keyword", product.getKeyword());
		intent.putExtra("Name", product.getName());
		intent.putExtra("pId", String.valueOf(product.getpId()));

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

	private AlertDialog normalAlertDialog(final String msg) {

		return new AlertDialog.Builder(new ContextThemeWrapper(mContext,
				R.style.AlertDialogCustom))
				// .setTitle(mName)
				.setTitle("Alert")
				.setMessage(msg)
				.setCancelable(true)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}
}
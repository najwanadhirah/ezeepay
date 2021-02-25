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
import com.rt.qpay99.activity.ui.SimDetailUI;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.object.ProductInfo;
import com.rt.qpay99.util.DLog;

import java.util.List;

public class SimGridviewOnItemClickListener implements
		AdapterView.OnItemClickListener {
	private Context mContext;
	private String TAG = "PayBillGridviewOnItemClickListener";
	private FragmentManager fm;
	private PrintDataService printDataService = null;

	public SimGridviewOnItemClickListener(Context mContext, FragmentManager fm) {
		this.fm = fm;
		this.mContext = mContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		DLog.e(TAG, "SimGridviewOnItemClickListener");

		List<ProductInfo> p = ((List<ProductInfo>) parent.getAdapter().getItem(
				position));
		ProductInfo product = new ProductInfo();
		product = p.get(position);

		DLog.e(TAG, "P " + p.get(position).getDenomination().toString());

		Intent intent;
		intent = new Intent(mContext, SimDetailUI.class);
        intent.putExtra("Tax", product.getTax());
		intent.putExtra("Denomination", product.getDenomination());
		intent.putExtra("Description", product.getDescription());
		intent.putExtra("Instruction", product.getInstruction());
		intent.putExtra("Keyword", product.getKeyword());
		intent.putExtra("Name", product.getName());
		intent.putExtra("pId", String.valueOf(product.getpId()));


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
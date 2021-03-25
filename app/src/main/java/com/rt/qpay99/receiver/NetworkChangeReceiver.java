package com.rt.qpay99.receiver;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.util.DLog;

public class NetworkChangeReceiver extends BroadcastReceiver {
	private ProgressDialog pd;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("app", "Network connectivity change");
		if (intent.getExtras() != null) {
			NetworkInfo ni = (NetworkInfo) intent.getExtras().get(
					ConnectivityManager.EXTRA_NETWORK_INFO);
			if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
				DLog.i("app", "Network " + ni.getTypeName() + " connected");
				SRSApp.noConnectivity = false;
			}
		}
		if (intent.getExtras().getBoolean(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
			DLog.d("app", "There's no network connectivity");
			Toast.makeText(context, R.string.no_internet_connection,
					Toast.LENGTH_SHORT).show();
			SRSApp.noConnectivity = true;
		}
	}
}

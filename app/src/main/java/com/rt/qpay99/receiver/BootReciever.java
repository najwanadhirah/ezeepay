package com.rt.qpay99.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rt.qpay99.activity.ui.LoginUI;

public class BootReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(context, LoginUI.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(myIntent);
	}

}

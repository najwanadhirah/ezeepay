package com.rt.qpay99.gcm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class RTGCMNotificationActivity extends Activity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
	}

}

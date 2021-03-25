/**
 *
 */
package com.rt.qpay99.gcm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.rt.qpay99.R;
import com.rt.qpay99.util.DLog;

public class GCMNotificationActivity extends Activity implements Const {

	Context mContext;
	String TAG = "GCMNotificationActivity";
	private String PNMsg;
	private TextView tvPNMsg;
	private TextView nav_text;
	private Button nav_btn_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		DLog.e(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_notification);

		tvPNMsg = (TextView) findViewById(R.id.tvPNMsg);

		Bundle extras = getIntent().getExtras();
		PNMsg = extras.getString("message");

		DLog.e(TAG,PNMsg);

		tvPNMsg.setText(PNMsg);

		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		nav_text = (TextView) findViewById(R.id.nav_text);
		nav_text.setText(mContext.getResources().getString(R.string.app_name)
				+ " Notification");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		DLog.e(TAG, "onStart");
	}

}

package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.R;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.ws.RTWS;

import java.util.ArrayList;
import java.util.List;

public class AgentRebateDetailUI extends FragmentActivity {

	private String TAG = this.getClass().getName();
	private ListView lvRebateDetail;

	private Button nav_btn_right;
	private TextView nav_text;
	private Context mContext;
	List<AgentProductDiscount> products = new ArrayList<AgentProductDiscount>();
	private ProgressDialog pd;
	private RTWS rtWS = new RTWS();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_agent_rebate_detail);

		lvRebateDetail = (ListView) findViewById(R.id.lvRebateDetail);

		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		nav_text = (TextView) findViewById(R.id.nav_text);
		nav_text.setText(R.string.my_acc_agent_info);

	}

}

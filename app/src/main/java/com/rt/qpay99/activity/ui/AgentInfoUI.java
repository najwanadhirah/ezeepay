package com.rt.qpay99.activity.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rt.qpay99.R;
import com.rt.qpay99.adapter.AgentInfoAdapter;
import com.rt.qpay99.adapter.AgentInfoAdapter.BtnClickListener;
import com.rt.qpay99.adapter.AgentRebateDetailAdapter;
import com.rt.qpay99.object.AgentProductDiscount;
import com.rt.qpay99.object.AgentProductRebate;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.ArrayList;
import java.util.List;

public class AgentInfoUI extends AppCompatActivity {

	private String TAG = this.getClass().getName();
	private ListView lvAgentProductInfo;
	private AgentInfoAdapter mAgentInfoAdapter;
	private AgentProductDiscount product = new AgentProductDiscount();


	private Context mContext;
	List<AgentProductDiscount> products = new ArrayList<AgentProductDiscount>();
	private ProgressDialog pd;
	private RTWS rtWS = new RTWS();
	private AgentRebateDetailAdapter mAgentRebateDetailAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_agent_info);

		lvAgentProductInfo = (ListView) findViewById(R.id.lvAgentProductInfo);
		mAgentInfoAdapter = new AgentInfoAdapter(mContext, products, listener);
		lvAgentProductInfo.setAdapter(mAgentInfoAdapter);


		GetAgentProductDiscount();

	}

	private BtnClickListener listener = new BtnClickListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onBtnClick(int position) {
			// TODO Auto-generated method stub
			DLog.e(TAG, "" + position);
			products = (List<AgentProductDiscount>) mAgentInfoAdapter
					.getItem(position);
			DLog.e(TAG, "product " + products.get(position).getProductName());
			product = products.get(position);
			if (product.getProductId() > 0) {
				GetAgentProductRebateAsync(product.getProductId());
			}
		}

	};

	private void GetAgentProductRebateAsync(final int sProductID) {

		new AsyncTask<String, Void, List<AgentProductRebate>>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Please wait ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				pd = new ProgressDialog(mContext);
				pd.setTitle(ss1);
				pd.setMessage("Verifying ...");
				pd.setCancelable(false);
				pd.show();
			}

			protected List<AgentProductRebate> doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				return rtWS.GetAgentProductRebate(SharedPreferenceUtil.getsClientUserName(),
						SharedPreferenceUtil.getsClientPassword(), sProductID);
			}

			@Override
			protected void onPostExecute(List<AgentProductRebate> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pd.dismiss();
				if (result.size() > 0) {
					showRebateListAlertDialog(result).show();
				} else {
					noRecordFoundAlertDialog("Sorry no record found!!").show();
				}
			}

		}.execute();

	}

	private AlertDialog showRebateListAlertDialog(
			List<AgentProductRebate> product) {
		View view = getLayoutInflater().inflate(
				R.layout.adapter_layout_agent_rebate_list, null);

		ListView lvAgentRebateList = (ListView) view
				.findViewById(R.id.lvAgentRebateList);
		mAgentRebateDetailAdapter = new AgentRebateDetailAdapter(mContext,
				product);
		lvAgentRebateList.setAdapter(mAgentRebateDetailAdapter);

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(R.string.app_name)
				.setView(view)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

	private AlertDialog showRebateAlertDialog(AgentProductRebate product) {
		TextView tvRebateId, tvRebateDeno, tvRebateProduct, tvRebateRate, tvRebateType;
		View view = getLayoutInflater().inflate(
				R.layout.adapter_layout_agent_rebate, null);
		tvRebateId = (TextView) view.findViewById(R.id.tvRebateId);
		tvRebateDeno = (TextView) view.findViewById(R.id.tvRebateDeno);
		tvRebateProduct = (TextView) view.findViewById(R.id.tvRebateProduct);
		tvRebateRate = (TextView) view.findViewById(R.id.tvRebateRate);
		tvRebateType = (TextView) view.findViewById(R.id.tvRebateType);

		tvRebateId.setText(String.valueOf(product.getProductID()));
		tvRebateDeno.setText(product.getDenomination());
		tvRebateProduct.setText(product.getProductName());
		tvRebateRate.setText(product.getRebateRate());
		tvRebateType.setText(product.getRebateType());

		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(R.string.app_name)
				.setView(view)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

	private AlertDialog noRecordFoundAlertDialog(String msg) {
		return new AlertDialog.Builder(new ContextThemeWrapper(this,
				R.style.AlertDialogCustom))
				.setTitle(R.string.app_name)
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.title_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).create();
	}

	private void GetAgentProductDiscount() {

		new AsyncTask<String, Void, List<AgentProductDiscount>>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pd = new ProgressDialog(mContext, R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Please wait ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				pd = new ProgressDialog(mContext);
				pd.setTitle(ss1);
				pd.setMessage("Verifying ...");
				pd.setCancelable(false);
				pd.show();
			}

			protected List<AgentProductDiscount> doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				return rtWS.GetAgentProductDiscount(SharedPreferenceUtil.getsClientUserName(),
						SharedPreferenceUtil.getsClientPassword());
			}

			@Override
			protected void onPostExecute(List<AgentProductDiscount> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pd != null) {
					pd.dismiss();
					pd = null;
				}

				if (result != null) {
					mAgentInfoAdapter = new AgentInfoAdapter(mContext, result,
							listener);
					lvAgentProductInfo.setAdapter(mAgentInfoAdapter);
					// mAgentInfoAdapter.notifyDataSetChanged();
				}
			}

		}.execute();

	}
}

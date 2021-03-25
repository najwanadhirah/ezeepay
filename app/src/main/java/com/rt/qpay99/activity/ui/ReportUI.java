package com.rt.qpay99.activity.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.adapter.AgentSalesAdapter;
import com.rt.qpay99.bluetooth.service.BluetoothService;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.fragment.CustomDatePickerFragment;
import com.rt.qpay99.object.AgentSales;
import com.rt.qpay99.object.BasicSales;
import com.rt.qpay99.util.CalenderUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportUI extends FragmentActivity implements OnClickListener,
		CustomDatePickerFragment.DatePickedListener {

	private ProgressDialog pd;
	private RTWS rtWS = new RTWS();
	private String TAG = this.getClass().getName();

	private Button nav_btn_right;
	private TextView nav_text, nav_btn_left;
	private Context mContext;
	private TextView edStartDate, edEndDate;
	private boolean isStarDateClick, isEndDateClick = false;
	private CustomDatePickerFragment newFragment = new CustomDatePickerFragment();
	private String sSDate, sEDate, sCustomerAccount;
	private ListView lvReport;
	private BasicSales mBasicSales;
	private Button btnCheckReport, btnPrintReport;

	private List<AgentSales> reportSales = new ArrayList<AgentSales>();

	private AgentSalesAdapter mAgentSalesAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_report);

		mContext = this;

		nav_text = (TextView) findViewById(R.id.nav_text);
		nav_btn_left = (TextView) findViewById(R.id.nav_btn_left);
		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);

		// nav_text.setText(R.string.my_acc_report);
		nav_btn_right = (Button) findViewById(R.id.nav_btn_right);
		nav_btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		btnPrintReport = (Button) findViewById(R.id.btnPrintReport);
		btnPrintReport.setOnClickListener(this);
		btnCheckReport = (Button) findViewById(R.id.btnCheckReport);
		btnCheckReport.setOnClickListener(this);

		edStartDate = (TextView) findViewById(R.id.edStartDate);
		edEndDate = (TextView) findViewById(R.id.edEndDate);
		edStartDate.setOnClickListener(this);
		edEndDate.setOnClickListener(this);

		lvReport = (ListView) findViewById(R.id.lvReport);

		edStartDate.setText(CalenderUtil.getStringAddDate(new Date(),
				"yyyy/MM/dd", 0));
		edEndDate.setText(CalenderUtil.getStringAddDate(new Date(),
				"yyyy/MM/dd", 0));

		GetAgentSalesAsync();

	}

	private void GetAgentSalesAsync() {

		new AsyncTask<String, Void, List<AgentSales>>() {

			@Override
			protected void onPreExecute() {

				// TODO Auto-generated method stub
				super.onPreExecute();
				mBasicSales = new BasicSales();
				mBasicSales.setsClientUserName(SharedPreferenceUtil.getsClientUserName());
				mBasicSales.setsClientPassword(SharedPreferenceUtil.getsClientPassword());
				mBasicSales.setsEDate(edEndDate.getText().toString());
				mBasicSales.setsSDate(edStartDate.getText().toString());
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
				pd.setMessage("Get sales report ...");
				pd.setCancelable(false);
				pd.show();
			}

			protected List<AgentSales> doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				return rtWS.GetAgentSales(mBasicSales.getsClientUserName(),
						mBasicSales.getsClientPassword(),
						mBasicSales.getsSDate(), mBasicSales.getsEDate());

			}

			@Override
			protected void onPostExecute(List<AgentSales> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				if (pd != null)
					if(pd.isShowing())
						pd.dismiss();
				if (result != null && result.size() > 0) {
					reportSales = result;
					mAgentSalesAdapter = new AgentSalesAdapter(mContext, result);
					lvReport.setAdapter(mAgentSalesAdapter);
					GetAgentSalesProfitAsync();
				} else {
					Toast.makeText(mContext,
							"No records found. Please try again later.",
							Toast.LENGTH_SHORT).show();
				}

			}

		}.execute();

	}

	private void GetAgentSalesProfitAsync() {

		new AsyncTask<String, Void, String>() {

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
				pd.setMessage("Get sales profit ...");
				pd.setCancelable(false);
				pd.show();
			}

			protected String doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				return rtWS.GetAgentSalesProfit(
						mBasicSales.getsClientUserName(),
						mBasicSales.getsClientPassword(),
						mBasicSales.getsSDate(), mBasicSales.getsEDate());

			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pd.dismiss();
				if (result != null && result.length() > 0) {
					nav_btn_left.setText("Profit: RM " + result);
				} else {
					Toast.makeText(mContext,
							"No records found. Please try again later.",
							Toast.LENGTH_SHORT).show();
				}

			}

		}.execute();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		DLog.e(TAG, " Click");
		switch (v.getId()) {
		case R.id.edStartDate:
			DLog.e(TAG, "edStartDate Click");
			isStarDateClick = true;
			newFragment.setmListener(this);
			newFragment.show(getSupportFragmentManager(), TAG);
			break;
		case R.id.edEndDate:
			DLog.e(TAG, "edEndDate Click");
			isEndDateClick = true;
			newFragment.setmListener(this);
			newFragment.show(getSupportFragmentManager(), TAG);
			break;

		case R.id.btnCheckReport:
			GetAgentSalesAsync();
			break;

		case R.id.btnPrintReport:
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
                if(SharedPreferenceUtil.getPrinterName().equalsIgnoreCase("InnerPrinter:")){
                    printReceiptInnerPrinter();
                }else
                    printReceipt();
			}
			break;

		}
	}

	@Override
	public void onDatePicked(Calendar time) {
		// TODO Auto-generated method stub
		DLog.e(TAG, "" + time.getTime());
		if (isStarDateClick) {
			edStartDate.setText(CalenderUtil.getStringAddDate(time.getTime(),
					"yyyy/MM/dd", 0));
			isStarDateClick = false;
			sSDate = edStartDate.getText().toString();
		}

		if (isEndDateClick) {
			edEndDate.setText(CalenderUtil.getStringAddDate(time.getTime(),
					"yyyy/MM/dd", 0));
			isEndDateClick = false;
			sEDate = edEndDate.getText().toString();
		}
	}

	private PrintDataService printDataService = null;
	private int totalSales = 0;

	private void printReceiptInnerPrinter() {
		totalSales = 0;
		printDataService = new PrintDataService(mContext, SRSApp.printerMacAdd);
		printDataService.setCommand(21);
		printDataService.send("\n");
		printDataService.setCommand(23);
		printDataService.setCommand(4);
		printDataService.setCommand(21);
		//printDataService.printImage();
		printDataService.send(mContext.getResources().getString(R.string.app_name) + " ");
		printDataService.setCommand(4);
		printDataService.setCommand(20);
		printDataService.setCommand(35);
		printDataService.setCommand(36);
		printDataService.setCommand(37);
		printDataService.send("\n");
		printDataService.send("** Sales Report **");
		printDataService.send("\n");
		printDataService.setCommand(3);
		printDataService.setCommand(2);
		printDataService.send("\n");

		printDataService.send("Merchant ID:");
		printDataService.send(FunctionUtil.countSpacing2("Merchant ID:",
				SharedPreferenceUtil.getsClientUserName()));
		printDataService.send(SharedPreferenceUtil.getsClientUserName());

		printDataService.send("\n");

		printDataService.send("Start Date :");
		printDataService.send(FunctionUtil.countSpacing2("Start Date :", edStartDate.getText()
				.toString()));
		printDataService.send(edStartDate.getText().toString());
		printDataService.send("\n");

		printDataService.send("End Date :");
		printDataService.send(FunctionUtil.countSpacing2("End Date :", edEndDate.getText()
				.toString()));
		printDataService.send(edEndDate.getText().toString());
		printDataService.send("\n");

		printDataService.send("\n");
		printDataService.send("Product");
		printDataService.send(FunctionUtil.countSpacing2("Product", "Total"));
		printDataService.send("Total");
		printDataService.send("\n");
		printDataService.send("--------------------------------");
		printDataService.send("\n");
		if (reportSales.size() > 0) {
			for (AgentSales as : reportSales) {
				printDataService.setCommand(2);
				printDataService.setCommand(20);
				printDataService.send(as.getProductName());
				printDataService.send(countSpacing(as.getProductName(),
						as.getTotalSales()));
				printDataService.send(as.getTotalSales());
				printDataService.send("\n");
				totalSales = totalSales + Integer.parseInt(as.getTotalSales());
			}
		}

		printDataService.send("--------------------------------");
		printDataService.send("Total Sales");
		printDataService.send(FunctionUtil.countSpacing2("Total Sales",
				String.valueOf(totalSales)));
		printDataService.send(String.valueOf(totalSales));

		printDataService.send("\n");
		printDataService.send("--------------------------------");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.send("Customer Care Line: " + Config.Custotmer_care);
		printDataService.send("\n");
		printDataService.send("9AM - 10PM (Monday - Friday)");
		printDataService.send("\n");
//		printDataService.send("www.QPay99.com");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
	}

	private void printReceipt() {
		totalSales = 0;
		printDataService = new PrintDataService(mContext, SRSApp.printerMacAdd);
		printDataService.setCommand(21);
		printDataService.send("\n");
		printDataService.setCommand(23);
		printDataService.setCommand(4);
		printDataService.setCommand(21);
		//printDataService.printImage();
		printDataService.send(mContext.getResources().getString(R.string.app_name) + " ");
		printDataService.setCommand(4);
		printDataService.setCommand(20);
		printDataService.setCommand(35);
		printDataService.setCommand(36);
		printDataService.setCommand(37);
		printDataService.send("\n");
		printDataService.send("** Sales Report **");
		printDataService.send("\n");
		printDataService.setCommand(3);
		printDataService.setCommand(2);
		printDataService.send("\n");

		printDataService.send("Merchant ID:");
		printDataService.send(countSpacing("Merchant ID:",
				SharedPreferenceUtil.getsClientUserName()));
		printDataService.send(SharedPreferenceUtil.getsClientUserName());

		printDataService.send("\n");

		printDataService.send("Start Date :");
		printDataService.send(countSpacing("Start Date :", edStartDate.getText()
				.toString()));
		printDataService.send(edStartDate.getText().toString());
		printDataService.send("\n");

		printDataService.send("End Date :");
		printDataService.send(countSpacing("End Date :", edEndDate.getText()
				.toString()));
		printDataService.send(edEndDate.getText().toString());
		printDataService.send("\n");

		printDataService.send("\n");
		printDataService.send("Product");
		printDataService.send(countSpacing("Product", "Total"));
		printDataService.send("Total");
		printDataService.send("\n");
		printDataService.send("------------------------------------------");
		printDataService.send("\n");
		if (reportSales.size() > 0) {
			for (AgentSales as : reportSales) {
				printDataService.setCommand(2);
				printDataService.setCommand(20);
				printDataService.send(as.getProductName());
				printDataService.send(countSpacing(as.getProductName(),
						as.getTotalSales()));
				printDataService.send(as.getTotalSales());
				printDataService.send("\n");
				totalSales = totalSales + Integer.parseInt(as.getTotalSales());
			}
		}

		printDataService.send("------------------------------------------");
		printDataService.send("Total Sales");
		printDataService.send(countSpacing("Total Sales",
				String.valueOf(totalSales)));
		printDataService.send(String.valueOf(totalSales));

		printDataService.send("\n");
		printDataService.send("------------------------------------------");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.send("Customer Care Line: " + Config.Custotmer_care);
		printDataService.send("\n");
		printDataService.send("9AM - 10PM (Monday - Friday)");
		printDataService.send("\n");
//		printDataService.send("www.QPay99.com");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
	}

	private String countSpacing(String name, String total) {
		String space = "";
		if (FunctionUtil.isSet(name)) {
			if (FunctionUtil.isSet(total)) {

				int totalSpacing = 40;
				int cLen = name.length() + total.length();
				int newLen = totalSpacing - cLen;

				for (int i = 0; i <= newLen; i++) {
					space = space + " ";
				}
			}

		}

		return space;
	}

	String BILL;

	private String getBillBody() {
		BILL = "Customer No:             "
				+ SharedPreferenceUtil.getsClientUserName() + "\n";

		BILL += "Start Date :             " + edStartDate.getText().toString()
				+ "\n";
		BILL += "End Date   :             " + edEndDate.getText().toString()
				+ "\n";

		return BILL;

	}

}

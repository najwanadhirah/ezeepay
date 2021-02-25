package com.rt.qpay99.activity.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.rt.qpay99.Config;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.bluetooth.service.BluetoothService;
import com.rt.qpay99.bluetooth.service.PrintDataService;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;

public class TestPrintUI extends FragmentActivity {

	private String TAG = this.getClass().getName();
	private Button btnSelect,btnPrint;

	private Context mContext;
	private PrintDataService printDataService = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_testprint);

		btnSelect =(Button) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BluetoothService.setmContext(mContext);
				BluetoothService.getBOundedDevices(mContext);
			}
		});

		btnPrint =(Button) findViewById(R.id.btnPrint);
		btnPrint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
					printReceipt();
				}
			}
		});



	}

	private void printReceipt() {
		printDataService = new PrintDataService(mContext, SRSApp.printerMacAdd);
		printDataService.setCommand(21);
		printDataService.send("\n");
		printDataService.setCommand(23);
		printDataService.setCommand(4);
		printDataService.setCommand(21);
		printDataService.printImage2();
		printDataService.send(mContext.getResources().getString(R.string.app_name) + " ");
		printDataService.setCommand(4);
		printDataService.setCommand(20);
		printDataService.send("COUPON ");

		printDataService.setCommand(35);
		printDataService.setCommand(36);
		printDataService.setCommand(37);
		printDataService.send("\n");
		printDataService.send(SharedPreferenceUtil.getMerchantName());
		printDataService.send("\n");
		printDataService.setCommand(3);
		printDataService.setCommand(2);

		printDataService.send("Merchant ID :");
		printDataService.send(FunctionUtil.countSpacing("Merchant ID :",
				SharedPreferenceUtil.getsClientUserName()));
		printDataService.send(SharedPreferenceUtil.getsClientUserName());
		printDataService.send("\n");

		printDataService.send("Date :");
		printDataService.send(FunctionUtil.countSpacing("Date :",
				FunctionUtil.getStrCurrentDateTime()));
		printDataService.send(FunctionUtil.getStrCurrentDateTime());
		printDataService.send("\n");

		printDataService.send("Serial No. :");
		printDataService.send(FunctionUtil.countSpacing("Serial No. :", "100000001"));
		printDataService.send("100000001");
		printDataService.send("\n");

		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.setCommand(23);
		printDataService.setCommand(24);
		printDataService.setCommand(25);
		printDataService.setCommand(26);
		printDataService.setCommand(32);
		printDataService.send(Config.DIGI_RM5);

		printDataService.setCommand(2);
		printDataService.setCommand(22);
		printDataService.setCommand(3);
		printDataService.setCommand(4);
		printDataService.setCommand(21);

		printDataService.send("SAMPLE" + " " + "RM" + "5");
		printDataService.send("\n");

		printDataService.setCommand(20);
		printDataService.send("PIN :");
		printDataService.send("\n");
		printDataService.send("1234567890123456");
		printDataService.setCommand(3);
		printDataService.send("\n");

		printDataService.setCommand(2);
		printDataService.send("Pin Expired Date : 2020-01-01");
		printDataService.send("\n");
		printDataService.send("\n");

		printDataService.setCommand(2);
		printDataService.send("Topup Instruction :");
		printDataService.send("\n");

		printDataService.send("Key in *000*<16-digit reload PIN>#, press CALL");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.setCommand(21);
		printDataService.send("------------------------------------------");
		printDataService.send("Customer Care Line: " + Config.Custotmer_care);
		printDataService.send("\n");
		printDataService.send("9AM - 6PM (Monday - Friday)");
		printDataService.send("\n");
		printDataService.send("Thank You");
		printDataService.send("\n");
		printDataService.send("\n");
		printDataService.send("\n");
	}

}

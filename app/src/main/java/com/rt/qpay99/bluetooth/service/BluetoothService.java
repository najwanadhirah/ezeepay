package com.rt.qpay99.bluetooth.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.rt.qpay99.Config;
import com.rt.qpay99.Helper.HttpHandlerHelper;
import com.rt.qpay99.R;
import com.rt.qpay99.SRSApp;
import com.rt.qpay99.adapter.BluetoothDeviceAdapter;
import com.rt.qpay99.innerPrinterUtil.BluetoothUtil;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothService {

	private static String TAG = "BluetoothService";

	private static Context mContext = null;
	private static BluetoothAdapter bluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private static ArrayList<BluetoothDevice> bondDevices = null;
	private static BluetoothDevice mDevice;

	private BroadcastReceiver receiver;
	private String deviceAddress;
	private BluetoothDeviceAdapter btAdapter;
	private String strUUID;

	private static LayoutInflater inflater;
	private static AlertDialog ad;

	private static ArrayAdapter<String> mPairedDevicesArrayAdapter;
	private static PrintDataService pd;
	private static ProgressDialog mProgressDialog;
	private static Switch switchPrinter;

	static String Url_Param  ;

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		BluetoothService.mContext = mContext;
	}

	public BluetoothService(Context mContext) {
		this.mContext = mContext;
		// this.initIntentFilter();
		createBTBroadcastReceiver();
		this.bondDevices = new ArrayList<BluetoothDevice>();
		DLog.e(TAG, "bondDevices " + bondDevices.size());

	}

	public void searchDevices() {
		this.bondDevices.clear();
		this.bluetoothAdapter.startDiscovery();
	}

	public boolean getPairedDevices() {
		return bondDevices.size() > 0;
	}

	public String getDevicesAddress() {
		deviceAddress = "";
		if (bondDevices.size() > 0) {
			deviceAddress = bondDevices.get(0).getAddress();
			setUUID();
		}
		return deviceAddress;
	}

	private String setUUID() {
		strUUID = "";
		strUUID = bondDevices.get(0).getUuids().toString();
		DLog.e(TAG, "strUUID " + strUUID);
		Config.strUUID = strUUID;
		return strUUID;
	}

	private void initIntentFilter() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		mContext.registerReceiver(receiver, intentFilter);

	}

	private void createBTBroadcastReceiver() {

		if (receiver == null) {

			receiver = new BroadcastReceiver() {
				ProgressDialog progressDialog = null;

				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
							addBandDevices(device);
						} else {
							// addUnbondDevices(device);
						}
					} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
							.equals(action)) {
						progressDialog = ProgressDialog.show(context,
								"Please wait...", "Searching...", true);

					} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
							.equals(action)) {

						progressDialog.dismiss();
					}

					if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
						if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {


						} else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {

						}
					}
				}
			};
			initIntentFilter();
		}

	}

	private static class btDevicesOnItemClickListener implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			if (ad != null) {
				ad.dismiss();
			}

			String macAdd = adapter.getItemAtPosition(position).toString();
			DLog.e(TAG,"macAdd " +macAdd);
			String line[] = macAdd.split("[\\r\\n]+");
			if (line.length >= 2) {
				SRSApp.printerMacAdd = line[1];
				Config.printerId = SRSApp.printerMacAdd.replace(":", "");
				DLog.e(TAG, "btDevicesOnItemClickListener " + macAdd);
				DLog.e(TAG, "btDevicesOnItemClickListener 1 " + line[0]);
				DLog.e(TAG, "btDevicesOnItemClickListener 2 " + line[1]);
				Config.printerName = line[0];
				//?macaddress=1&serverkey=1&descption=1&descption2=1
				Url_Param = "macaddress=" + line[1].replace(":","") + "&serverkey=" + "qpay99" + "&descption=" + line[0] + "&descption2=" + line[1].replace(":","");
				SharedPreferenceUtil.editPrinterName(line[0]);
//				updatePrinterMacAddressAsync();
				connectPrinterAsync();

			}


		}
	}

	public static void updatePrinterMacAddressAsync() {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				mProgressDialog = new ProgressDialog(mContext,
						R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Please wait ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				mProgressDialog = new ProgressDialog(mContext);
				mProgressDialog.setTitle(ss1);
				mProgressDialog.setMessage("Connecting database to verify printer...");
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
			}


			@Override
			protected void onPostExecute(Boolean aBoolean) {
				super.onPostExecute(aBoolean);
				if(mProgressDialog!=null)
					mProgressDialog.dismiss();
				connectPrinterAsync();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				HttpHandlerHelper sh = new HttpHandlerHelper();

				String url = Config.URL_UpdatePrinterMacAddress + Url_Param;
				DLog.e(TAG, "url post : " + url);
				String jsonStr = sh.makeServiceCall(url);
				DLog.e(TAG, "Response from url: " + jsonStr);
				return null;
			}
		}.execute();
	}

	public  static void ConnectInnerPrinter(){
		BluetoothUtil mBluetoothUtil  = new BluetoothUtil();
	}

	public static void connectPrinterAsync() {
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				mProgressDialog = new ProgressDialog(mContext,
						R.style.AlertDialogCustom);
				SpannableString ss1 = new SpannableString("Please wait ...");
				ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);
				ss1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
						ss1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				ss1.setSpan(
						new ForegroundColorSpan(Color.parseColor("#008000")),
						0, ss1.length(), 0);
				mProgressDialog = new ProgressDialog(mContext);
				mProgressDialog.setTitle(ss1);
				mProgressDialog.setMessage("Connecting printer ...");
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
			}

			@SuppressWarnings("static-access")
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				DLog.e(TAG, "printDataService.connect()");

				pd = new PrintDataService(mContext, SRSApp.printerMacAdd);
				return pd.connect();


			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(mProgressDialog!=null)
					mProgressDialog.dismiss();
				if (result) {
					Toast.makeText(mContext, "Printer Connected  ",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext,
							"Connect FAILED",
							Toast.LENGTH_SHORT).show();
				}
			}

		}.execute();
	}

	private void getBTBOundedDevices(final String msg) {
		inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_layout_bt_devices, null);

		ListView listview = (ListView) view.findViewById(R.id.listview);
		btAdapter = new BluetoothDeviceAdapter(mContext, bondDevices);
		listview.setTextFilterEnabled(true);
		listview.setAdapter(btAdapter);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
		builder.setTitle(msg);
		builder.setView(view);
		builder.setCancelable(false);
		ad = builder.create();

		listview.setOnItemClickListener(new btDevicesOnItemClickListener());
		builder.create();
		ad.show();

	}

	public static void getBOundedDevices(Context mContext) {
		inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_layout_bt_devices, null);
		ListView listview = (ListView) view.findViewById(R.id.listview);

		switchPrinter = (Switch) view.findViewById(R.id.switchPrinter);
		switchPrinter.setChecked(false);
		if (SharedPreferenceUtil.isRequiredPrinter())
			switchPrinter.setChecked(true);

		switchPrinter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				DLog.e(TAG, "isChecked " + isChecked);
				SharedPreferenceUtil.editIsRequiredPrinter(isChecked);
				// Do Something
			}
		});

		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(mContext,
				R.layout.device_name);
		listview.setAdapter(mPairedDevicesArrayAdapter);
		Set<BluetoothDevice> mPairedDevices = bluetoothAdapter
				.getBondedDevices();

		if (mPairedDevices.size() > 0) {
			for (BluetoothDevice bt : bluetoothAdapter.getBondedDevices()) {

//				mPairedDevicesArrayAdapter.add(bt.getName() + ":\n"
//						+ bt.getAddress()  +  ":\n"
//						+ bt.getBondState()  +  ":\n"
//						+ bt.getUuids().toString()  +  ":\n"
//				);

				mPairedDevicesArrayAdapter.add(bt.getName() + ":\n"
						+ bt.getAddress());
			}
		} else {
			String mNoDevices = "None Paired";// getResources().getText(R.string.none_paired).toString();
			mPairedDevicesArrayAdapter.add(mNoDevices);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
		builder.setTitle("Select printer ");
		builder.setView(view);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.skip,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// SRSApp.printerMacAdd = SRSApp.NOT_REQUIRED_PRINTER;
					}
				});
		ad = builder.create();

		listview.setOnItemClickListener(new btDevicesOnItemClickListener());
		builder.create();
		ad.show();

	}



	public void addBandDevices(BluetoothDevice device) {
		if (!this.bondDevices.contains(device)) {
			this.bondDevices.add(device);
		}
		DLog.e(TAG, "bondDevices 2 " + bondDevices.size());
		if (bondDevices.size() > 0)
			DLog.e(TAG, "getAddress 2 " + bondDevices.get(0).getAddress());
	}

	public void unRegisterBTReceiver() {
		if (receiver != null) {
			try {
				mContext.unregisterReceiver(receiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
			receiver = null;
		}
	}

}

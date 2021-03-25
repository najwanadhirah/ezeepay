package com.rt.qpay99.adapter;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rt.qpay99.R;

public class BluetoothDeviceAdapter extends BaseAdapter {

	private Context mContext;
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private final LayoutInflater inflater;

	public BluetoothDeviceAdapter(Context mContext,
			List<BluetoothDevice> devices) {
		this.mContext = mContext;
		this.devices = devices;
		inflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return devices.size();
	}

	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return devices.get(location);
	}

	@Override
	public long getItemId(int location) {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		vHolder holder = new vHolder();
		if (convertView == null) {
			holder = new vHolder();
			convertView = inflater.inflate(R.layout.adapter_layout_bt_devices,
					null);

			holder.devicesName = (TextView) convertView
					.findViewById(R.id.tvDeviceName);
			holder.deviceAddress = (TextView) convertView
					.findViewById(R.id.tvDeviceAdd);
			convertView.setTag(holder);

		} else {
			holder = (vHolder) convertView.getTag();
		}

		BluetoothDevice device = devices.get(position);
		holder.devicesName.setText(device.getName());
		holder.deviceAddress.setVisibility(View.INVISIBLE);
		holder.deviceAddress.setText(device.getAddress());

		return convertView;
	}

	private class vHolder {
		TextView devicesName;
		TextView deviceAddress;
		TextView deviceStatus;

	}

}

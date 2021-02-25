package com.rt.qpay99.receiver;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rt.qpay99.util.DLog;

public class BluetoothConnectActivityReceiver extends BroadcastReceiver
{

        String strPsw = "0";
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
                // TODO Auto-generated method stub
//                if (intent.getAction().equals(
//                                "android.bluetooth.device.action.PAIRING_REQUEST"))
//                {
//                        BluetoothDevice btDevice = intent
//                                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//                        // byte[] pinBytes = BluetoothDevice.convertPinToBytes("1234");
//                        // device.setPin(pinBytes);
//                        DLog.i("BluetoothConnectActivityReceiver", "onReceive");
//                        try
//                        {
//                             
//                        }
//                        catch (Exception e)
//                        {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                        }
//                }


        }
}
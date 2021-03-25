package com.rt.qpay99.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.rt.qpay99.util.DLog;

public class OTPReceiver extends BroadcastReceiver {

    String TAG = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        DLog.e(TAG,"onReceive " + intent.getAction().toString());
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    DLog.e(TAG,"SUCCESS ");
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    DLog.e(TAG,"message " + message);
                    // Extract one-time code from the message and complete verification
                    // by sending the code back to your server.
                    break;
                case CommonStatusCodes.TIMEOUT:
                    DLog.e(TAG,"TIMEOUT ");
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }
}

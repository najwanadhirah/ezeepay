package com.rt.qpay99.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.rt.qpay99.R;

/**
 * Created by User on 9/5/2016.
 */
public class SendWhatsAppListener implements View.OnClickListener {

    private Context mContext;
    public SendWhatsAppListener(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public void onClick(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.register));
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        mContext.startActivity(sendIntent);
    }
}

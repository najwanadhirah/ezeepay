package com.rt.qpay99.ShareFunc;

import android.content.Context;
import android.widget.Toast;

public class ShareWidget {

    Context mContext;

    public ShareWidget(Context mContext) {
        this.mContext = mContext;
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}

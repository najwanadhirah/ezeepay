package com.rt.qpay99.ShareFunc;

import android.os.AsyncTask;

import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;


public class ChangePassTaskAsync extends AsyncTask<String, String, Boolean> {

    RTWS rtWS = new RTWS();
    private ChangePassResultListener mListner;

    public ChangePassTaskAsync(ChangePassResultListener listner) {
        this.mListner = listner;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListner != null) mListner.onStart();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        //DO YOUR STUFF
        if (params[0].length() == 8)
            return rtWS.changePassword(
                    SharedPreferenceUtil.getsClientUserName(),
                    SharedPreferenceUtil.getsClientPassword(),
                    params[0]);

        return false;

    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (mListner != null) mListner.onResult(result);
    }

    public interface ChangePassResultListener {

        public void onStart();

        public void onResult(Boolean result);
    }
}

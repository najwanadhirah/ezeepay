package com.rt.qpay99.ShareFunc;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.rt.qpay99.Config;
import com.rt.qpay99.object.AgentInfoObj;
import com.rt.qpay99.util.DLog;
import com.rt.qpay99.util.FunctionUtil;
import com.rt.qpay99.util.HttpUtil;
import com.rt.qpay99.util.SharedPreferenceUtil;
import com.rt.qpay99.ws.RTWS;

public class SendLocationAsync extends AsyncTask<String, String, String> {

    RTWS rtWS = new RTWS();
    private SendLocationAsyncListener mListner;
    AgentInfoObj obj = new AgentInfoObj();
    private String TAG = this.getClass().getName();
    Context mContext;
    FusedLocationProviderClient client;
    String loc = "";

    public SendLocationAsync(SendLocationAsyncListener listner, Context mContext) {
        this.mListner = listner;
        this.mContext = mContext;
        this.client = LocationServices.getFusedLocationProviderClient(mContext);
        initData();
    }

    void initData() {
        LocationRequest request = new LocationRequest();
        request.setInterval(3600000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int permission = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Config.VersionCheck_Status = "ACTIVE";
                        if (Config.VersionCheck_Status.equalsIgnoreCase("ACTIVE")) {
                            DLog.e(TAG, "set location ------------------------------" + location.getProvider());
                            loc = String.valueOf(location.getLatitude()) + "|" + String.valueOf(location.getLongitude());
                        }
                        Config.Latitude = location.getLatitude();
                        Config.Longitude = location.getLongitude();
                    }
                }
            }, null);


        }
        obj.setAgentID(0);
        obj.setName(SharedPreferenceUtil.getMerchantName());
        obj.setHPNo(SharedPreferenceUtil.getsClientUserName());
        obj.setState("");
        obj.setDistrict("");
        obj.setPrefixFilter("");
        obj.setParentAgentID(SharedPreferenceUtil.getsClientID());
        obj.setBanner("");
        obj.setCreatedBy(SharedPreferenceUtil.getServerKey());
        obj.setLastModifiedBy(FunctionUtil.getDeviceIdOrAndroidId(mContext));
        obj.setLastModifiedBy("");
        obj.setLastModifiedDate("");
        obj.setAddress("");
        obj.setEmail("");
        obj.setLocation(loc);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListner != null) mListner.onStart();
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = Config.AZUREMAIN_URL + Config.AGENT_URL;
        DLog.e(TAG, "url " + url);
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        DLog.e(TAG, "json " + json);
        String r = HttpUtil.httpPost(url, json);
        DLog.e(TAG, "" + r);
        return r;

    }

    @Override
    protected void onPostExecute(String result) {
        if (mListner != null) mListner.onResult(result);
    }

    public interface SendLocationAsyncListener {

        public void onStart();

        public void onResult(String result);
    }
}



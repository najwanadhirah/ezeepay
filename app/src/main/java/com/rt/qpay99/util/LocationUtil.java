package com.rt.qpay99.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rt.qpay99.Config;

public class LocationUtil {

    private FusedLocationProviderClient fusedLocationClient;
    private String TAG = "LocationUtil";
    private LastKnowLocationInterface mLastKnowLocationInterface;
    private Context mContext;

    public LocationUtil() {

    }

    public LocationUtil(Context mCtx, LastKnowLocationInterface mLoc) {
        mContext = mCtx;
        mLastKnowLocationInterface = mLoc;
    }

    public void getLastKnowLocation(Context mContext) {
        DLog.e(TAG, "getLastKnowLocation");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient((Activity) mContext);
        fusedLocationClient.getLastLocation()
                .addOnFailureListener((Activity) mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DLog.e(TAG, "onFailure");
                        mLastKnowLocationInterface.onFailure();
                    }
                })
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            DLog.e(Config.DEBUG_TAG, "fusedLocationClient " + location.toString());
                            try {
                                DLog.e(TAG, "fusedLocationClient " + location.getLatitude() + "," + location.getLongitude());
                                SharedPreferenceUtil.editLocationCoordinate(location.getLatitude() + "," + location.getLongitude());
                                Config.Latitude = location.getLatitude();
                                Config.Longitude = location.getLongitude();
                                DLog.e(TAG, "getLocationCoordinate " + SharedPreferenceUtil.getLocationCoordinate());
                                mLastKnowLocationInterface.onSuccess();
                            } catch (Exception e) {
                                DLog.e(TAG, "fusedLocationClient Err " + e.getMessage());
                            }
                        }
                    }
                });
    }

    public boolean canGetLocation(Context context) {
        return isLocationEnabled(context); // application context
    }

    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public interface LastKnowLocationInterface {
        public void onSuccess();

        public void onFailure();

    }
}

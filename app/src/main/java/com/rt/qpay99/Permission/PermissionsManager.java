package com.rt.qpay99.Permission;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by User on 9/15/2016.
 */
public class PermissionsManager {
    public interface PermissionsResultCallback {
        void onRequestPermissionsResult(boolean allGranted);
    }
    private int mRequestCodeId;
    private final Context mContext;
    private final Map<Integer, PermissionsResultCallback> mRequestIdToCallback = new HashMap<>();
    private static PermissionsManager sInstance;
    public PermissionsManager(Context context) {
        mContext = context;
    }

    @NonNull
    public static synchronized PermissionsManager get(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new PermissionsManager(context);
        }
        return sInstance;
    }
    private synchronized int getNextRequestId() {
        return ++mRequestCodeId;
    }
    public synchronized void requestPermissions(@NonNull PermissionsResultCallback callback,
                                                @Nullable Activity activity,
                                                String... permissionsToRequest) {
        List<String> deniedPermissions = PermissionsUtil.getDeniedPermissions(
                mContext, permissionsToRequest);
        if (deniedPermissions.isEmpty()) {
            return;
        }
        // otherwise request the permissions.
        int requestId = getNextRequestId();
        String[] permissionsArray = deniedPermissions.toArray(
                new String[deniedPermissions.size()]);
        mRequestIdToCallback.put(requestId, callback);
        if (activity != null) {
            PermissionsUtil.requestPermissions(activity, requestId, permissionsArray);
        } else {
            PermissionsActivity.run(mContext, requestId, permissionsArray);
        }
    }
    public synchronized void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        PermissionsResultCallback permissionsResultCallback = mRequestIdToCallback.get(requestCode);
        mRequestIdToCallback.remove(requestCode);
        boolean allGranted = PermissionsUtil.allGranted(grantResults);
        permissionsResultCallback.onRequestPermissionsResult(allGranted);
    }
}

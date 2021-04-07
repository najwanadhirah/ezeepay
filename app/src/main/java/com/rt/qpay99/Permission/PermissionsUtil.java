package com.rt.qpay99.Permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rt.qpay99.activity.ui.ForgotPasswordUI;

import java.util.ArrayList;
import java.util.List;
/**
 * Utility class for permissions.
 */
public class PermissionsUtil {


    /**
     * Returns the list of permissions not granted from the given list of permissions.
     * @param context Context
     * @param permissions list of permissions to check.
     * @return the list of permissions that do not have permission to use.
     */

    public static final int REQUEST_PERMISSION_MULTIPLE = 0;
    public static final int REQUEST_PERMISSION_CAMERA = 1;
    public static final int REQUEST_PERMISSION_LOCATION = 2;
    public static final int REQUEST_WRITE_EXTERNAL = 3;
    public static final int REQUEST_READ_PHONE_STATE = 4;

    public static List<String> getDeniedPermissions(Context context,
                                                    String... permissions) {
        final List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }
    /**
     * Uses the given activity and requests the user for permissions.
     * @param activity activity to use.
     * @param requestCode request code/id to use.
     * @param permissions String array of permissions that needs to be requested.
     */
    public static void requestPermissions(Activity activity, int requestCode,
                                          String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
    /**
     * Checks if all the permissions are granted.
     */
    public static boolean allGranted(@NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    /**
     * Queries if al the permissions are granted for the given permission strings.
     */
    public static boolean checkAllPermissionsGranted(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            // For all pre-M devices, we should have all the premissions granted on install.
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkAndRequestPermissions(Activity activity) {
        System.out.println("PermissionsUtils checkAndRequestPermissions()");


        int permissionContact = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        int permissionPhone = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        int permissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int permissionLocation = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionWriteExternal = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Permission List
        List<String> listPermissionsNeeded = new ArrayList<>();

        // Camera Permission
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Toast.makeText(activity, "Camera Permission is required for this app to run", Toast.LENGTH_SHORT)
                        .show();
            }
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        // Read/Write Permission
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // Location Permission
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (permissionPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissionContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }



        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_PERMISSION_MULTIPLE);
            return false;
        }

        return true;
    }
}

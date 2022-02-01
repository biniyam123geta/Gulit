package com.biniyamshopping.shop.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {


    /**
     * Check Permissions
     *
     * @param context     Context
     * @param permissions Permissions
     * @return Required Permissions
     */
    public static List<String> checkPermissions(Context context, String[] permissions) {
        List<String> permissionStatus = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionStatus.add(permission);
            }
        }

        return permissionStatus;
    }

    /**
     * Get All Required Permissions
     *
     * @return Permissions List
     */
    public static String[] getAllRequiredPermissions() {
        return new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,

        };
    }
}
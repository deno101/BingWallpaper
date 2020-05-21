package com.dnz.inc.bingwallpaper.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    public static final int WRITE_STORAGE_PERMISSION = 102;
    private static final String TAG = "Permissions";

    public static void getPermissions(Activity context, String s) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(context, s)) {
            // TODO: 5/21/20 Show dialog
            Log.d(TAG, "getPermissions: permission denied");
        }
        {
            ActivityCompat.requestPermissions(context, new String[]{s}, WRITE_STORAGE_PERMISSION);
        }


    }

    public static boolean checkPermission(String s, Activity context) {
        return ContextCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_GRANTED;
    }

    public static void getStoragePermissions(Activity context) {
        getPermissions(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean checkStoragePermission(Activity context){
        return checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context);
    }
}

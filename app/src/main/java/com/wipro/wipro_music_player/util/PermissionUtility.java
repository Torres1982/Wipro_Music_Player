package com.wipro.wipro_music_player.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class PermissionUtility {
    private final static String PERMISSION_TAG = "PERMISSION_TAG";
    private final static String PERMISSION_MSG = "Storage Permission Accepted!";

    // Self-check permissions
    @SuppressLint("ObsoleteSdkInt")
    public static void checkStoragePermissions(Context context, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showToastAndLogMessages(context);
            } else {
                ActivityCompat.requestPermissions(activity, new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                Log.i(PERMISSION_TAG,"Permission is Revoked!");
            }
        } else {
            showToastAndLogMessages(context);
        }
    }

    // Put Toast and Log messages together and reuse it across the block statements in check permission method
    private static void showToastAndLogMessages(Context context) {
        Toast.makeText(context, PERMISSION_MSG, Toast.LENGTH_SHORT).show();
        Log.i(PERMISSION_TAG, PERMISSION_MSG);
    }
}

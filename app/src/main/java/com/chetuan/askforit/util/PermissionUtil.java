package com.chetuan.askforit.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;

/**
 * Created by Administrator on 2016/1/4.
 */
public class PermissionUtil {

    public static void CheckPermission(final Activity activity, final String permission, final int code) {

        if (activity != null && permission != null) {

            if (shouldShowRequestPermissionRationale(activity, permission)) {
                showMessageOKCancel(activity, "You need to allow access to Phone State",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(activity, new String[]{permission}, code);
                            }
                        });
                return;
            }
            requestPermissions(activity, new String[]{permission}, code);
            return;
        }
    }

    private static void showMessageOKCancel(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}

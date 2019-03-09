package com.coffeehouse.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.coffeehouse.AppInstance;
import com.coffeehouse.view.dialog.NotifiDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by No1VietNam on 16/05/2017.
 */

public class MyPermission {
    public static final int PERMISSION_ACCESS_LIBRARY = 0;
    public static final int PERMISSION_RECORD_AUDIO = 1;
    public static final int PERMISSION_FINE_LOCATION = 2;
    public static final int PERMISSION_PHONE_AND_MMS = 3;
    public static final int PERMISSION_CALL_VIDEO = 4;
    public static final int PERMISSION_USE_CAMERA = 5;


    private static boolean isUnderAPI23() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    public static boolean checkHasPermission(String[] PERMISSIONS) {
        for (String PERMISSION : PERMISSIONS) {
            int check = ContextCompat.checkSelfPermission(AppInstance.getContext(), PERMISSION);
            if (check != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean onRequestResult(Context context, int[] grantResults, int idStringRes) {
        for (Integer grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                new NotifiDialog(context).notify(idStringRes);
                return false;
            }
        }
        return true;
    }

    /*public static boolean onRequestResult(Fragment fragment, int[] grantResults, int idStringRes, int requestCode) {
        for (Integer grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                NotifiDialog.newBuilder(fragment.getContext())
                        .setNotify(idStringRes)
                        .setCancelable(false)
                        .setButtonCancel(R.string.dong, (dialogInterface, i) -> dialogInterface.dismiss())
                        .setButtonOk(R.string.dong_y, (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            App.goToAppSettings(fragment, requestCode);
                        })
                        .build()
                        .show();
                return false;
            }
        }
        return true;
    }*/


    public static boolean requirePermission(Fragment fragment, final String[] PERMISSIONS, final int PERMISSIONS_REQUEST_ID) {
        if (isUnderAPI23())
            return true;

        List<String> pendingPermission = new ArrayList<>();

        Context context = fragment.getContext();
        for (String PERMISSION : PERMISSIONS) {
            int check = ContextCompat.checkSelfPermission(context, PERMISSION);
            Log.i(TAG, PERMISSION + ": " + (check != PackageManager.PERMISSION_GRANTED));
            if (check != PackageManager.PERMISSION_GRANTED) {
                pendingPermission.add(PERMISSION);
            }
        }

        int denyPermissionLength = pendingPermission.size();
        String[] denyPermission = new String[denyPermissionLength];
        for (int i = 0; i < denyPermissionLength; i++) {
            denyPermission[i] = pendingPermission.get(i);
        }

        if (denyPermissionLength > 0) {
            fragment.requestPermissions(denyPermission, PERMISSIONS_REQUEST_ID);
            return false;
        } else {
            return true;
        }
    }


}

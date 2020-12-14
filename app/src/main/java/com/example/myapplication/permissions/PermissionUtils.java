package com.example.myapplication.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PermissionUtils {

    private Activity current_activity;

    private PermissionResultCallback permissionResultCallback;
    private int PERMISSION_REQUEST_CODE = 22;


    private ArrayList<String> permission_list = new ArrayList<>();
    private ArrayList<String> listPermissionsNeeded = new ArrayList<>();

    public PermissionUtils(Context context) {
        this.current_activity = (Activity) context;
        permissionResultCallback = (PermissionResultCallback) context;
    }


    public void check_permission() {

        permission_list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permission_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permission_list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permission_list.add(Manifest.permission.READ_PHONE_STATE);
        permission_list.add(Manifest.permission.CALL_PHONE);
        permission_list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permission_list.add(Manifest.permission.BIND_INPUT_METHOD);
        permission_list.add(Manifest.permission.CAMERA);
        permission_list.add(Manifest.permission.BLUETOOTH);
        permission_list.add(Manifest.permission.BLUETOOTH_ADMIN);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions(permission_list, PERMISSION_REQUEST_CODE)) {
                permissionResultCallback.PermissionGranted(PERMISSION_REQUEST_CODE);
//                Log.i("all permissions", "granted");
//                Log.i("proceed", "to callback");
            }
        } else {
            permissionResultCallback.PermissionGranted(PERMISSION_REQUEST_CODE);
//            Log.i("all permissions", "granted");
//            Log.i("proceed", "to callback");
        }

    }

    private boolean checkAndRequestPermissions(ArrayList<String> permissions, int request_code) {

        if (permissions.size() > 0) {
            listPermissionsNeeded = new ArrayList<>();

            for (int i = 0; i < permissions.size(); i++) {
                int hasPermission = ContextCompat.checkSelfPermission(current_activity, permissions.get(i));

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions.get(i));
                }

            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(current_activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), request_code);
                return false;
            }
        }

        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    Map<String, Integer> perms = new HashMap<>();

                    for (int i = 0; i < permissions.length; i++) {
                        perms.put(permissions[i], grantResults[i]);
                    }

                    final ArrayList<String> pending_permissions = new ArrayList<>();

                    for (int i = 0; i < listPermissionsNeeded.size(); i++) {
                        if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(current_activity, listPermissionsNeeded.get(i)))
                                pending_permissions.add(listPermissionsNeeded.get(i));
                            else {
//                                Log.i("Go to settings", "and enable permissions");
                                permissionResultCallback.NeverAskAgain(PERMISSION_REQUEST_CODE);
//                                Toast.makeText(current_activity, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                    }

                    if (pending_permissions.size() > 0) {
                        showMessageOKCancel(
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                check_permission();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
//                                                Log.i("permisson", "not fully given");
                                                if (permission_list.size() == pending_permissions.size())
                                                    permissionResultCallback.PermissionDenied(PERMISSION_REQUEST_CODE);
                                                else
                                                    permissionResultCallback.PartialPermissionGranted(PERMISSION_REQUEST_CODE, pending_permissions);
                                                break;
                                        }
                                    }
                                });
                    } else {
//                        Log.i("all", "permissions granted");
//                        Log.i("proceed", "to next step");
                        permissionResultCallback.PermissionGranted(PERMISSION_REQUEST_CODE);
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(current_activity)
                .setMessage("App needs permissions")
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
}

package com.doors.myplacetourist.managers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.doors.myplacetourist.R;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

public class PermissionManager {
    public static final int REQUEST_CAMERA_PERMISSIONS = 0;
    private static final String[] APP_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Activity mContext;

    public PermissionManager(Context context){
        mContext = (Activity) context;
    }

    public boolean handleResult(int requestCode, @NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
            showMissingPermissionError();
            return false;
            }
        }
        return true;
    }



    public boolean hasAllPermissionsGranted() {
        for (String permission : APP_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(mContext, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestAppPermissions() {
        if (shouldShowRationale()) {
            PermissionManager.PermissionConfirmationDialog.newInstance();
        } else {
            ActivityCompat.requestPermissions(mContext, APP_PERMISSIONS, REQUEST_CAMERA_PERMISSIONS);
        }
    }

    private boolean shouldShowRationale() {
        for (String permission : APP_PERMISSIONS) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission)) {
                return true;
            }
        }
        return false;
    }

    private void showMissingPermissionError() {
        Activity activity = mContext;
        if (activity != null) {
            Toast.makeText(activity, R.string.request_permission, Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    public static class PermissionConfirmationDialog extends DialogFragment {

        public static PermissionConfirmationDialog newInstance() {
            return new PermissionConfirmationDialog();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->
                            ActivityCompat.requestPermissions(getActivity(), APP_PERMISSIONS,
                            REQUEST_CAMERA_PERMISSIONS))
                    .setNegativeButton(android.R.string.cancel,
                            (dialog, which) -> getActivity().finish())
                    .create();
        }

    }

}

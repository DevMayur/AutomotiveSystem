package com.mayur.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class ProgressUtils {

    public static final String TAG = "ProgressUtils";
    private static ProgressUtils instance;
    private static ProgressDialog progressDialog;
    private ProgressUtils(){

    }

    public static ProgressUtils getInstance(Context context) {
        instance = new ProgressUtils();
        progressDialog = new ProgressDialog(context);
        Log.d(TAG, "getInstance: Instance created");
        return instance;
    }

    public void showProgress(String title,String message) {
        Log.d(TAG, "showProgress: ");
        if (title != null) {
            progressDialog.setTitle(title);
        } else {
            progressDialog.setTitle("Please Wait");
        }
        if (message != null) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    public void hideProgress() {
        Log.d(TAG, "hideProgress: ");
        progressDialog.dismiss();
    }
}

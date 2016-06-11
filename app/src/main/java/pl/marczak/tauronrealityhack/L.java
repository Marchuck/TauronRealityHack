package pl.marczak.tauronrealityhack;

import android.util.Log;


public class L {

    public static final String TAG = "SAMSUNG";

    public static void i(String str) {
        if (Constants.DEBUG_LOG) {
            Log.i(TAG, str);
        }
    }

    public static void w(String str) {
        if (Constants.DEBUG_LOG) {
            Log.w(TAG, str);
        }
    }

    public static void d(String str) {
        if (Constants.DEBUG_LOG) {
            Log.d(TAG, str);
        }
    }

    public static void e(String str) {
        if (Constants.DEBUG_LOG) {
            Log.e(TAG, str);
        }
    }

    public static void e(String str, Exception e) {
        if (Constants.DEBUG_LOG) {
            if (e != null) {
                Log.e(TAG, str, e);
            }
        }
    }

    public static void wtf(String str, Exception e) {
        if (Constants.DEBUG_LOG) {
            if (e != null) {
                Log.wtf(TAG, str, e);
            }
        }
    }
}

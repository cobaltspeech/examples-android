package com.cubic.sdk.common;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cubic.example.BuildConfig;

public class Logger {

    private static final String TAG = "COM.CUBIC.SDK";

    public static void debug(String message) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, String.valueOf(message));
    }

    public static void exception(@NonNull Throwable t) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, String.valueOf(t.getMessage()));
    }
}

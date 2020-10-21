package com.cubic.example.message;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

public final class ToastManager implements IMessageManager {

    private final Context mContext;
    private Toast mToast;

    public ToastManager(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public void showMessage(@NonNull String message) {
        cancelToast();
        mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}

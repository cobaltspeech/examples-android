package com.cubic.example.dialog.model;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.cubic.example.R;

public class ConnectionDialog extends BaseDialogFragment {

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_connection;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = getDialogBuilder();
        return builder.create();
    }
}
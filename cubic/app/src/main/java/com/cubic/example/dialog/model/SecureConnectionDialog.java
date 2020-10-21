package com.cubic.example.dialog.model;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.cubic.example.R;
import com.cubic.example.SettingsActivity;

public class SecureConnectionDialog extends BaseDialogFragment {

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_secure_connection;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setTitleText(getString(R.string.app_secure_connection_title));
        setPositiveText(R.string.app_secure_connection_positive_button);
        setCancelable(false);
        return getDialogBuilder().create();
    }

    @Override
    public void onPositiveClickButton() {
        super.onPositiveClickButton();
        startActivity(new Intent(requireActivity(), SettingsActivity.class));
    }
}
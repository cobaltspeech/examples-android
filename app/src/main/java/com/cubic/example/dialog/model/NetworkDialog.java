package com.cubic.example.dialog.model;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.cubic.example.R;
import com.cubic.example.SettingsActivity;

public class NetworkDialog extends BaseDialogFragment {

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_network;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setTitleText(getString(R.string.app_network_dialog_title));
        setPositiveText(R.string.app_network_dialog_positive_button);
        setNegativeText(R.string.app_network_dialog_negative_button);
        setCancelable(false);
        return getDialogBuilder().create();
    }

    @Override
    public void onPositiveClickButton() {
        super.onPositiveClickButton();
        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }

    @Override
    public void onNegativeClickButton() {
        super.onNegativeClickButton();
        startActivity(new Intent(requireActivity(), SettingsActivity.class));
    }
}
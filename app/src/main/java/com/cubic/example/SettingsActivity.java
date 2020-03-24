package com.cubic.example;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.cubic.sdk.model.ConnectionConfiguration;
import com.cubic.sdk.preference.ConnectionPreferenceManager;
import com.cubic.sdk.preference.ConnectionPreferenceOptions;
import com.cubic.sdk.preference.IConnectionPreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    private IConnectionPreferenceManager mIConnectionPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);

        mIConnectionPreferenceManager = new ConnectionPreferenceManager(getApplicationContext());
        setupUI();
    }

    private void setupUI() {
        initHostView();
        initSecureView();
    }

    private void initSecureView() {
        Switch switchView = this.findViewById(R.id.secureView);
        ConnectionConfiguration connectionConfiguration = mIConnectionPreferenceManager.getConnectionConfiguration();
        switchView.setChecked(connectionConfiguration.isSecure());
        switchView.setOnCheckedChangeListener((compoundButton, b) -> mIConnectionPreferenceManager.saveSecure(b));
    }

    private void initHostView() {
        EditText hostView = this.findViewById(R.id.hostView);
        ConnectionConfiguration connectionConfiguration = mIConnectionPreferenceManager.getConnectionConfiguration();
        hostView.setText(connectionConfiguration.getHost());
        hostView.setHint(ConnectionPreferenceOptions.Defaults.HOST);
        hostView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIConnectionPreferenceManager.saveHost(s.toString());
            }
        });
    }
}

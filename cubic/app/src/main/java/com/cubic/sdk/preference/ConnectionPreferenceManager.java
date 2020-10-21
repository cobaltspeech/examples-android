package com.cubic.sdk.preference;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cubic.sdk.model.ConnectionConfiguration;
import com.cubic.sdk.preference.base.BasePrefsManager;

public class ConnectionPreferenceManager extends BasePrefsManager implements IConnectionPreferenceManager {

    private static final String SHARED_PREFERENCES_NAME = "CONNECTION_PREFERENCE";

    public ConnectionPreferenceManager(@NonNull Context context) {
        super(context, SHARED_PREFERENCES_NAME);
    }

    @Override
    public ConnectionConfiguration getConnectionConfiguration() {
        String host = getParam(ConnectionPreferenceOptions.Keys.HOST,
                ConnectionPreferenceOptions.Defaults.HOST);
        boolean isSecure = getParam(ConnectionPreferenceOptions.Keys.IS_SECURE_CONNECTION,
                ConnectionPreferenceOptions.Defaults.IS_SECURE_CONNECTION);
        return new ConnectionConfiguration(host, isSecure);
    }

    @Override
    public void saveHost(@NonNull String host) {
        setParam(ConnectionPreferenceOptions.Keys.HOST, host);
    }

    @Override
    public void saveSecure(boolean isSecure) {
        setParam(ConnectionPreferenceOptions.Keys.IS_SECURE_CONNECTION, isSecure);
    }
}

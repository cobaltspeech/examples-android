package com.cubic.sdk.preference;

import androidx.annotation.NonNull;

import com.cubic.sdk.model.ConnectionConfiguration;

public interface IConnectionPreferenceManager {

    ConnectionConfiguration getConnectionConfiguration();

    void saveHost(@NonNull String host);

    void saveSecure(boolean isSecure);
}

package com.cubic.sdk.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public final class ConnectionConfiguration {

    private final String mHost;
    private final boolean isSecure;

    public ConnectionConfiguration(@NonNull String host, boolean isSecure) {
        mHost = host;
        this.isSecure = isSecure;
    }

    public String getHost() {
        return mHost;
    }

    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionConfiguration that = (ConnectionConfiguration) o;
        return isSecure == that.isSecure &&
                TextUtils.equals(mHost, that.mHost);
    }

    @Override
    public int hashCode() {
        return mHost.hashCode() + Boolean.valueOf(isSecure).hashCode();
    }
}

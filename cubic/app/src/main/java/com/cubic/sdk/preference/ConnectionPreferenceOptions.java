package com.cubic.sdk.preference;

public class ConnectionPreferenceOptions {

    public interface Keys {
        String HOST = "HOST";
        String IS_SECURE_CONNECTION = "IS_SECURE_CONNECTION";
    }

    public interface Defaults {
        String HOST = "demo-cubic.cobaltspeech.com:2727";
        boolean IS_SECURE_CONNECTION = true;
    }
}

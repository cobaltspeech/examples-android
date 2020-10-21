package com.cubic.sdk;

import androidx.annotation.NonNull;

import com.cubic.sdk.model.CubicAudioConfiguration;

import java.util.List;

public interface OnCubicChangeListener {

    void onConnect();

    void onConnecting();

    void onDisconnect();

    void onGetAudioConfigurations(@NonNull List<CubicAudioConfiguration> configurations);

    void onText(@NonNull String text);

    void onError(@NonNull Throwable e);
}

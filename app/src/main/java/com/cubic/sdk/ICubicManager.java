package com.cubic.sdk;

import com.cubic.sdk.model.CubicAudioConfiguration;

import java.util.List;

public interface ICubicManager {

    void connect();

    void disconnect();

    void setAudioConfiguration(CubicAudioConfiguration configuration);

    List<CubicAudioConfiguration> getAudioConfigurations();

    void talk();

    void stopTalk();
}

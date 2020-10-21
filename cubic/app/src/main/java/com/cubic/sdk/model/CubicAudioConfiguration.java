package com.cubic.sdk.model;

import androidx.annotation.NonNull;

import cobaltspeech.cubic.CubicOuterClass;

public class CubicAudioConfiguration {

    private final CubicOuterClass.Model mConfiguration;

    public CubicAudioConfiguration(@NonNull CubicOuterClass.Model configuration) {
        mConfiguration = configuration;
    }

    public String getName() {
        return mConfiguration.getName();
    }

    public CubicOuterClass.Model getConfiguration() {
        return mConfiguration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof CubicAudioConfiguration)) return false;
        if (getClass() != o.getClass()) return false;
        CubicAudioConfiguration that = (CubicAudioConfiguration) o;
        return this.equals(that);
    }

    @Override
    public int hashCode() {
        return mConfiguration.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return mConfiguration.getName();
    }
}
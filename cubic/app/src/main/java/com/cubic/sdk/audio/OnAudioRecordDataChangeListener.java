package com.cubic.sdk.audio;

import androidx.annotation.NonNull;

public interface OnAudioRecordDataChangeListener {

    void onDataChange(@NonNull byte[] data);

    void onError(Exception e);
}
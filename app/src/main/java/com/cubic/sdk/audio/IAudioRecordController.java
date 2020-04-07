package com.cubic.sdk.audio;

import androidx.annotation.NonNull;

import com.cubic.sdk.audio.model.AudioRecordConfig;

public interface IAudioRecordController {

    void startRecord(@NonNull AudioRecordConfig config);

    void stopRecord();
}

package com.cubic.sdk.audio.model;

import android.media.AudioFormat;

import androidx.annotation.NonNull;

public class AudioRecordConfig {

    private int mEncoding;
    private int mChannel;
    private int mRate;
    private int mMinBufferSize;

    public AudioRecordConfig(int encoding, int channel, int rate, int minBufferSize) {
        mEncoding = encoding;
        mChannel = channel;
        mRate = rate;
        mMinBufferSize = minBufferSize;
    }

    public AudioRecordConfig(int rate) {
        this(
                AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.CHANNEL_IN_MONO,
                rate,
                8192
        );
    }

    public int getEncoding() {
        return mEncoding;
    }

    public int getChannel() {
        return mChannel;
    }

    public int getRate() {
        return mRate;
    }

    public int getMinBufferSize() {
        return mMinBufferSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudioRecordConfig that = (AudioRecordConfig) o;
        return mEncoding == that.mEncoding &&
                mChannel == that.mChannel &&
                mRate == that.mRate &&
                mMinBufferSize == that.mMinBufferSize;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(mEncoding).hashCode()
                + Integer.valueOf(mChannel).hashCode()
                + Integer.valueOf(mRate).hashCode()
                + Integer.valueOf(mMinBufferSize).hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "AudioRecordConfig{" +
                "mEncoding=" + mEncoding +
                ", mChannel=" + mChannel +
                ", mRate=" + mRate +
                ", mMinBufferSize=" + mMinBufferSize +
                '}';
    }
}

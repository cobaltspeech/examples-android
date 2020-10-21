package com.cubic.sdk.audio;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import androidx.annotation.NonNull;

import com.cubic.sdk.audio.model.AudioRecordConfig;

public final class AudioRecordController implements IAudioRecordController {

    private final OnAudioRecordDataChangeListener mOnAudioDataChangeListener;

    private AudioRecord mAudioRecord;
    private boolean isStartRecord;

    public AudioRecordController(@NonNull OnAudioRecordDataChangeListener listener) {
        mOnAudioDataChangeListener = listener;
    }

    @Override
    public void startRecord(@NonNull AudioRecordConfig config) {
        if (initAudioRecord(config)) {
            isStartRecord = true;
            mAudioRecord.startRecording();

            byte[] audio = new byte[config.getMinBufferSize()];
            int bufferSizeInShorts = config.getMinBufferSize() / 2;
            short[] audioShorts = new short[bufferSizeInShorts];

            while (isStartRecord) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mAudioRecord.read(
                            audio,
                            0,
                            config.getMinBufferSize(),
                            AudioRecord.READ_BLOCKING
                    );
                } else {
                    int cntRead = 0;
                    while (cntRead < bufferSizeInShorts && isStartRecord) {
                        int nRead = mAudioRecord.read(audioShorts, cntRead, bufferSizeInShorts - cntRead);
                        cntRead += nRead;
                    }
                    for (int i = 0; i < bufferSizeInShorts; i++) {
                        audio[(i * 2)] = (byte) (audioShorts[i] & 0xff);
                        audio[(i * 2) + 1] = (byte) ((audioShorts[i] >> 8) & 0xff);
                    }
                }

                mOnAudioDataChangeListener.onDataChange(audio);
            }
            mAudioRecord.stop();
            mAudioRecord.release();
        }
    }

    private boolean initAudioRecord(@NonNull AudioRecordConfig config) {
        int buffSize = AudioRecord.getMinBufferSize(
                config.getRate(),
                config.getChannel(),
                config.getEncoding()
        );

        if (buffSize == AudioRecord.ERROR) {
            mOnAudioDataChangeListener.onError(new Exception("getMinBufferSize returned ERROR"));
            return false;
        }

        if (buffSize == AudioRecord.ERROR_BAD_VALUE) {
            mOnAudioDataChangeListener.onError(new Exception("getMinBufferSize returned ERROR_BAD_VALUE"));
            return false;
        }

        mAudioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                config.getRate(),
                config.getChannel(),
                config.getEncoding(),
                config.getMinBufferSize()
        );

        int state = mAudioRecord.getState();
        if (state != AudioRecord.STATE_INITIALIZED) {
            mOnAudioDataChangeListener.onError(new Exception("state != AudioRecord.STATE_INITIALIZED"));
            return false;
        }
        return true;
    }

    @Override
    public void stopRecord() {
        isStartRecord = false;
    }
}

package com.cobaltspeech.diatheke;

import android.annotation.SuppressLint;
import android.util.Log;

import com.cobaltspeech.diatheke.DiathekeOuterClass.ASRInput;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ASRResult;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ActionData;
import com.cobaltspeech.diatheke.DiathekeOuterClass.CommandResult;
import com.cobaltspeech.diatheke.DiathekeOuterClass.Empty;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ListModelsResponse;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ModelInfo;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ReplyAction;
import com.cobaltspeech.diatheke.DiathekeOuterClass.SessionInput;
import com.cobaltspeech.diatheke.DiathekeOuterClass.SessionOutput;
import com.cobaltspeech.diatheke.DiathekeOuterClass.SessionStart;
import com.cobaltspeech.diatheke.DiathekeOuterClass.SetStory;
import com.cobaltspeech.diatheke.DiathekeOuterClass.TTSAudio;
import com.cobaltspeech.diatheke.DiathekeOuterClass.TextInput;
import com.cobaltspeech.diatheke.DiathekeOuterClass.TokenData;
import com.cobaltspeech.diatheke.DiathekeOuterClass.VersionResponse;
import com.google.protobuf.ByteString;
import com.theeasiestway.wavformat.WavFileBuilderJava;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class DiathekeClient {

    private ManagedChannel mDiathekeChannel;
    private DiathekeGrpc.DiathekeStub mDiathekeService;
    private DiathekeGrpc.DiathekeBlockingStub mDiathekeBlockingService;

    private TokenData mToken;

    public DiathekeClient() {
    }

    public void connect(String host, boolean isSecure) {
        ManagedChannelBuilder<?> builder = ManagedChannelBuilder
                .forTarget(host);
        if (!isSecure) {
            builder.usePlaintext();
        }

        mDiathekeChannel = builder.build();
        mDiathekeService = DiathekeGrpc.newStub(mDiathekeChannel);
        mDiathekeBlockingService = DiathekeGrpc.newBlockingStub(mDiathekeChannel);
    }

    public void disconnect() {
        if (mDiathekeChannel != null && !mDiathekeChannel.isShutdown()) {
            mDiathekeChannel.shutdownNow();
            mDiathekeChannel = null;
        }
        mDiathekeService = null;
        mDiathekeBlockingService = null;
    }


    public VersionResponse version() throws StatusRuntimeException {
        Empty e = Empty.newBuilder().build();
        return mDiathekeBlockingService.version(e);
    }

    public List<ModelInfo> listModels() throws StatusRuntimeException {
        Empty e = Empty.newBuilder().build();
        ListModelsResponse resp = mDiathekeBlockingService.listModels(e);
        return resp.getModelsList();
    }

    public List<ActionData> createSession(String modelId) throws StatusRuntimeException {
        SessionStart req = SessionStart.newBuilder().setModelId(modelId).build();
        SessionOutput resp = mDiathekeBlockingService.createSession(req);
        mToken = resp.getToken();
        return resp.getActionListList();
    }

    @SuppressLint("CheckResult")
    public void deleteSession() throws StatusRuntimeException {
        // The response is an Empty message, so there is nothing to check.
        //noinspection ResultOfMethodCallIgnored
        mDiathekeBlockingService.deleteSession(mToken);
    }

    public List<ActionData> sendText(String text) throws StatusRuntimeException {
        TextInput txtMsg = TextInput.newBuilder()
                .setText(text)
                .build();
        SessionInput input = SessionInput.newBuilder()
                .setToken(mToken)
                .setText(txtMsg)
                .build();
        SessionOutput resp = mDiathekeBlockingService.updateSession(input);
        mToken = resp.getToken();
        return resp.getActionListList();
    }

    public List<ActionData> sendASRResult(ASRResult asrResult) throws StatusRuntimeException {
        SessionInput input = SessionInput.newBuilder()
                .setToken(mToken)
                .setAsr(asrResult)
                .build();
        SessionOutput resp = mDiathekeBlockingService.updateSession(input);
        mToken = resp.getToken();
        return resp.getActionListList();
    }

    public List<ActionData> sendCommandResult(CommandResult commandResult) throws StatusRuntimeException {
        SessionInput input = SessionInput.newBuilder()
                .setToken(mToken)
                .setCmd(commandResult)
                .build();
        SessionOutput resp = mDiathekeBlockingService.updateSession(input);
        mToken = resp.getToken();
        return resp.getActionListList();
    }

    public List<ActionData> setStory(String storyId, Map<String, String> params) throws StatusRuntimeException {
        SetStory story = SetStory.newBuilder()
                .setStoryId(storyId)
                .putAllParameters(params)
                .build();
        SessionInput input = SessionInput.newBuilder()
                .setToken(mToken)
                .setStory(story)
                .build();
        SessionOutput resp = mDiathekeBlockingService.updateSession(input);
        mToken = resp.getToken();
        return resp.getActionListList();
    }

    public byte[] newTTSStream(ReplyAction reply) throws StatusRuntimeException, IOException {
        Iterator<TTSAudio> ttsResp = mDiathekeBlockingService.streamTTS(reply);

        // Collect the audio into a single array
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        while (ttsResp.hasNext()) {
            bb.write(ttsResp.next().getAudio().toByteArray());
        }

        // Create a wav file from it
        WavFileBuilderJava wb = new WavFileBuilderJava();
        wb.setBitsPerSample(WavFileBuilderJava.BITS_PER_SAMPLE_16);
        wb.setSampleRate(48000);
        wb.setNumChannels(WavFileBuilderJava.CHANNELS_MONO);
        wb.setSubChunk1Size(WavFileBuilderJava.SUBCHUNK_1_SIZE_PCM);
        wb.setAudioFormat(WavFileBuilderJava.PCM_AUDIO_FORMAT);

        return wb.build(bb.toByteArray());
    }

    public ASRResult newASRStream(Iterator<ByteString> audioBytes) throws InterruptedException, RuntimeException {

        final ASRResult[] results = {null};
        final Throwable[] failed = {null};

        final CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<ASRResult> responseObserver = new StreamObserver<ASRResult>() {
            @Override
            public void onNext(ASRResult result) {
                results[0] = result;
            }

            @Override
            public void onError(Throwable t) {
                failed[0] = t;
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };

        StreamObserver<ASRInput> requestObserver = mDiathekeService.streamASR(responseObserver);
        try {
            ASRInput.Builder inputBuilder = ASRInput.newBuilder();

            // Send token first
            ASRInput cfg = ASRInput.newBuilder()
                    .setToken(mToken)
                    .clearAudio()
                    .build();
            requestObserver.onNext(cfg);

            // TODO For each chunk of audio, send it to diatheke
            inputBuilder.clearToken().clearAudio();
            while (audioBytes.hasNext()) {
                ByteString bs = audioBytes.next();
                ASRInput msg = inputBuilder.clearAudio().setAudio(bs).build();
                requestObserver.onNext(msg);
            }
        } catch (RuntimeException e) {
            // Cancel RPC
            requestObserver.onError(e);
            throw e;
        }

        // Mark the end of requests
        requestObserver.onCompleted();

        // Receiving happens asynchronously
        if (!finishLatch.await(3, TimeUnit.MINUTES)) {
            throw new RuntimeException(
                    "Could not finish rpc within 3 minute, the server is likely down");
        }

        if (failed[0] != null) {
            throw new RuntimeException(failed[0]);
        }
        return results[0];
    }

}


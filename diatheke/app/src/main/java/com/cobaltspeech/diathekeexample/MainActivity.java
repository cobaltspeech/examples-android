package com.cobaltspeech.diathekeexample;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cobaltspeech.diatheke.DiathekeClient;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ActionData;
import com.cobaltspeech.diatheke.DiathekeOuterClass.CommandAction;
import com.cobaltspeech.diatheke.DiathekeOuterClass.CommandResult;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ModelInfo;
import com.cobaltspeech.diatheke.DiathekeOuterClass.ReplyAction;
import com.cobaltspeech.diatheke.DiathekeOuterClass.VersionResponse;
import com.cobaltspeech.diatheke.DiathekeOuterClass.WaitForUserAction;
import com.cobaltspeech.diathekeexample.model.Model;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private DiathekeClient mDiathekeClient;

    private TextInputEditText mEditTextURL, mEditTextUserText;
    private Button mBtnConnect, mBtnStartSession, mBtnSend;
    private RecyclerView mConversationView;
    private ConversationViewAdapter mConversationViewAdapter;
    private AppCompatSpinner mSpinnerModel;

    private ExecutorService mExecutorService;
    private Handler mMainThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupThreads();
        findViews();

        Model.getInstance(); // Initialize the singleton.
        setupModelListeners(); // Link the UI with the model.
        Model.getInstance().notifyObservers(); // Initialize the UI views.
        setOnClickListeners();
    }

    private void setupThreads() {
        mExecutorService = Executors.newFixedThreadPool(2);
        mMainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    }

    private void findViews() {
        mEditTextURL = findViewById(R.id.urlInput);
        mBtnConnect = findViewById(R.id.connectButton);

        mBtnStartSession = findViewById(R.id.startButton);
        mSpinnerModel = findViewById(R.id.modelSpinner);

        mConversationView = findViewById(R.id.conversationView);
        mEditTextUserText = findViewById(R.id.userText);
        mBtnSend = findViewById(R.id.sendButton);

        // The model Spinner needs a little more setup
        ArrayAdapter<String> mModelsViewAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[0]);
        mModelsViewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerModel.setAdapter(mModelsViewAdapter);

        // The conversation view needs just a little bit more setup
        mConversationView.setHasFixedSize(true);
        mConversationView.setLayoutManager(new LinearLayoutManager(this));
        mConversationViewAdapter = new ConversationViewAdapter();
        mConversationView.setAdapter(mConversationViewAdapter);
    }

    private void setupModelListeners() {
        Model model = Model.getInstance();

        // These are enable/disable listeners
        model.registerObserver(() -> mMainThreadHandler.post(() -> {
            mEditTextURL.setEnabled(model.getConnectionState() == Model.ConnectionState.DISCONNECTED);
            mBtnConnect.setEnabled(model.getConnectionState() != Model.ConnectionState.CONNECTING);
        }));
        model.registerObserver(() -> mMainThreadHandler.post(() -> {
            mSpinnerModel.setEnabled(model.getConnectionState() == Model.ConnectionState.CONNECTED);
            mBtnStartSession.setEnabled(
                    model.getCurModelIndex() != -1 &&
                            (model.getConnectionState() == Model.ConnectionState.CONNECTED ||
                                    model.getConnectionState() == Model.ConnectionState.IN_SESSION));
        }));
        model.registerObserver(() -> mMainThreadHandler.post(() -> {
            mConversationView.setEnabled(model.getConnectionState() == Model.ConnectionState.IN_SESSION);
            mEditTextUserText.setEnabled(model.getConnectionState() == Model.ConnectionState.IN_SESSION);
            mBtnSend.setEnabled(model.getConnectionState() == Model.ConnectionState.IN_SESSION);
        }));

        // Now we need to update the text
        model.registerObserver(() -> mMainThreadHandler.post(() -> {
            List<Model.ConversationEntry> conv = model.getConversation();
            mConversationViewAdapter.setConversation(conv);
            if (conv.size() > 1) {
                mConversationView.smoothScrollToPosition(conv.size() - 1); // Scroll to bottom;
            }

            // TODO I'm not sure I like this, but we are rebuilding the spinner's backing array
            List<ModelInfo> models = Model.getInstance().getModels();
            String[] arr = new String[models.size()];
            for (int i = 0; i < models.size(); i++) {
                ModelInfo m = models.get(i);
                arr[i] = String.format(Locale.US, "%s - %s", m.getId(), m.getName());
            }
            ArrayAdapter<String> mModelsViewAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    arr);
            mModelsViewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerModel.setAdapter(mModelsViewAdapter);
        }));

        model.registerObserver(() -> mMainThreadHandler.post(() -> {
            if (model.getConnectionState() == Model.ConnectionState.DISCONNECTED) {
                mBtnConnect.setText(R.string.txt_Connect);
            } else {
                mBtnConnect.setText(R.string.txt_Disconnect);
            }

            if (model.getConnectionState() == Model.ConnectionState.IN_SESSION) {
                mBtnStartSession.setText(R.string.txt_Stop);
            } else {
                mBtnStartSession.setText(R.string.txt_Start);
            }
        }));
    }

    private void setOnClickListeners() {
        mBtnConnect.setOnClickListener(view -> {
            if (mDiathekeClient == null) {
                String url = mEditTextURL.getText().toString();
                startServerConnection(url);
            } else {
                // We are disconnecting from the server
                mDiathekeClient.disconnect();
                mDiathekeClient = null;
                Model.getInstance().reset();
                Model.getInstance().setConnectionState(Model.ConnectionState.DISCONNECTED);
            }
        });

        mBtnStartSession.setOnClickListener(view -> {
            if (Model.getInstance().getConnectionState() != Model.ConnectionState.IN_SESSION) {
                this.startSession();
            } else {
                this.stopSession();
            }
        });

        mBtnSend.setOnClickListener(view -> sendCurrentUserText());
        mEditTextUserText.setOnKeyListener((view, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                sendCurrentUserText();
                return true;
            }
            return false;
        });

        mSpinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Model.getInstance().setCurModelIndex(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Model.getInstance().setCurModelIndex(-1);
            }
        });
    }

    private void sendCurrentUserText() {
        String txt = mEditTextUserText.getText().toString();
        mEditTextUserText.setText("");
        if (txt.trim().equals("")) {
            return;
        }

        mExecutorService.execute(() -> {
            List<ActionData> actionsToExecute = mDiathekeClient.sendText(txt);
            Model.getInstance().pushConversationEntry(new Model.ConversationEntry(Model.ConversationEntry.Source.USER, txt));
            executeActions(actionsToExecute);
        });
    }

    private void startServerConnection(String url) {
        Model.getInstance().setConnectionState(Model.ConnectionState.CONNECTING);
        mDiathekeClient = new DiathekeClient();
        mDiathekeClient.connect(url, false);

        mExecutorService.execute(() -> {
            VersionResponse version = mDiathekeClient.version();
            Model.getInstance().setVersion(version);

            List<ModelInfo> models = mDiathekeClient.listModels();
            Model.getInstance().setModels(models);

            Model.getInstance().setConnectionState(Model.ConnectionState.CONNECTED);
        });
    }

    private void startSession() {
        mExecutorService.execute(() -> {
            Model model = Model.getInstance();
            model.clearConversation();
            int curModelIndex = model.getCurModelIndex();

            if (curModelIndex == -1) {
                Log.i(TAG, "Can't use a curModelIndex of -1");
                return;
            }

            ModelInfo diathekeModel = model.getModels().get(curModelIndex);
            List<ActionData> actionsToExecute = mDiathekeClient.createSession(diathekeModel.getId());
            executeActions(actionsToExecute);
            Model.getInstance().setConnectionState(Model.ConnectionState.IN_SESSION);
        });
    }

    private void stopSession() {
        mExecutorService.execute(() -> {
            mDiathekeClient.deleteSession();
            Model.getInstance().setConnectionState(Model.ConnectionState.CONNECTED);
        });
    }

    private void executeActions(List<ActionData> actions) {
        Log.i(TAG, "Executing Actions (Cnt: " + actions.size() + ")");

        for (ActionData action : actions) {
            switch (action.getActionCase()) {
                case INPUT:
                    WaitForUserAction input = action.getInput();
                    Log.i(TAG, String.format(Locale.US, "Wait for user input (requires_wake_word: %b; immediate: %b)", input.getRequiresWakeWord(), input.getImmediate()));
                    break;

                case COMMAND:
                    CommandAction cmd = action.getCommand();
                    Log.i(TAG, "Command action received");

                    handleCommandRequest(cmd);
                    break;

                case REPLY:
                    ReplyAction reply = action.getReply();
                    Log.i(TAG, String.format(Locale.US, "Text Response (text: \"%s\"; luna_model: %s)", reply.getText(), reply.getLunaModel()));

                    handleReplyAction(reply);
                    break;

                case ACTION_NOT_SET:
                    Log.i(TAG, "ACTION_NOT_SET Response :/");
                    break;
            }
        }
    }

    private void handleCommandRequest(CommandAction cmd) {
        // This is where your business logic goes!

        // Log the incoming command and parameters.
        Log.i(TAG, String.format(Locale.US, "Command ID: %s", cmd.getId()));
        for (Map.Entry<String, String> entry : cmd.getInputParametersMap().entrySet()) {
            Log.i(TAG, String.format(Locale.US, "{%s: %s}", entry.getKey(), entry.getValue()));
        }

        // Each command should have a result sent.  We will just do a hardcoded that response.
        CommandResult cmdResult = CommandResult.newBuilder()
                .putOutParameters("Param1", "Value1")
                .putOutParameters("Param2", "Value2")
                .setId(cmd.getId())
                .setError("")
                .build();

        Log.i(TAG, "Sending command results now");
        List<ActionData> actionsToExecute = mDiathekeClient.sendCommandResult(cmdResult);
        executeActions(actionsToExecute);
    }

    private void handleReplyAction(ReplyAction reply) {
        // Add the text to the UI.
        Model.getInstance().pushConversationEntry(new Model.ConversationEntry(
                Model.ConversationEntry.Source.SERVER,
                reply.getText()));

        // Now we will request a call to TTS.
        try {
            // Blocks until all of the audio has returned.
            byte[] ttsAudio = mDiathekeClient.newTTSStream(reply);

            // Write the bytes to a temporary file.
            File outputFile = File.createTempFile("tts-", ".wav", getCacheDir());
            Log.d(TAG, "Writing TTS response to file " + outputFile.getPath());
            FileOutputStream writer = new FileOutputStream(outputFile);
            writer.write(ttsAudio);
            writer.close();

            // Now play that temporary file.
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(outputFile.getAbsolutePath());
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                mediaPlayer1.release();
                boolean success = outputFile.delete();
                Log.d(TAG, "Deleting TTS file " + outputFile.getPath() + " -- Success? " + success);
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
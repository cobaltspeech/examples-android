package com.cobaltspeech.diathekeexample.model;

import com.cobaltspeech.diatheke.DiathekeOuterClass;

import java.util.ArrayList;
import java.util.List;

public class Model implements IModel {
    /*
     * Setup a singleton pattern
     */
    private static Model mInstance;

    public static Model getInstance() {
        if (mInstance == null) {
            mInstance = new Model();
        }
        return mInstance;
    }

    private Model() {
        mObservers = new ArrayList<>();
        conversation = new ArrayList<>();
        models = new ArrayList<>();
        connectionState = ConnectionState.DISCONNECTED;
    }

    /*
     * Setup observer stuff
     */
    private ArrayList<IModelObserver> mObservers;

    @Override
    public void registerObserver(IModelObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    @Override
    public void removeObserver(IModelObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        for (IModelObserver o : mObservers) {
            o.onUserDataChanged();
        }
    }

    /*
     * Enums/Classes
     */
    public enum ConnectionState {DISCONNECTED, CONNECTING, CONNECTED, IN_SESSION}

    public static class ConversationEntry {
        public enum Source {USER, SERVER}

        public ConversationEntry(Source source, String text) {
            this.source = source;
            this.text = text;
        }
        public Source source;
        public String text;
    }

    /*
     * Application's Model data.
     */
    private ConnectionState connectionState;
    private DiathekeOuterClass.VersionResponse version;
    private List<DiathekeOuterClass.ModelInfo> models;
    private int curModelIndex;
    private List<ConversationEntry> conversation;

    /*
     * Setters/Getters
     */

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
        notifyObservers();
    }


    public DiathekeOuterClass.VersionResponse getVersion() {
        return version;
    }

    public void setVersion(DiathekeOuterClass.VersionResponse version) {
        this.version = version;
        notifyObservers();
    }


    public List<DiathekeOuterClass.ModelInfo> getModels() {
        return models;
    }

    public void setModels(List<DiathekeOuterClass.ModelInfo> models) {
        this.models.clear();
        this.models.addAll(models);
        notifyObservers();
    }


    public int getCurModelIndex() {
        return curModelIndex;
    }

    public void setCurModelIndex(int curModelIndex) {
        this.curModelIndex = curModelIndex;
        notifyObservers();
    }


    public List<ConversationEntry> getConversation() {
        return conversation;
    }

    public void setConversation(List<ConversationEntry> conversation) {
        this.conversation.clear();
        this.conversation.addAll(conversation);
        notifyObservers();
    }

    public void pushConversationEntry(ConversationEntry entry) {
        this.conversation.add(entry);
        notifyObservers();
    }

    public void clearConversation() {
        this.conversation.clear();
        notifyObservers();
    }


    public void reset() {
        this.version = null;
        this.models.clear();
        this.curModelIndex  = -1;
        this.conversation.clear();
    }

}

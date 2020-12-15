package com.cobaltspeech.diathekeexample;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cobaltspeech.diathekeexample.model.Model;

import java.util.ArrayList;
import java.util.List;

public class ConversationViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_SERVER = 1;

    public static class ViewHolderServerEntry extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public ViewHolderServerEntry(TextView v) {
            super(v);
            textView = v;
        }
    }

    public static class ViewHolderUserEntry extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public ViewHolderUserEntry(TextView v) {
            super(v);
            textView = v;
        }
    }

    private List<Model.ConversationEntry> conversation;

    public ConversationViewAdapter() {
        this.conversation = new ArrayList<>();
    }
    public ConversationViewAdapter(List<Model.ConversationEntry> conversation) {
        this.conversation = conversation;
    }

    public void addConversationEntry(Model.ConversationEntry entry) {
        this.conversation.add(entry);
    }

    public void setConversation(List<Model.ConversationEntry> newConversation) {
        this.conversation.clear();
        this.conversation.addAll(newConversation);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            default: // Use VIEW_TYPE_USER as default
            case VIEW_TYPE_USER:
                TextView vUser = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.conversation_user, parent, false);
                return new ViewHolderUserEntry(vUser);
            case 1:
                TextView vServer = (TextView) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.conversation_server, parent, false);
                return new ViewHolderServerEntry(vServer);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            default:
            case VIEW_TYPE_USER:
                ViewHolderUserEntry vhue = (ViewHolderUserEntry) holder;
                vhue.textView.setText(conversation.get(position).text);
                break;
            case VIEW_TYPE_SERVER:
                ViewHolderServerEntry vhse = (ViewHolderServerEntry) holder;
                vhse.textView.setText(conversation.get(position).text);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return conversation.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        switch (conversation.get(position).source) {
            default: // Use VIEW_TYPE_USER as default
            case USER:
                return VIEW_TYPE_USER;
            case SERVER:
                return VIEW_TYPE_SERVER;
        }
    }
}


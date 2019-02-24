package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;

import java.util.Arrays;

public class ConversationItem {

    private final String id;
    private final Conversation conversation;

    public ConversationItem(Conversation conversation) {
        this.id = conversation.getId();
        this.conversation = conversation;
    }

    public String getTitle() {
        return conversation.getTitle();
    }

    public String getId() {
        return id;
    }

    public ConversationType getType() {
        return conversation.getType();
    }

    public CharSequence getContent() {
        return "";
    }

    public long getTime() {
        return conversation.getLatestMessageTime();
    }

    public boolean isTop() {
        return conversation.isTop();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConversationItem) {
            return getId().equals(((ConversationItem) obj).getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{getId()});
    }


}

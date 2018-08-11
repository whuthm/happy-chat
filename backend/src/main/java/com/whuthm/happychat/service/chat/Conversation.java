package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.domain.model.ConversationType;
import com.whuthm.happychat.domain.model.Message;

import javax.validation.constraints.NotNull;
import java.util.Collection;

abstract class Conversation {

    private final String id;
    private final ConversationType conversationType;

    protected Conversation(@NotNull String id, @NotNull ConversationType conversationType) {
        this.id = id;
        this.conversationType = conversationType;
    }

    final String getId() {
        return id;
    }

    ConversationType getConversationType() {
        return conversationType;
    }

    abstract Collection<String> getTargetIds();

    void sendMessage(MessageManager messageManager, Message message) {
        for (String userId : getTargetIds()) {
            if (!userId.equals(message.getFrom())) {
                messageManager.sendMessage(message, userId);
            }
        }
    }

}

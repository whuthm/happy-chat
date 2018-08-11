package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.domain.model.ConversationType;

import java.util.Arrays;
import java.util.Collection;

class SingleChatConversation extends Conversation {

    private final Collection<String> targetIds;

    protected SingleChatConversation(String conversationId, String target1, String target2) {
        super(conversationId, ConversationType.SingleChat);
        this.targetIds = Arrays.asList(target1, target2);
    }

    @Override
    Collection<String> getTargetIds() {
        return targetIds;
    }
}

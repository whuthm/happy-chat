package com.whuthm.happychat.service.im;

import com.whuthm.happychat.domain.model.ConversationType;

import java.util.Arrays;
import java.util.Collection;

class PrivateChatConversation extends Conversation {

    private final Collection<String> targetIds;

    protected PrivateChatConversation(String conversationId, String target1, String target2) {
        super(conversationId, ConversationType.PRIVATE);
        this.targetIds = Arrays.asList(target1, target2);
    }

    @Override
    Collection<String> getTargetIds() {
        return targetIds;
    }
}

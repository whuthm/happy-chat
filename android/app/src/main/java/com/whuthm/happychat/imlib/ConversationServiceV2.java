package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Conversation;

import java.util.List;

public interface ConversationServiceV2 {

    Conversation getConversation(String conversationId);

    List<Conversation> getAllConversations();

}

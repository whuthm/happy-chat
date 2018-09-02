package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Conversation;

import java.util.List;

public interface ConversationService {

    Conversation getConversation(String conversationId);

    List<Conversation> getAllConversations();

}

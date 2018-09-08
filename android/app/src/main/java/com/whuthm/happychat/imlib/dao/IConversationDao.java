package com.whuthm.happychat.imlib.dao;

import com.whuthm.happychat.imlib.model.Conversation;

import java.util.List;

public interface IConversationDao {

    Conversation getConversation(String conversationId);

    List<Conversation> getAllConversations();

    void deleteConversation(String conversationId);

    void insertOrUpdateConversation(Conversation conversation);

}

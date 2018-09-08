package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Conversation;

import java.util.List;

import io.reactivex.Observable;

public interface ConversationService {

    Observable<Conversation> getConversation(String conversationId);

    Observable<List<Conversation>> getAllConversations();

}

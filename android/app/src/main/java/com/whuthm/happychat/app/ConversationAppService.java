package com.whuthm.happychat.app;

import com.whuthm.happychat.imlib.model.Conversation;

import java.util.List;

import io.reactivex.Observable;

public interface ConversationAppService {

    Observable<List<Conversation>> getAllConversations();

    void syncAllConversations();

    int getTotalUnreadCount();

}

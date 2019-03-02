package com.whuthm.happychat.app;

import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;

import io.reactivex.Observable;

public interface MessageAppService {

    Observable<Message> resendMessage(String messageUid);

    Observable<Message> sendTextMessage(String conversationId, ConversationType conversationType, String text);

}

package com.whuthm.happychat.imlib.db;

import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;

import java.util.List;

public interface IMessageDao {

    List<Message> getHistoryMessages(HistoryMessagesRequest request);

    Message getMessage(long id);

    Message getMessageByUid(String uid);

    void markMessagesOfConversationAsRead(String conversationId);

    void deleteMessage(long id);

    long insertMessage(Message message);

    void updateMessage(Message message);
}

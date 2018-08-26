package com.whuthm.happychat.imlib.db;

import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.LoadDataDirection;

import java.util.List;

public interface IMessageDao {

    List<Message> getHistoryMessages(String conversationId, long baseMessageId, LoadDataDirection direction, int count);

    Message getMessage(long id);

    Message getMessageByUid(String uid);

    void markMessagesOfConversationAsRead(String conversationId);

    void deleteMessage(long id);

    long insertMessage(Message message);

    void updateMessage(Message message);
}

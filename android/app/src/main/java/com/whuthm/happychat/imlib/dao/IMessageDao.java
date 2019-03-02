package com.whuthm.happychat.imlib.dao;

import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;

import java.util.List;

public interface IMessageDao {

    List<Message> getHistoryMessages(HistoryMessagesRequest request);

    Message getMessageByUid(String uid);

    Message getMessage(long id);

    Message getLatestMessage();

    void markMessagesOfConversationAsRead(String conversationId);

    void markMessageAsDeleted(long id);

    void insertMessage(Message message);

    void updateMessage(Message message);

    int getUnreadCountOf(String conversationId);

}
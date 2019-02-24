package com.whuthm.happychat.imlib.dao;

import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;

import java.util.List;

public interface IMessageDao {

    List<Message> getHistoryMessages(HistoryMessagesRequest request);

    Message getMessage(String id);

    void markMessagesOfConversationAsRead(String conversationId);

    void deleteMessage(String id);

    void insertMessage(Message message);

    void updateMessage(Message message);

}
package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.vo.LoadDataDirection;
import com.whuthm.happychat.imlib.model.Message;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;

class MessageManager extends AbstractChatContextImplService<MessageService> implements MessageService, MessageReceiver {

    private final Collection<MessageReceiver> messageReceivers;

    MessageManager(ChatContext chatContext) {
        super(chatContext);
        messageReceivers = new HashSet<>();
    }

    @Override
    public Observable<List<Message>> getHistoryMessages(String conversationId, long baseMessageId, LoadDataDirection direction, int count) {
        return null;
    }

    @Override
    public Observable<Message> sendMessage(Message message) {
        return null;
    }

    @Override
    public Observable<Message> resendMessage(Message message) {
        return null;
    }

    @Override
    public void onMessageReceive(Message message) {
        try {
            long messageId = getOpenHelper().getMessageDao().insertMessage(message);
            message.setId(messageId);
            // notify Message added;
            for (MessageReceiver messageReceiver : messageReceivers) {
                messageReceiver.onMessageReceive(message);
            }
        } catch (Exception ex) {
            Message messageByUid = getOpenHelper().getMessageDao().getMessageByUid(message.getUid());
            if (messageByUid != null) {
                message.setId(messageByUid.getId());
            } else {
                throw ex;
            }
        }
    }

    void addMessageReceiver(MessageReceiver messageReceiver) {
        messageReceivers.add(messageReceiver);
    }
}

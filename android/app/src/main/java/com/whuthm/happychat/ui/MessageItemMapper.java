package com.whuthm.happychat.ui;

import com.whuthm.happychat.common.util.Mapper;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Message;

class MessageItemMapper implements Mapper<Message, MessageItem> {

    private final IMContext imContext;

    MessageItemMapper(IMContext imContext) {
        this.imContext = imContext;
    }

    @Override
    public MessageItem transform(Message message) {
        MessageItem messageItem = new MessageItem(message.getUid());
        messageItem.setConversationId(message.getConversationId());
        messageItem.setConversationType(message.getConversationType());
        messageItem.setSenderId(message.getSenderUserId());
        messageItem.setSenderDisplayName(PresenterUtils.getMessageSenderDisplayName(imContext, message));
        messageItem.setSenderPortraitUrl(PresenterUtils.getMessageSenderPortraitUrl(imContext, message));
        messageItem.setSenderGender(PresenterUtils.getMessageSenderGender(imContext, message));
        messageItem.setDirection(message.getDirection());
        messageItem.setReceivedStatus(message.getReceivedStatus());
        messageItem.setSentStatus(message.getSentStatus());
        messageItem.setSendTime(message.getSendTime());
        messageItem.setBody(message.getBodyObject());
        return messageItem;
    }

}

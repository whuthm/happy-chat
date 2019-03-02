package com.whuthm.happychat.ui;

import com.whuthm.happychat.common.util.Mapper;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Conversation;

class ConversationItemMapper implements Mapper<Conversation, ConversationItem>{

    private final IMContext imContext;

    ConversationItemMapper(IMContext imContext) {
        this.imContext = imContext;
    }

    @Override
    public ConversationItem transform(Conversation conversation) {
        final ConversationItem conversationItem = new ConversationItem(conversation.getId(), conversation.getType());
        conversationItem.setTop(conversation.isTop());
        conversationItem.setLatestMessage(conversation.getLatestMessage());
        conversationItem.setLatestMessageTime(conversation.getLatestMessageTime());
        conversationItem.setUnreadCount(conversation.getUnreadCount());
        conversationItem.setNotificationStatus(conversation.getNotificationStatus());
        conversationItem.setTitle(PresenterUtils.getConversationTitle(imContext, conversation));
        conversationItem.setPortraitUrl(PresenterUtils.getConversationPortraitUrl(imContext, conversation));
        conversationItem.setContent(PresenterUtils.getConversationContent(imContext, conversation));
        conversationItem.setTargetGender(PresenterUtils.getConversationTargetGender(imContext, conversation));
        return conversationItem;
    }


}

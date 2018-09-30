package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.domain.model.ConversationType;
import com.whuthm.happychat.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
class ConversationManager {

    @Autowired
    GroupService groupService;

    private Map<String, Conversation> conversations = new ConcurrentHashMap<>();

    public Conversation getConversation(String from, String to, ConversationType conversationType) {

        if(conversationType == null){
            return null;
        }

        String conversationId;
        switch (conversationType) {
            case SingleChat:
                conversationId = StringUtils.sortToStringValue(from, to);
                break;
            case GroupChat:
                conversationId = to;
                break;
            default:
                return null;
        }
        Conversation conversation = conversations.get(conversationId);
        if (conversation == null) {
            switch (conversationType) {
                case GroupChat:
                    conversation = new GroupChatConversation(groupService, conversationId);
                    break;
                case SingleChat:
                    conversation = new SingleChatConversation(conversationId, from, to);
                    break;
            }
            conversations.put(conversationId, conversation);
        }
        return conversation;
    }


}

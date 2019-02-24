package com.whuthm.happychat.service.im;

import com.whuthm.happychat.domain.model.ConversationType;
import com.whuthm.happychat.exception.NotFoundException;
import com.whuthm.happychat.service.user.UserService;
import com.whuthm.happychat.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
class ConversationManager {

    @Autowired
    GroupService groupService;
    @Autowired
    UserService userService;

    private Map<String, Conversation> conversations = new ConcurrentHashMap<>();

    public Conversation getConversation(String from, String to, ConversationType conversationType) throws Exception {
        if (conversationType == null) {
            throw new NullPointerException("conversationType is null");
        }
        String conversationId = conversationType.isMultiUser() ? to : StringUtils.sortToStringValue(from, to);
        Conversation conversation = conversations.get(conversationId);
        if (conversation == null) {
            switch (conversationType) {
                case GROUP:
                    if (groupService.getGroup(conversationId) == null) {
                        throw new NotFoundException("Group Not found");
                    }
                    conversation = new GroupChatConversation(groupService, conversationId);
                    break;
                case PRIVATE:
                    if (userService.getUser(to) == null) {
                        throw new NotFoundException("User Not found");
                    }
                    conversation = new PrivateChatConversation(conversationId, from, to);
                    break;
                default:
                    throw new IllegalArgumentException("unsupported conversation type:" + conversationType.getValue());
            }
            conversations.put(conversationId, conversation);
        }
        return conversation;
    }


}

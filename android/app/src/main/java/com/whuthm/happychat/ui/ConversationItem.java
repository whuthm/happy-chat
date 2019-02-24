package com.whuthm.happychat.ui;

import com.whuthm.happychat.app.UserAppService;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.User;

import java.util.Arrays;

public class ConversationItem {

    private final String id;
    private final Conversation conversation;

    private final IMContext imContext;

    public ConversationItem(IMContext imContext, Conversation conversation) {
        this.id = conversation.getId();
        this.conversation = conversation;
        this.imContext = imContext;
    }

    public String getTitle() {
        if (conversation.getType() == ConversationType.PRIVATE) {
            User user = imContext.getService(UserAppService.class).getUser(conversation.getId());
            if (user != null) {
                return user.getName();
            }
        }
        return "";
    }

    public String getId() {
        return id;
    }

    public ConversationType getType() {
        return conversation.getType();
    }

    public CharSequence getContent() {
        return "";
    }

    public long getTime() {
        return conversation.getLatestMessageTime();
    }

    public boolean isTop() {
        return conversation.isTop();
    }

    public String getPortraitUrl() {
        if (conversation.getType() == ConversationType.PRIVATE) {
            User user = imContext.getService(UserAppService.class).getUser(conversation.getId());
            if (user != null) {
                return user.getPortraitUrl();
            }
        }
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConversationItem) {
            return getId().equals(((ConversationItem) obj).getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{getId()});
    }


}

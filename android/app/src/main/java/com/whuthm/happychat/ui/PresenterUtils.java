package com.whuthm.happychat.ui;

import android.text.TextUtils;

import com.whuthm.happychat.app.UserAppService;
import com.whuthm.happychat.common.util.ObjectUtils;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageBody;
import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.model.UserInfo;
import com.whuthm.happychat.imlib.model.message.TextMessageBody;

public class PresenterUtils {

    public static String getMessageSenderDisplayName(IMContext imContext, Message message) {
        if (message == null) {
            return null;
        }
        User user = imContext.getService(UserAppService.class).getUser(message.getSenderUserId());
        final String displayName = getUserDisplayName(user);
        if (!TextUtils.isEmpty(displayName)) {
            return displayName;
        }
        final UserInfo userInfo = message.getBodyObject() != null ? message.getBodyObject().getUserInfo() : null;
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        }
        return null;
    }

    public static String getMessageSenderPortraitUrl(IMContext imContext, Message message) {
        if (message == null) {
            return null;
        }
        User user = imContext.getService(UserAppService.class).getUser(message.getSenderUserId());
        if (user != null) {
            if (!TextUtils.isEmpty(user.getPortraitUrl())) {
                return user.getPortraitUrl();
            }
        }
        final UserInfo userInfo = message.getBodyObject() != null ? message.getBodyObject().getUserInfo() : null;
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getPortraitUrl())) {
            return userInfo.getPortraitUrl();
        }
        return null;
    }

    public static int getMessageSenderGender(IMContext imContext, Message message) {
        if (message == null) {
            return 0;
        }
        User user = imContext.getService(UserAppService.class).getUser(message.getSenderUserId());
        if (user != null) {
            return user.getGender();
        }
        final UserInfo userInfo = message.getBodyObject() != null ? message.getBodyObject().getUserInfo() : null;
        if (userInfo != null) {
            return userInfo.getGender();
        }
        return 0;
    }

    public static UserInfo getMessageSenderDisplayInfo(IMContext imContext, Message message) {
        if (message == null) {
            return new UserInfo();
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(message.getSenderUserId());
        userInfo.setPortraitUrl(getMessageSenderPortraitUrl(imContext, message));
        userInfo.setGender(getMessageSenderGender(imContext, message));
        userInfo.setName(getMessageSenderDisplayName(imContext, message));
        return userInfo;
    }

    public static String getUserDisplayName(User user) {
        if (user != null) {
            if (!TextUtils.isEmpty(user.getNick())) {
                return user.getNick();
            }
            if (!TextUtils.isEmpty(user.getName())) {
                return user.getName();
            }
        }
        return null;
    }

    public static boolean equals(UserInfo a, UserInfo b) {
        return (a == b) || (a != null
                && b != null
                && a.getGender() == b.getGender()
                && ObjectUtils.equals(a.getId(), b.getId())
                && ObjectUtils.equals(a.getName(), b.getName())
                && ObjectUtils.equals(a.getPortraitUrl(), b.getPortraitUrl()));
    }

    public static String getConversationContent(IMContext imContext, Conversation conversation) {
        // TODO
        final Message message = conversation.getLatestMessage();
        final MessageBody body = message != null ? message.getBodyObject() : null;
        final String senderId = message != null ? message.getSenderUserId() : null;
        String messageContent = null;
        if (body instanceof TextMessageBody) {
            messageContent = ((TextMessageBody) body).getText();
        }
        StringBuilder content = new StringBuilder();
        if (conversation.getType().isMultiUser() && !imContext.getConfiguration().getUserId().equals(senderId)) {
            final String senderDisplayName = getMessageSenderDisplayName(imContext, message);
            if (!TextUtils.isEmpty(senderDisplayName)) {
                content.append(senderDisplayName).append(": ");
            }
        }
        if (messageContent != null) {
            content.append(messageContent);
        }
        return content.toString();
    }

    public static String getConversationTitle(IMContext imContext, Conversation conversation) {
        if (conversation.getType() == ConversationType.PRIVATE) {
            User user = imContext.getService(UserAppService.class).getUser(conversation.getId());
            return getUserDisplayName(user);
        } else if (conversation.getType() == ConversationType.GROUP) {
            // TODO getGroupName
        }
        return null;
    }

    public static String getConversationPortraitUrl(IMContext imContext, Conversation conversation) {
        if (conversation.getType() == ConversationType.PRIVATE) {
            User user = imContext.getService(UserAppService.class).getUser(conversation.getId());
            return user != null ? user.getPortraitUrl() : null;
        } else if (conversation.getType() == ConversationType.GROUP) {
            // TODO getGroupName
        }
        return null;
    }

    public static int getConversationTargetGender(IMContext imContext, Conversation conversation) {
        if (conversation.getType() == ConversationType.PRIVATE) {
            User user = imContext.getService(UserAppService.class).getUser(conversation.getId());
            return user != null ? user.getGender() : 0;
        }
        return 0;
    }

}

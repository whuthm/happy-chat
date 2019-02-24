package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageBody;
import com.whuthm.happychat.imlib.model.UserInfo;

import java.util.Arrays;

public class MessageItem {

    private final String id;
    private final Message message;

    public MessageItem(Message message) {
        this.message = message;
        this.id = message.getId();
    }

    public String getSenderDisplayName() {
        final MessageBody messageBody = message.getBodyObject();
        final UserInfo userInfo = messageBody != null ? messageBody.getUserInfo() : null;
        return userInfo != null ? userInfo.getName() : null;
    }

    public MessageBody getBody() {
        return message.getBodyObject();
    }

    public String getSenderPortraitUrl() {
        final MessageBody messageBody = message.getBodyObject();
        final UserInfo userInfo = messageBody != null ? messageBody.getUserInfo() : null;
        return userInfo != null ? userInfo.getPortraitUrl() : null;
    }

    public int getSenderGender() {
        final MessageBody messageBody = message.getBodyObject();
        final UserInfo userInfo = messageBody != null ? messageBody.getUserInfo() : null;
        return userInfo != null ? userInfo.getGender() : 0;
    }

    public String getSenderUserId() {
        return message.getSenderUserId();
    }

    public String getConversationId() {
        return message.getConversationId();
    }

    public boolean isSendDirection() {
        return message.getDirection() == Message.Direction.SEND;
    }

    public boolean isSentFailed() {
        return message.getSentStatus() == Message.SentStatus.FAILED;
    }

    public boolean isSending() {
        return message.getSentStatus() == Message.SentStatus.SENDING;
    }

    public boolean isListened() {
        final Message.ReceivedStatus receivedStatus = message.getReceivedStatus();
        return receivedStatus != null && receivedStatus.isListened();
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageItem && getClass() == obj.getClass()) {
            return getId().equals(((MessageItem) obj).getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{getId()});
    }

}


package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;

import java.util.Arrays;

public class ConversationItem {

    private final String id;
    private final ConversationType type;
    private String title;
    private String portraitUrl;
    private int targetGender;
    private long latestMessageTime;
    private Message latestMessage;
    private int unreadCount;
    private boolean top;

    private CharSequence content;

    private String messageSenderDisplayName;
    private Conversation.NotificationStatus notificationStatus;

    public ConversationItem(String id, ConversationType type) {
        this.id = id;
        this.type = type;
    }

    public int getTargetGender() {
        return targetGender;
    }

    public void setTargetGender(int targetGender) {
        this.targetGender = targetGender;
    }

    public ConversationType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public long getLatestMessageTime() {
        return latestMessageTime;
    }

    public void setLatestMessageTime(long latestMessageTime) {
        this.latestMessageTime = latestMessageTime;
    }

    public Message getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(Message latestMessage) {
        this.latestMessage = latestMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public CharSequence getContent() {
        return content;
    }

    public void setContent(CharSequence content) {
        this.content = content;
    }

    public Conversation.NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Conversation.NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getMessageSenderDisplayName() {
        return messageSenderDisplayName;
    }

    public void setMessageSenderDisplayName(String messageSenderDisplayName) {
        this.messageSenderDisplayName = messageSenderDisplayName;
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

package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageBody;

import java.util.Arrays;

public class MessageItem {

    private final String uid;
    private String senderId;
    private String senderDisplayName;
    private String senderPortraitUrl;
    private int senderGender;
    private String conversationId;
    private ConversationType conversationType;
    private MessageBody body;
    private Message.ReceivedStatus receivedStatus;
    private Message.SentStatus sentStatus;
    private Message.Direction direction;
    private long sendTime;

    public MessageItem(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public void setSenderDisplayName(String senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
    }

    public String getSenderPortraitUrl() {
        return senderPortraitUrl;
    }

    public void setSenderPortraitUrl(String senderPortraitUrl) {
        this.senderPortraitUrl = senderPortraitUrl;
    }

    public int getSenderGender() {
        return senderGender;
    }

    public void setSenderGender(int senderGender) {
        this.senderGender = senderGender;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    public Message.ReceivedStatus getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(Message.ReceivedStatus receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public Message.SentStatus getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(Message.SentStatus sentStatus) {
        this.sentStatus = sentStatus;
    }

    public Message.Direction getDirection() {
        return direction;
    }

    public void setDirection(Message.Direction direction) {
        this.direction = direction;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MessageItem && getClass() == obj.getClass()) {
            return getUid().equals(((MessageItem) obj).getUid());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{getUid()});
    }

}


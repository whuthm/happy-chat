package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;

/**
 * Created by huangming on 2018/9/7.
 */

public class MessageUtils {

    public static MessageProtos.MessageBean getSendMessagePacketFrom(Message message) {
        return MessageProtos.MessageBean.newBuilder().setId(message.getUid())
                .setBody(message.getBody())
                .setConversationType(message.getConversationType().getValue())
                .setFrom(message.getSenderUserId())
                .setSendTime(message.getSendTime())
                .setType(message.getType())
                .setTo(message.getConversationId())
                .build();
    }

    public static Message getMessageFrom(MessageProtos.MessageBean messageBean, String currentUserId) throws Exception {
        Message message = new Message();
        message.setUid(messageBean.getId());
        message.setSid(messageBean.getSid());
        message.setType(messageBean.getType());
        message.setBody(messageBean.getBody());
        message.setSenderUserId(messageBean.getFrom());
        ConversationType conversationType = ConversationType.from(messageBean.getConversationType());

        if (conversationType == null) {
            throw new Exception("ConversationType(" + messageBean.getConversationType() + ") is unsupported");
        }
        if (conversationType.isMultiUser()) {
            message.setConversationId(messageBean.getTo());
        } else {
            if (messageBean.getFrom().equals(currentUserId)) {
                message.setConversationId(messageBean.getTo());
            } else if (messageBean.getTo().equals(currentUserId)) {
                message.setConversationId(messageBean.getFrom());
            } else {
                throw new Exception("Message is not belong to you");
            }
        }
        // PC发送给手机，Direction无法按照以下判断
        if (message.getSenderUserId().equals(currentUserId)) {
            message.setDirection(Message.Direction.SEND);
        } else {
            message.setDirection(Message.Direction.RECEIVE);
        }
        message.setAttrs(messageBean.getAttributes());
        message.setConversationType(conversationType);
        message.setReceiveTime(System.currentTimeMillis());
        message.setSendTime(messageBean.getSendTime());
        message.setExtra(messageBean.getExtra());

        return message;

    }

}

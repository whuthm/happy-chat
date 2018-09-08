package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.imlib.model.Message;

/**
 * Created by huangming on 2018/9/7.
 */

public class MessageUtils {

    public static  MessageProtos.MessageBean getMessagePacketFrom(Message message) {
        return MessageProtos.MessageBean.newBuilder()
                .setId(message.getUid())
                .setBody(message.getBody())
                .setConversationType(message.getConversationType())
                .setFrom(message.getFrom())
                .setTo(message.getTo())
                .setSendTime(message.getSendTime())
                .build();
    }

    public static Message getMessageFrom(MessageProtos.MessageBean messageBean, String currentUserId) {
        Message message = new Message();
        message.setUid(messageBean.getId());
        message.setSid(messageBean.getSid());
        message.setType(messageBean.getType());
        message.setBody(messageBean.getBody());
        message.setFrom(messageBean.getFrom());
        message.setTo(messageBean.getTo());
        message.setAttrs(messageBean.getAttributes());
        message.setConversationType(messageBean.getConversationType());
        message.setReceiveTime(System.currentTimeMillis());
        message.setSendTime(messageBean.getSendTime());
        message.setExtra(messageBean.getExtra());
        if (message.getFrom().equals(currentUserId)) {
            message.setDirection(Message.Direction.SEND);
        } else {
            message.setDirection(Message.Direction.RECEIVE);
        }
        return message;

    }

}

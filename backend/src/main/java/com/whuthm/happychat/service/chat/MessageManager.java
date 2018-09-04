package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.domain.model.ConversationType;
import com.whuthm.happychat.domain.model.MentionedInfo;
import com.whuthm.happychat.domain.model.Message;
import com.whuthm.happychat.domain.repository.MessageRepository;
import com.whuthm.happychat.service.connection.Connection;
import com.whuthm.happychat.service.handler.MessagePacketHandler;
import com.whuthm.happychat.util.PacketUtils;
import com.whuthm.happychat.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
class MessageManager implements MessagePacketHandler  {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ConversationManager conversationManager;
    @Autowired
    OfflineMessageManager offlineMessageManager;
    @Autowired
    ChatConnectionManager connectionManager;

    @Override
    public void handleMessagePacket(Connection connection, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception {
        //Validation.of(MessageProtos.MessageBean.class).requireValid(messageBean);
        final Conversation conversation = conversationManager.getConversation(
                messageBean.getFrom(),
                messageBean.getTo(),
                ConversationType.from(messageBean.getConversationType()));
        if (conversation != null) {
            Message message = new Message();
            message.setId(messageBean.getId());
            message.setSid(nextSortId());
            message.setFrom(messageBean.getFrom());
            message.setTo(messageBean.getTo());
            message.setType(messageBean.getType());
            message.setConversationType(messageBean.getConversationType());
            message.setBody(messageBean.getBody());
            long time = System.currentTimeMillis();
            message.setCreateTime(time);
            message.setUpdateTime(time);
            if (messageBean.getMentionedInfo() != null) {
                MessageProtos.MentionedInfoBean mentionedInfoBean = messageBean.getMentionedInfo();
                MentionedInfo mentionedInfo = new MentionedInfo();
                mentionedInfo.setType(mentionedInfoBean.getType());
                mentionedInfo.setContent(mentionedInfoBean.getContent());
                mentionedInfo.setUserIds(mentionedInfoBean.getUserIds());
                message.setMentionedInfo(mentionedInfo);
            }
            message.setExtra(messageBean.getExtra());
            message.setAttributes(messageBean.getAttributes());
            Message newMessage = messageRepository.save(message);

            connection.sendPacket(PacketUtils.createPacket(
                    PacketProtos.Packet.Type.iq,
                    PacketUtils.getMessageDeliveredIQ(IQProtos.MessageDeliveredIQ.newBuilder()
                            .setConversationId(newMessage.getTo())
                            .setId(newMessage.getId())
                            .setSid(newMessage.getSid())
                            .build())));

            conversation.sendMessage(this, newMessage);
        }
    }

    void sendMessage(Message message, String to) {
        final Collection<Connection> connections = connectionManager.getConnectionsOf(to);
        if (connections != null && connections.size() > 0) {
            for (Connection connection : connections) {
                MessageProtos.MentionedInfoBean mentionedInfoBean = null;
                MentionedInfo mentionedInfo = message.getMentionedInfo();
                if (mentionedInfo != null) {
                    mentionedInfoBean = MessageProtos.MentionedInfoBean.newBuilder()
                            .setType(mentionedInfo.getType())
                            .setContent(mentionedInfo.getContent())
                            .setUserIds(mentionedInfo.getUserIds())
                            .build();
                }
                MessageProtos.MessageBean messageBean = MessageProtos.MessageBean.newBuilder()
                        .setFrom(message.getFrom())
                        .setTo(message.getTo())
                        .setAttributes(message.getAttributes())
                        .setConversationType(message.getConversationType())
                        .setType(message.getType())
                        .setSendTime(message.getCreateTime())
                        .setBody(message.getBody())
                        .setExtra(message.getExtra())
                        .setId(message.getId())
                        .setSid(message.getSid())
                        .setMentionedInfo(mentionedInfoBean)
                        .build();
                try {
                    connection.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.message, messageBean));
                } catch (Exception e) {
                    e.printStackTrace();
                    // 发送失败重试，retry
                    // 重试失败，进入离线队列 enqueue offline
                    offlineMessageManager.enqueue(message, to);
                }
            }
        } else {
            // 发送对象未在线，进入利息那队列
            offlineMessageManager.enqueue(message, to);
        }
    }

    private void sendMessagePacket(Message message, MessageProtos.MessageBean messageBean, Connection connection) throws Exception {
        try {
            connection.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.message, messageBean));
        } catch (Exception ex) {
            throw ex;
        }

    }

    private long nextSortId() {
        return -1;
    }

}

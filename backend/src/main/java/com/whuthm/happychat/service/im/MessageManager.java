package com.whuthm.happychat.service.im;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.domain.model.ConversationType;
import com.whuthm.happychat.domain.model.MentionedInfo;
import com.whuthm.happychat.domain.model.Message;
import com.whuthm.happychat.domain.repository.MessageRepository;
import com.whuthm.happychat.util.PacketUtils;
import com.whuthm.happychat.validation.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
class MessageManager implements MessagePacketHandler {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ConversationManager conversationManager;
    @Autowired
    OfflineMessageManager offlineMessageManager;
    @Autowired
    ConnectionManager connectionManager;

    private volatile AtomicLong sortId;

    @Override
    public void handleMessagePacket(Connection connection, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception {
        try {
            Validations.requireValid(messageBean);
            final Conversation conversation = conversationManager.getConversation(
                    messageBean.getFrom(),
                    messageBean.getTo(),
                    ConversationType.from(messageBean.getConversationType()));
            Optional<Message> messageOptional = messageRepository.findById(messageBean.getId());
            Message persistedMessage = messageOptional.orElse(null);
            if (persistedMessage == null) {
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

                Validations.requireValid(message);
                persistedMessage = messageRepository.save(message);

                conversation.sendMessage(this, persistedMessage);
            }

            // 向客户端发送已送达IQ
            connection.sendPacket(PacketUtils.createPacket(
                    PacketProtos.Packet.Type.iq,
                    PacketUtils.getMessageDeliveredIQ(IQProtos.MessageDeliveredIQ.newBuilder()
                            .setConversationId(persistedMessage.getTo())
                            .setId(persistedMessage.getId())
                            .setSid(persistedMessage.getSid())
                            .build())));
        } catch (Exception ex) {
            // TODO message sent failed iq
        }
    }

    void sendMessage(Message message, String to) {
        final Collection<Connection> connections = connectionManager.getConnectionsOf(to);
        offlineMessageManager.enqueue(message, to);
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
                }
            }
        }
    }

    private long nextSortId() {
        if (sortId == null) {
            synchronized (MessageManager.class) {
                if (sortId == null) {
                    Page<Message> page = messageRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Order.desc("sid"))));
                    List<Message> messages = page.getContent();

                    final long maxSortId;
                    if (!messages.isEmpty()) {
                        maxSortId = messages.get(0).getSid();
                    } else {
                        maxSortId = -1;
                    }
                    sortId = new AtomicLong(maxSortId);
                }
            }
        }
        return sortId.incrementAndGet();
    }

}

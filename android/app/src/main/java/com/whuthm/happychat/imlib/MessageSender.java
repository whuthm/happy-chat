package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.util.PacketUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MessageSender {

    private final PacketSender packetSender;

    private Map<String, Message> sendingMessages;

    MessageSender(PacketSender packetSender) {
        this.packetSender = packetSender;
        this.sendingMessages = new ConcurrentHashMap<>();
    }

    void sendMessage(Message message) throws Exception {
        try {
            MessageProtos.MessageBean messageBean = MessageUtils.getSendMessagePacketFrom(message);
            this.sendingMessages.put(message.getUid(), message);
            packetSender.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.message, messageBean));
            synchronized (message) {
                if (this.sendingMessages.containsKey(message.getUid())) {
                    message.wait(8 * 1000);
                }
                if (message.getSid() != null) {
                    message.setSentStatus(Message.SentStatus.SENT);
                } else {
                    throw new Exception("Message sent Failed(" + message.getUid() + ")");
                }
            }
        } catch (Exception e){
            message.setSentStatus(Message.SentStatus.FAILED);
            throw  e;
        } finally {
            this.sendingMessages.remove(message.getUid());
        }
    }

    void performMessageDelivered(String uid, long sid) {
        final Message message = sendingMessages.get(uid);
        if (message != null) {
            synchronized (message) {
                message.setSid(sid);
                message.notifyAll();
            }
        }
    }

}

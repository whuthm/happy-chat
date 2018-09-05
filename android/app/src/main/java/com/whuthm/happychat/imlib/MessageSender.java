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

    public void sendMessage(Message message) throws Exception {
        try {
            MessageProtos.MessageBean messageBean = MessageProtos.MessageBean.newBuilder()
                    .setId(message.getUid())
                    .setBody(message.getBody())
                    .setConversationType(message.getConversationType())
                    .setFrom(message.getFrom())
                    .setTo(message.getTo())
                    .setSendTime(message.getSendTime())
                    .build();
            this.sendingMessages.put(message.getUid(), message);
            packetSender.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.message, messageBean));
            if (this.sendingMessages.containsKey(message.getUid())) {
                synchronized (message) {
                    wait(6 * 1000);
                }
            }
        } catch (Exception e){
            this.sendingMessages.remove(message.getUid());
            throw  e;
        }
    }
}

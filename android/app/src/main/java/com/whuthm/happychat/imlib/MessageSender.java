package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.util.PacketUtils;

class MessageSender {

    private final PacketSender packetSender;

    MessageSender(PacketSender packetSender) {
        this.packetSender = packetSender;
    }

    public void sendMessage(Message message) throws Exception {
        MessageProtos.MessageBean messageBean = MessageProtos.MessageBean.newBuilder()
                .setId(message.getUid())
                .setBody(message.getBody())
                .setConversationType(message.getConversationType())
                .setFrom(message.getFrom())
                .setTo(message.getTo())
                .setSendTime(message.getSendTime())
                .build();
        packetSender.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.message, messageBean));
    }
}

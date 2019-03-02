package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.util.PacketUtils;

public class MessagePacketReceiver implements MessagePacketHandler {

    private final String currentUserId;
    private final MessageReceiver messageReceiver;

    public MessagePacketReceiver(String currentUserId, MessageReceiver messageReceiver) {
        this.currentUserId = currentUserId;
        this.messageReceiver = messageReceiver;
    }

    @Override
    public void handleMessagePacket(PacketSender packetSender, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception {
        Message message = MessageUtils.getMessageFrom(messageBean, currentUserId);
        if (message != null &&  messageReceiver != null && messageReceiver.onReceive(message)) {
            // 需要发送已接收回执
            packetSender.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.iq,
                    PacketUtils.getMessageDeliveredACKIQ(IQProtos.MessageDeliveredACKIQ.newBuilder().setId(message.getUid()).build())));
        }
    }
}

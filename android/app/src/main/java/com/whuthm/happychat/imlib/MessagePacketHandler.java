package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;

public interface MessagePacketHandler {

    void handleMessagePacket(PacketSender packetSender, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception;
}

package com.whuthm.happychat.service.im;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;

public interface MessagePacketHandler {

    void handleMessagePacket(Connection connection, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception;

}

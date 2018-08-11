package com.whuthm.happychat.service.handler;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.connection.Connection;

public interface MessagePacketHandler {

    void handleMessagePacket(Connection connection, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception;

}

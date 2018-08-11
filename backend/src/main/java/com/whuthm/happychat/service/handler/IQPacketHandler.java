package com.whuthm.happychat.service.handler;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.connection.Connection;

public interface IQPacketHandler {

    void handlerIQPacket(Connection connection, PacketProtos.Packet packet, IQProtos.IQ iq);
}

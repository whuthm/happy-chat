package com.whuthm.happychat.service.im;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.PacketProtos;

public interface IQPacketHandler {

    void handlerIQPacket(Connection connection, PacketProtos.Packet packet, IQProtos.IQ iq);
}

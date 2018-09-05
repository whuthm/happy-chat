package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.PacketProtos;

public interface IQPacketHandler {

    void handlerIQPacket(PacketSender packetSender, PacketProtos.Packet packet, IQProtos.IQ iq) throws Exception;

}

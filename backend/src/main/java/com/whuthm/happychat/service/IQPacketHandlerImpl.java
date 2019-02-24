package com.whuthm.happychat.service;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.im.Connection;
import com.whuthm.happychat.service.im.IQPacketHandler;
import org.springframework.stereotype.Component;

@Component
public class IQPacketHandlerImpl implements IQPacketHandler  {
    @Override
    public void handlerIQPacket(Connection connection, PacketProtos.Packet packet, IQProtos.IQ iq) {

    }
}

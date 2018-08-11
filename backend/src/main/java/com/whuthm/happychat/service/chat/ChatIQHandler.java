package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.connection.Connection;
import com.whuthm.happychat.service.handler.IQPacketHandler;
import org.springframework.stereotype.Component;

@Component
class ChatIQHandler implements IQPacketHandler {

    @Override
    public void handlerIQPacket(Connection connection, PacketProtos.Packet packet, IQProtos.IQ iq) {
    }
}

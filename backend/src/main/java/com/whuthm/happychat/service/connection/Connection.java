package com.whuthm.happychat.service.connection;

import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.vo.Identifier;

public interface Connection {
    void disconnect() throws Exception;

    void sendPacket(PacketProtos.Packet packet) throws Exception;

    boolean isConnected();

    Identifier getIdentifier();

}
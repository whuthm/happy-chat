package com.whuthm.happychat.service.connection;

import com.whuthm.happychat.data.PacketProtos;

public interface Connection {
    void disconnect() throws Exception;

    void sendPacket(PacketProtos.Packet packet) throws Exception;

    boolean isConnected();

}
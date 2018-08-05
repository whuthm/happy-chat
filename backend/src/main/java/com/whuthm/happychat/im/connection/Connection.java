package com.whuthm.happychat.im.connection;

import com.whuthm.happychat.data.PacketProtos;

public interface Connection {
    void disconnect() throws Exception;

    void sendPacket(PacketProtos.Packet packet) throws Exception;

    boolean isConnected();

    boolean isAuthenticated();
}
package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.PacketProtos;

interface PacketSender {

     void sendPacket(PacketProtos.Packet packet) throws Exception;

}

package com.whuthm.happychat.util;

import com.google.protobuf.MessageLite;
import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.PacketProtos;

public class PacketUtils {

    public static IQProtos.IQ getPingIQ() {
        return IQProtos.IQ.newBuilder()
                .setType(IQProtos.IQ.Type.get)
                .setAction(IQProtos.IQ.Action.ping)
                .build();
    }

    public static IQProtos.IQ getPongIQ() {
        return IQProtos.IQ.newBuilder()
                .setType(IQProtos.IQ.Type.result)
                .setAction(IQProtos.IQ.Action.pong)
                .build();
    }

    public static IQProtos.IQ getMessageDeliveredIQ(IQProtos.MessageDeliveredIQ messageDeliveredIQ) {
        return IQProtos.IQ.newBuilder()
                .setType(IQProtos.IQ.Type.result)
                .setAction(IQProtos.IQ.Action.messageDelivered)
                .setData(messageDeliveredIQ.toByteString())
                .build();
    }

    public static IQProtos.IQ getMessageReadIQ(IQProtos.MessageReadIQ messageReadIQ) {
        return IQProtos.IQ.newBuilder()
                .setType(IQProtos.IQ.Type.result)
                .setAction(IQProtos.IQ.Action.messageRead)
                .setData(messageReadIQ.toByteString())
                .build();
    }

    public static PacketProtos.Packet createPacket(PacketProtos.Packet.Type type, MessageLite messageLite) {
        return PacketProtos.Packet.newBuilder()
                .setId(PacketIdGenerator.nextId())
                .setData(messageLite.toByteString())
                .setType(type).build();
    }

}

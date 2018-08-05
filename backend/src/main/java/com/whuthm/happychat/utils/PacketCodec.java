package com.whuthm.happychat.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.whuthm.happychat.data.PacketProtos;

import javax.websocket.*;
import java.nio.ByteBuffer;

/**
 * Packet 编解码器
 */
public class PacketCodec implements Decoder.Binary<PacketProtos.Packet>, Encoder.Binary<PacketProtos.Packet> {

    @Override
    public PacketProtos.Packet decode(ByteBuffer byteBuffer) throws DecodeException {
        try {
            return PacketProtos.Packet.newBuilder().mergeFrom(byteBuffer.array()).build();
        } catch (InvalidProtocolBufferException e) {
            throw new DecodeException(byteBuffer, "decode error", e);
        }
    }

    @Override
    public ByteBuffer encode(PacketProtos.Packet object) throws EncodeException {
        try {
            return ByteBuffer.wrap(object.toByteArray());
        } catch (Exception e) {
            throw new EncodeException(object, "encode error", e);
        }
    }

    @Override
    public boolean willDecode(ByteBuffer bytes) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

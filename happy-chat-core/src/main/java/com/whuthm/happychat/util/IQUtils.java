package com.whuthm.happychat.util;

import com.whuthm.happychat.data.IQProtos;

public class IQUtils {

    private IQUtils() {
    }

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

}

package com.whuthm.happychat.im.chat;

import com.whuthm.happychat.im.connection.AbstractConnection;
import com.whuthm.happychat.utils.PacketCodec;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/chat",
        encoders = PacketCodec.class,
        decoders = PacketCodec.class
)
@Component
public class ChatConnection extends AbstractConnection {


}

package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.service.connection.AbstractConnection;
import com.whuthm.happychat.utils.PacketCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/chat",
        encoders = PacketCodec.class,
        decoders = PacketCodec.class
)
@Component
public class ChatConnection extends AbstractConnection {

    @Autowired
    ChatManager chatManager;

}

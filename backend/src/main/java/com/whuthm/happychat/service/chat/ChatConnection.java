package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.authentication.AuthenticationService;
import com.whuthm.happychat.service.connection.AbstractConnection;
import com.whuthm.happychat.service.connection.ConnectionCloseCodes;
import com.whuthm.happychat.service.handler.IQPacketHandler;
import com.whuthm.happychat.service.handler.MessagePacketHandler;
import com.whuthm.happychat.util.PacketUtils;
import com.whuthm.happychat.utils.PacketCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;

@ServerEndpoint(
        value = "/chat",
        encoders = PacketCodec.class,
        decoders = PacketCodec.class
)
@Component
public class ChatConnection extends AbstractConnection {

    @Autowired
    ChatConnectionManager chatConnectionManager;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    MessagePacketHandler messagePacketHandler;
    @Autowired
    IQPacketHandler iqPacketHandler;

    private String token;
    private String userId;

    @Override
    public void onOpen(Session session) {
        super.onOpen(session);
        if (requireAuthenticated()) return;
        performConnected(session);
    }

    @Override
    public void onPacket(Session session, PacketProtos.Packet packet) {
        super.onPacket(session, packet);
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        List<String> tokens = requestParameterMap.get("token");
        this.token = tokens != null && tokens.size() > 0 ? tokens.get(0) : null;
        this.userId = authenticationService.getUserIdByToken(token);
        if (requireAuthenticated()) return;
        try {
            switch (packet.getType()) {
                case iq:
                    handleReceivedIQ(packet, IQProtos.IQ.newBuilder().mergeFrom(packet.getData()).build());
                case message:
                    messagePacketHandler.handleMessagePacket(this, packet, MessageProtos.MessageBean.newBuilder().mergeFrom(packet.getData()).build());
                case push:
                default:
                    throw new Exception("Unknown packet type : " + packet.getType());
            }
        } catch (Exception e) {
            LOGGER.warn("onPacket", e);
        }
    }

    private void handleReceivedIQ(PacketProtos.Packet packet, IQProtos.IQ iq) throws Exception {
        switch (iq.getAction()) {
            case ping:
                sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.iq, PacketUtils.getPongIQ()));
                break;
            case UNRECOGNIZED:
            case pong:
                return;
        }
        iqPacketHandler.handlerIQPacket(this, packet, iq);

    }

    private boolean isAuthenticated() {
        return authenticationService.isTokenValid(token) && !StringUtils.isEmpty(userId);
    }

    private boolean requireAuthenticated() {
        if (!isAuthenticated()) {
            try {
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private String getUserId() {
        return userId;
    }

    @Override
    protected void performConnected(Session webSocketSession) {
        super.performConnected(webSocketSession);
        chatConnectionManager.addConnection(getUserId(), this);
    }

    @Override
    protected void performDisconnected() {
        super.performDisconnected();
        chatConnectionManager.removeConnection(getUserId());
    }

    @Override
    protected CloseReason getCloseReason() {
        if (!isAuthenticated()) {
            return ConnectionCloseCodes.token_incorrect.getCloseReason();
        }
        return super.getCloseReason();
    }
}

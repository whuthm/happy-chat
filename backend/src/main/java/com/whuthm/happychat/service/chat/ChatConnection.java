package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.authentication.AuthenticationService;
import com.whuthm.happychat.service.connection.AbstractConnection;
import com.whuthm.happychat.service.connection.ConnectionCloseCodes;
import com.whuthm.happychat.service.handler.IQPacketHandler;
import com.whuthm.happychat.service.handler.MessagePacketHandler;
import com.whuthm.happychat.service.vo.Identifier;
import com.whuthm.happychat.util.PacketUtils;
import com.whuthm.happychat.utils.Constants;
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
        value = "/" + Constants.IDENTIFIER_DOMAIN_CHAT,
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

    String token;


    @Override
    public void onOpen(Session session) {
        super.onOpen(session);
        try {
            if (authenticateConnectionIdentifier()) {
                performConnected(session);
            } else {
                disconnect();
            }
        } catch (Exception ex) {
            LOGGER.error("onOpen", ex);
            try {
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private boolean authenticateConnectionIdentifier() throws Exception {
        Map<String, List<String>> requestParameterMap = getWebSocketSession().getRequestParameterMap();
        final List<String> tokens = requestParameterMap.get(Constants.HEADER_TOKEN);
        final List<String> userIds = requestParameterMap.get(Constants.HEADER_USER_ID);
        final List<String> clientResources = requestParameterMap.get(Constants.HEADER_CLIENT_RESOURCE);
        final String token = tokens != null && tokens.size() > 0 ? tokens.get(0) : null;
        final String userId = userIds != null && userIds.size() > 0 ? userIds.get(0) : null;
        final String clientResourceStringValue = clientResources != null && clientResources.size() > 0 ? clientResources.get(0) : null;
        ClientProtos.ClientResource clientResource = ClientProtos.ClientResource.valueOf(clientResourceStringValue);
        setToken(token);
        final Identifier identifier = Identifier.from(userId, Constants.IDENTIFIER_DOMAIN_AUTH, clientResource.name());
        setIdentifier(identifier);
        return isAuthenticated();
    }

    private String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    @Override
    public void onPacket(Session session, PacketProtos.Packet packet) {
        super.onPacket(session, packet);
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
        final String token = getToken();
        final Identifier identifier = getIdentifier();
        final ClientProtos.ClientResource resource = ClientProtos.ClientResource.valueOf(identifier.getResource());
        return !StringUtils.isEmpty(token) && token.equals(authenticationService.getToken(token, resource));
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

    @Override
    protected void performConnected(Session webSocketSession) {
        super.performConnected(webSocketSession);
        LOGGER.error("performConnected:" + getIdentifier());
        chatConnectionManager.addConnection(this);
    }

    @Override
    protected void performDisconnected() {
        super.performDisconnected();
        LOGGER.error("performDisconnected:" + getIdentifier());
        if (chatConnectionManager != null) {
            chatConnectionManager.removeConnection(this);
        }
    }

    @Override
    protected CloseReason getCloseReason() {
        if (!isAuthenticated()) {
            return ConnectionCloseCodes.token_incorrect.getCloseReason();
        }
        return super.getCloseReason();
    }

}

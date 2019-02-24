package com.whuthm.happychat.service.im;

import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.authentication.AuthenticationService;
import com.whuthm.happychat.service.vo.Identifier;
import com.whuthm.happychat.util.PacketUtils;
import com.whuthm.happychat.utils.Constants;
import com.whuthm.happychat.utils.PacketCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;

@ServerEndpoint(
        value = "/" + Constants.IDENTIFIER_DOMAIN_CHAT + "/{user_id}/{client_resource}",
        encoders = PacketCodec.class,
        decoders = PacketCodec.class,
        configurator = ConnectionConfigurator.class
)
@Component
public class Connection {

    ConnectionManager chatConnectionManager;
    AuthenticationService authenticationService;
    MessagePacketHandler messagePacketHandler;
    IQPacketHandler iqPacketHandler;

    private boolean connected;

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void setChatConnectionManager(ConnectionManager chatConnectionManager) {
        this.chatConnectionManager = chatConnectionManager;
    }

    public void setIqPacketHandler(IQPacketHandler iqPacketHandler) {
        this.iqPacketHandler = iqPacketHandler;
    }

    public void setMessagePacketHandler(MessagePacketHandler messagePacketHandler) {
        this.messagePacketHandler = messagePacketHandler;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Session webSocketSession;
    private Identifier identifier;

    private String token;

    @OnOpen
    public void onOpen(Session session, @PathParam("user_id") String userId, @PathParam("client_resource") String clientResourceStringValue) {
        // Get session and WebSocket connection
        LOGGER.error("onOpen: " + session.getId());
        webSocketSession = session;
        try {
            ClientProtos.ClientResource clientResource = ClientProtos.ClientResource.valueOf(clientResourceStringValue);
            final Identifier identifier = Identifier.from(userId, Constants.IDENTIFIER_DOMAIN_AUTH, clientResource.name());
            setIdentifier(identifier);
            if (isAuthenticated()) {
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

    @OnMessage
    public void onPacket(Session session, PacketProtos.Packet packet) {
        // Handle new messages
        LOGGER.error("onMessage: " + session.getId() + ", " + packet.getId());
        if (requireAuthenticated()) return;
        try {
            switch (packet.getType()) {
                case iq:
                    handleReceivedIQ(packet, IQProtos.IQ.newBuilder().mergeFrom(packet.getData()).build());
                    break;
                case message:
                    messagePacketHandler.handleMessagePacket(this, packet, MessageProtos.MessageBean.newBuilder().mergeFrom(packet.getData()).build());
                    break;
                case push:
                default:
                    throw new Exception("Unknown packet type : " + packet.getType());
            }
        } catch (Exception e) {
            LOGGER.warn("onPacket", e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        // WebSocket connection closes
        LOGGER.error("onClose: " + session.getId());
        performDisconnected();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        LOGGER.error("onError: " + session.getId(), throwable);
    }

    public synchronized void disconnect() throws Exception {
        final Session webSocketSession = getWebSocketSession();
        if (webSocketSession != null) {
            webSocketSession.close(getCloseReason());
            performDisconnected();
        }
    }

    public void sendPacket(PacketProtos.Packet packet) throws Exception {
        final Session webSocketSession = getWebSocketSession();
        if (webSocketSession != null) {
            webSocketSession.getAsyncRemote().sendBinary(ByteBuffer.wrap(packet.toByteArray()));
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    protected Session getWebSocketSession() {
        return webSocketSession;
    }

    private void setWebSocketSession(Session webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    protected synchronized void performConnected(Session webSocketSession) {
        setWebSocketSession(webSocketSession);
        setConnected(true);
        LOGGER.info("performConnected:" + getIdentifier());
        chatConnectionManager.addConnection(this);
    }

    protected synchronized void performDisconnected() {
        setWebSocketSession(null);
        if (isConnected()) {
            setConnected(false);
            LOGGER.info("performDisconnected:" + getIdentifier());
            if (chatConnectionManager != null) {
                chatConnectionManager.removeConnection(this);
            }
        }
    }

    void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
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

    private String getToken() {
        return token;
    }

    void setToken(String token) {
        this.token = token;
    }


    private boolean isAuthenticated() {
        final String token = getToken();
        final Identifier identifier = getIdentifier();final ClientProtos.ClientResource resource = ClientProtos.ClientResource.valueOf(identifier.getResource());
        return !StringUtils.isEmpty(token) && token.equals(authenticationService.getToken(identifier.getNode(), resource));
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


    protected CloseReason getCloseReason() {
        if (!isAuthenticated()) {
            return ConnectionCloseCodes.token_incorrect.getCloseReason();
        }
        return new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "close");
    }

}

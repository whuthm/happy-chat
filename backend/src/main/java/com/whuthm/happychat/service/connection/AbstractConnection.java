package com.whuthm.happychat.service.connection;

import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.service.vo.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.nio.ByteBuffer;

public abstract class AbstractConnection implements Connection {

    private boolean connected;

    public final boolean isConnected() {
        return this.connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Session webSocketSession;
    private Identifier identifier;

    @OnOpen
    public void onOpen(Session session) {
        // Get session and WebSocket connection
        LOGGER.info("onOpen: " + session.getId());
        webSocketSession = session;
    }

    @OnMessage
    public void onPacket(Session session, PacketProtos.Packet packet) {
        // Handle new messages
        LOGGER.info("onMessage: " + session.getId() + ", " + packet.getId());
    }

    @OnClose
    public  void onClose(Session session){
        // WebSocket connection closes
        LOGGER.info("onClose: " + session.getId());
        performDisconnected();
    }

    @OnError
    public  void onError(Session session, Throwable throwable) {
        // Do error handling here
        LOGGER.error("onError: " + session.getId(), throwable);
    }

    @Override
    public void disconnect()  throws Exception{
        final Session webSocketSession = getWebSocketSession();
        if (webSocketSession != null) {
            webSocketSession.close(getCloseReason());
            performDisconnected();
        }
    }

    @Override
    public void sendPacket(PacketProtos.Packet packet)  throws Exception {
        final Session webSocketSession = getWebSocketSession();
        if (webSocketSession != null) {
            webSocketSession.getAsyncRemote().sendBinary(ByteBuffer.wrap(packet.toByteArray()));
        }
    }

    protected Session getWebSocketSession() {
        return webSocketSession;
    }

    private void setWebSocketSession(Session webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    protected void performConnected(Session webSocketSession) {
        setWebSocketSession(webSocketSession);
        setConnected(true);
    }

    protected void performDisconnected() {
        setWebSocketSession(null);
        setConnected(false);
    }

    protected void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public final Identifier getIdentifier() {
        return identifier;
    }

    protected CloseReason getCloseReason() {
        return new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "close");
    }

}

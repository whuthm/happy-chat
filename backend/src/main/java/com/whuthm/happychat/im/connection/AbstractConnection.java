package com.whuthm.happychat.im.connection;

import com.whuthm.happychat.data.PacketProtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;

public abstract class AbstractConnection implements Connection {

    private boolean connected;
    private boolean authenticated;

    public final boolean isConnected() {
        return this.connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    public final boolean isAuthenticated() {
        return this.authenticated;
    }

    protected void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Session webSocketSession;

    @OnOpen
    public final void onOpen(Session session) {
        // Get session and WebSocket connection
        LOGGER.info("onOpen: " + session.getId());
        performConnected(session);
    }

    @OnMessage
    public final void onPacket(Session session, PacketProtos.Packet packet) {
        // Handle new messages
        LOGGER.info("onMessage: " + session.getId() + ", " + packet.getId());
    }

    @OnClose
    public final void onClose(Session session){
        // WebSocket connection closes
        LOGGER.info("onClose: " + session.getId());
        performDisconnected();
    }

    @OnError
    public final void onError(Session session, Throwable throwable) {
        // Do error handling here
        LOGGER.error("onError: " + session.getId(), throwable);
    }

    @Override
    public void disconnect()  throws Exception{
        final Session webSocketSession = getWebSocketSession();
        if (webSocketSession != null) {
            webSocketSession.close();
        }
    }

    @Override
    public void sendPacket(PacketProtos.Packet packet)  throws Exception {
        final Session webSocketSession = getWebSocketSession();
        if (webSocketSession != null) {
            webSocketSession.close();
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
        setAuthenticated(true);
    }
}

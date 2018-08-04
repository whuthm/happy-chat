package com.whuthm.happychat.service.chat;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/chat")
@Component
public class Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

    @OnOpen
    public void onOpen(Session session) {
        // Get session and WebSocket connection
        LOGGER.info("onOpen: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle new messages
        LOGGER.info("onMessage: " + session.getId() + ", message=" + message);
    }

    @OnClose
    public void onClose(Session session){
        // WebSocket connection closes
        LOGGER.info("onClose: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        LOGGER.warn("onError: " + session.getId(), throwable);
    }

}

package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.service.connection.Connection;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
class ChatConnectionManager {

    private Map<String, Connection> connectionsByUserId = new ConcurrentHashMap<>();

    void addConnection(String userId, Connection connection) {
        connectionsByUserId.put(userId, connection);
    }

    void removeConnection(String userId) {
        connectionsByUserId.remove(userId);
    }

    Connection getConnection(String userId) {
        return connectionsByUserId.get(userId);
    }

}

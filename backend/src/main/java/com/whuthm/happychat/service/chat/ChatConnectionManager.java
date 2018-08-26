package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.service.connection.Connection;
import com.whuthm.happychat.service.vo.Identifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
class ChatConnectionManager {

    private Map<String, Map<ClientProtos.ClientResource, Connection>> connections = new ConcurrentHashMap<>();

    void addConnection(Connection connection) {
        final Identifier identifier = connection.getIdentifier();
        final ClientProtos.ClientResource resource = ClientProtos.ClientResource.valueOf(identifier.getResource());
        final String userId = identifier.getNode();
        Map<ClientProtos.ClientResource, Connection> connectionsByClientResource = connections.get(userId);
        if (connectionsByClientResource == null) {
            connectionsByClientResource = new ConcurrentHashMap<>();
            connectionsByClientResource.put(resource, connection);
            connections.put(userId, connectionsByClientResource);
        } else {
            Connection oldConnection = connectionsByClientResource.get(resource);
            // todo
            connectionsByClientResource.remove(resource);
            connectionsByClientResource.put(resource, connection);

        }
    }

    void removeConnection(Connection connection) {
        final Identifier identifier = connection.getIdentifier();
        final ClientProtos.ClientResource resource = ClientProtos.ClientResource.valueOf(identifier.getResource());
        final String userId = identifier.getNode();
        Map<ClientProtos.ClientResource, Connection> connectionsByClientResource = connections.get(userId);
        if (connectionsByClientResource != null) {
            connectionsByClientResource.remove(resource);
        }
    }

    Connection getConnection(String userId, ClientProtos.ClientResource resource) {
        Map<ClientProtos.ClientResource, Connection> connectionsByClientResource = connections.get(userId);
        return connectionsByClientResource != null ? connectionsByClientResource.get(resource) : null;
    }

    Collection<Connection> getConnectionsOf(String userId) {
        Map<ClientProtos.ClientResource, Connection> connectionsByClientResource = connections.get(userId);
        return connectionsByClientResource!= null ? connectionsByClientResource.values() : null;
    }

}

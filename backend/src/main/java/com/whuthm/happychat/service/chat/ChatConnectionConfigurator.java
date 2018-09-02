package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.service.authentication.AuthenticationService;
import com.whuthm.happychat.service.vo.Identifier;
import com.whuthm.happychat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;

public class ChatConnectionConfigurator extends ServerEndpointConfig.Configurator {

    private String token;
    private Identifier identifier;

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        List<String> tokens = request.getHeaders().get(Constants.HEADER_TOKEN);

        final List<String> userIds = request.getParameterMap().get(Constants.HEADER_USER_ID);
        final List<String> clientResources = request.getParameterMap().get(Constants.HEADER_CLIENT_RESOURCE);
        final String token = tokens != null && tokens.size() > 0 ? tokens.get(0) : null;
        final String userId = userIds != null && userIds.size() > 0 ? userIds.get(0) : null;
        final String clientResourceStringValue = clientResources != null && clientResources.size() > 0 ? clientResources.get(0) : null;
        ClientProtos.ClientResource clientResource = ClientProtos.ClientResource.valueOf(clientResourceStringValue);
        this.token = token;
        this.identifier = Identifier.from(userId, Constants.IDENTIFIER_DOMAIN_AUTH, clientResource.name());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        ChatConnection chatConnection = (ChatConnection) super.getEndpointInstance(clazz);
        chatConnection.setToken(token);
        chatConnection.setIdentifier(identifier);
        return (T) chatConnection;
    }
}


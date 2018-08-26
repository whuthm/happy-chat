package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.domain.model.User;

public interface AuthenticationService {

    String getToken(String userId, ClientProtos.ClientResource clientResource);

    User login(String username, String password, ClientProtos.ClientResource clientResource) throws Exception;

    User register() throws Exception;

}

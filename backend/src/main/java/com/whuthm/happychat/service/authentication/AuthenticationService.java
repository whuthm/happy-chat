package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.domain.model.User;

public interface AuthenticationService {

    String getToken(String userId, ClientProtos.ClientResource clientResource);

    User login(AuthenticationProtos.LoginRequest loginRequest) throws Exception;

    User register(AuthenticationProtos.RegisterRequest registerRequest) throws Exception;

    String getOrCreateEmailValidationCode(String email) throws Exception;

}

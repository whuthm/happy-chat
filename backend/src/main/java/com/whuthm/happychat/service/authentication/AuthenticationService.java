package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.domain.model.User;

public interface AuthenticationService {

    boolean isAuthenticated(String userId);

    boolean isTokenValid(String token);

    String getUserIdByToken(String token);

    User login(String username, String password) throws Exception;

    User register() throws Exception;

}

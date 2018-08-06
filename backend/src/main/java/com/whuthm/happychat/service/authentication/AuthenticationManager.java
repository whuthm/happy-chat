package com.whuthm.happychat.service.authentication;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationManager implements AuthenticationService  {

    @Autowired
    TokenManager tokenManager;


    @Override
    public boolean isAuthenticated(boolean userId) {
        return false;
    }
}

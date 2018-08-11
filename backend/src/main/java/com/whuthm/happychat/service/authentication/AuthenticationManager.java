package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.exception.NotFoundException;
import com.whuthm.happychat.exception.ServerException;
import com.whuthm.happychat.service.user.UserService;
import com.whuthm.happychat.util.StringUtils;
import com.whuthm.happychat.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationManager implements AuthenticationService  {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    UserService userService;

    @Override
    public boolean isAuthenticated(String userId) {
        return tokenManager.checkUser(userId);
    }

    @Override
    public boolean isTokenValid(String token) {
        return tokenManager.checkToken(token);
    }

    @Override
    public String getUserIdByToken(String token) {
        return tokenManager.getUserIdByToken(token);
    }

    @Override
    public User login(String username, String password) throws Exception {
        User user = userService.getUser(username);
        if (user != null ) {
            if (!StringUtils.isEmpty(user.getPassword())
                    && user.getPassword().equals(AuthenticationUtils.encryptPassword(password, user.getSalt()))) {
                String token = tokenManager.createToken(user.getId());
                if (!StringUtils.isEmpty(token)) {
                    return user;
                } else {
                    throw new ServerException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public User register() throws Exception {
        return null;
    }

}

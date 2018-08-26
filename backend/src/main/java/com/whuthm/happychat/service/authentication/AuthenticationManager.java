package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.exception.NotFoundException;
import com.whuthm.happychat.exception.ServerException;
import com.whuthm.happychat.service.user.UserService;
import com.whuthm.happychat.service.vo.Identifier;
import com.whuthm.happychat.util.StringUtils;
import com.whuthm.happychat.utils.AuthenticationUtils;
import com.whuthm.happychat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationManager implements AuthenticationService  {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    UserService userService;

    @Override
    public String getToken(String userId, ClientProtos.ClientResource clientResource) {
        Identifier authIdentifier = Identifier.from(userId, Constants.IDENTIFIER_DOMAIN_AUTH, clientResource.name());
        return tokenManager.getToken(authIdentifier);
    }

    @Override
    public User login(String username, String password, ClientProtos.ClientResource clientResource) throws Exception {
        User user = userService.getUser(username);
        if (user != null ) {
            if (!StringUtils.isEmpty(user.getPassword())
                    && user.getPassword().equals(AuthenticationUtils.encryptPassword(password, user.getSalt()))) {
                Identifier authIdentifier = Identifier.from(user.getId(), Constants.IDENTIFIER_DOMAIN_AUTH, clientResource.name());
                String token = tokenManager.createToken(authIdentifier);
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

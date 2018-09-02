package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.data.AuthenticationProtos;
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

import java.util.UUID;

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
    public User login(AuthenticationProtos.LoginRequest loginRequest) throws Exception {
        ClientProtos.ClientResource clientResource = loginRequest.getClientResource();
        User user = userService.getUserByName(loginRequest.getUsername());
        if (user != null ) {
            if (!StringUtils.isEmpty(user.getPassword())
                    && user.getPassword().equals(loginRequest.getPassword())) {
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
    public User register(AuthenticationProtos.RegisterRequest registerRequest) throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(registerRequest.getUsername());
        user.setNick(registerRequest.getNick());
        user.setAvatar("http://pic3.40017.cn/nongjiale/2015/02/10/10/ZmLXp4.jpg");
        user.setGender(1);
        user.setPassword(registerRequest.getPassword());
        user.setRole(User.Role.Normal.getValue());
        user.setSalt("gouzi");
        User newUser = userService.addUser(user);
        return newUser;
    }

}

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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationManager implements AuthenticationService  {

    @Autowired
    TokenManager tokenManager;

    @Autowired
    UserService userService;

    @Autowired
    private StringRedisTemplate redis;

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
            final String encryptedPassword = AuthenticationUtils.encryptPassword(loginRequest.getPassword(), user.getSalt());
            if (!StringUtils.isEmpty(user.getPassword())
                    && user.getPassword().equals(encryptedPassword)) {
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
        if (!AuthenticationUtils.matchUsername(registerRequest.getUsername())) {
            new IllegalArgumentException("username is invalid");
        }
        if (!AuthenticationUtils.matchEmail(registerRequest.getEmail())) {
            new IllegalArgumentException("email is invalid");
        }

        if (!AuthenticationUtils.matchPassword(registerRequest.getPassword())) {
            new IllegalArgumentException("password is invalid");
        }

        if (StringUtils.isNullOrEmpty(registerRequest.getCode())) {
            new IllegalArgumentException("code is invalid");
        }

        if (!registerRequest.getCode().equals(getEmailCodeKey(registerRequest.getEmail()))) {
            new IllegalArgumentException("code is invalid");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPortraitUrl("http://pic3.40017.cn/nongjiale/2015/02/10/10/ZmLXp4.jpg");
        user.setGender(1);
        final String salt = StringUtils.randomString(4);
        user.setPassword(AuthenticationUtils.encryptPassword(registerRequest.getPassword(), salt));
        user.setSalt(salt);
        User newUser = userService.addUser(user);
        return newUser;
    }

    @Override
    public String getOrCreateEmailValidationCode(String email) throws Exception {
        final String key = getEmailCodeKey(email);
        String code = getEmailValidationCode(email);
        if (!StringUtils.isNullOrEmpty(code)) {
            return code;
        }
        code = AuthenticationUtils.getRandomCode();
        redis.boundValueOps(key).set(code);
        redis.expire(key, 120, TimeUnit.SECONDS);
        return code;
    }

    private String getEmailValidationCode(String email) throws Exception {
        if (!AuthenticationUtils.matchEmail(email)) {
            new IllegalArgumentException("email is invalid");
        }
        return redis.boundValueOps(getEmailCodeKey(email)).get();
    }

    private String getEmailCodeKey(String email) {
        return "evc:" + email;
    }

}

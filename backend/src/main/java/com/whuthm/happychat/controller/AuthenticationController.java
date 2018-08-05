package com.whuthm.happychat.controller;

import com.alibaba.fastjson.JSON;
import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.utils.ApiBaseResponses;
import com.whuthm.happychat.utils.AuthenticationUtils;
import com.whuthm.happychat.service.authentication.TokenManager;
import com.whuthm.happychat.service.authentication.Token;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

@RestController
@RequestMapping(consumes = {"application/x-protobuf"}, produces = {"application/x-protobuf"})
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenManager tokenManager;

    @Token(check = false)
    @RequestMapping(value = "/v1/auth/login", method = {RequestMethod.POST})
    AuthenticationProtos.LoginResponse login(@RequestBody AuthenticationProtos.LoginRequest request) throws NoSuchAlgorithmException {
        User user =  null;
        if (!StringUtils.isEmpty(request.getUsername()) /**&& AuthenticationUtils.matchUsername(request.getUsername())*/) {
            user = userRepository.findUserByName(request.getUsername());
        }
        Iterator<User> it = userRepository.findAll().iterator();
        while (it.hasNext()) {
            LOGGER.error(JSON.toJSONString(it.next()));
        }

        if (user != null ) {
            if (!StringUtils.isEmpty(user.getPassword())
                    && user.getPassword().equals(AuthenticationUtils.encryptPassword(request.getPassword(), user.getSalt()))) {
                String token = tokenManager.createToken(user.getId());
                LOGGER.info(request.getUsername() + " login token: " + token);
                return AuthenticationProtos.LoginResponse
                        .newBuilder()
                        .setUserId(user.getId())
                        .setToken(token)
                        .setResponse(ApiBaseResponses.SUCCESS.getResponse())
                        .build();
            } else {
                return AuthenticationProtos.LoginResponse
                        .newBuilder()
                        .setResponse(ApiBaseResponses.INCORRECT_PARAMETERS.getResponse())
                        .build();
            }
        } else {
            return AuthenticationProtos.LoginResponse
                    .newBuilder()
                    .setResponse(ApiBaseResponses.NOT_FOUND.getResponse())
                    .build();
        }

    }

}

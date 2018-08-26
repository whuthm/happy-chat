package com.whuthm.happychat.controller;

import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.exception.ServerException;
import com.whuthm.happychat.service.authentication.AuthenticationService;
import com.whuthm.happychat.utils.ApiBaseResponses;
import com.whuthm.happychat.utils.ApiUtils;
import com.whuthm.happychat.controller.util.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = {"application/x-protobuf"}, produces = {"application/x-protobuf"})
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    AuthenticationService authenticationService;

    @Token(check = false)
    @RequestMapping(value = "/v1/auth/login", method = {RequestMethod.POST})
    AuthenticationProtos.LoginResponse login(@RequestBody AuthenticationProtos.LoginRequest request) {

        try {
            final ClientProtos.ClientResource resource = request.getClientResource();
            User user = authenticationService.login(
                    request.getUsername(),
                    request.getPassword(),
                    resource);
            String token = authenticationService.getToken(user.getId(), resource);
            if (!StringUtils.isEmpty(token)) {
                return AuthenticationProtos.LoginResponse
                        .newBuilder()
                        .setUserId(user.getId())
                        .setToken(token)
                        .setResponse(ApiBaseResponses.SUCCESS.getResponse())
                        .build();
            } else {
                throw new ServerException();
            }
        } catch (Exception ex) {
            return AuthenticationProtos.LoginResponse
                    .newBuilder()
                    .setResponse(ApiUtils.getErrorResponse(ex))
                    .build();
        }

    }

}

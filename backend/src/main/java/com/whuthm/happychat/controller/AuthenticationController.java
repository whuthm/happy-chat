package com.whuthm.happychat.controller;

import com.alibaba.fastjson.JSON;
import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.exception.ServerException;
import com.whuthm.happychat.service.authentication.AuthenticationService;
import com.whuthm.happychat.service.email.EmailService;
import com.whuthm.happychat.utils.ApiBaseResponses;
import com.whuthm.happychat.utils.ApiUtils;
import com.whuthm.happychat.controller.token.Token;
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
    @Autowired
    EmailService emailService;

    @Token(check = false)
    @RequestMapping(value = "/v1/auth/emailvcode", method = {RequestMethod.POST})
    BaseProtos.BaseResponse getEmailValidationCode(@RequestBody BaseProtos.StringBean request) {
        LOGGER.info("getEmailValidationCode");
        try {
            String code = authenticationService.getOrCreateEmailValidationCode(request.getData());
            emailService.sendEmail(request.getData(), "Email", code);
            return ApiBaseResponses.SUCCESS.getResponse();
        } catch (Exception ex) {
            return ApiUtils.getErrorResponse(ex);
        }
    }

    @Token(check = false)
    @RequestMapping(value = "/v1/auth/login", method = {RequestMethod.POST})
    AuthenticationProtos.LoginResponse login(@RequestBody AuthenticationProtos.LoginRequest request) {

        LOGGER.info("login");
        try {
            final ClientProtos.ClientResource resource = request.getClientResource();
            User user = authenticationService.login(request);
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

    @Token(check = false)
    @RequestMapping(value = "/v1/auth/register", method = {RequestMethod.POST})
    BaseProtos.BaseResponse register(@RequestBody AuthenticationProtos.RegisterRequest request) {
        LOGGER.info("register");
        try {
            User user = authenticationService.register(request);
            LOGGER.info("register result:" + JSON.toJSONString(user));
            if (user != null && !StringUtils.isEmpty(user.getId())) {
                return ApiBaseResponses.SUCCESS.getResponse();
            } else {
                throw new ServerException();
            }
        } catch (Exception ex) {
            return ApiUtils.getErrorResponse(ex);
        }

    }

}

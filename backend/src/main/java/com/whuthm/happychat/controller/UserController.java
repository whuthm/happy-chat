package com.whuthm.happychat.controller;

import com.whuthm.happychat.data.UserProtos;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.domain.repository.UserRepository;
import com.whuthm.happychat.exception.NotFoundException;
import com.whuthm.happychat.service.user.UserService;
import com.whuthm.happychat.utils.ApiBaseResponses;
import com.whuthm.happychat.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whuthm.happychat.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(consumes = {"application/x-protobuf"}, produces = {"application/x-protobuf"})
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/v1/users/{id}")
    UserProtos.UserResponse getUser(@PathVariable String id) {
        try {
            User user = userService.getUser(id);
            if (user != null) {
                return UserProtos.UserResponse
                        .newBuilder()
                        .setResponse(ApiBaseResponses.SUCCESS.getResponse())
                        .setData(getUserBeanBy(user))
                        .build();
            } else {
                throw new NotFoundException();
            }
        } catch (Exception ex) {
            return UserProtos.UserResponse
                    .newBuilder()
                    .setResponse(ApiUtils.getErrorResponse(ex))
                    .build();
        }
    }


    @RequestMapping("/v1/users")
    UserProtos.UsersResponse getUsers(@RequestHeader(value = Constants.HEADER_USER_ID, required = true) String userId) {
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new IllegalArgumentException("header userId is empty");
            }
            List<User> users = userService.getUsers();
            List<UserProtos.UserBean> userBeans = new ArrayList<>();
            for (User user : users) {
                if (!user.getId().equals(userId)) {
                    userBeans.add(getUserBeanBy(user));
                }
            }
            return UserProtos.UsersResponse
                    .newBuilder()
                    .setResponse(ApiBaseResponses.SUCCESS.getResponse())
                    .addAllData(userBeans)
                    .build();
        } catch (Exception ex) {
            return UserProtos.UsersResponse
                    .newBuilder()
                    .setResponse(ApiUtils.getErrorResponse(ex))
                    .build();
        }
    }

    private static UserProtos.UserBean getUserBeanBy(User user) {
        UserProtos.UserBean.Builder builder = UserProtos.UserBean
                .newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setGender(user.getGender());
        if (user.getPortraitUrl() != null) {
            builder.setPortraitUrl(user.getPortraitUrl());
        }
        if (user.getNick() != null) {
            builder.setNick(user.getNick());
        }
        return builder.build();
    }

}

package com.whuthm.happychat.controller;

import com.whuthm.happychat.data.UserProtos;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.domain.repository.UserRepository;
import com.whuthm.happychat.exception.NotFoundException;
import com.whuthm.happychat.service.user.UserService;
import com.whuthm.happychat.utils.ApiBaseResponses;
import com.whuthm.happychat.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                        .setData(UserProtos.UserBean
                                .newBuilder()
                                .setId(user.getId())
                                .setAvatar(user.getAvatar())
                                .setGender(user.getGender())
                                .setName(user.getName())
                                .setNick(user.getNick())
                                .build())
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

}

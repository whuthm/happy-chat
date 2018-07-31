package com.whuthm.happychat.controller;

import com.whuthm.happychat.proto.api.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = {"application/x-protobuf"}, produces = {"application/x-protobuf"})
public class UserController {

    @RequestMapping("/v1/users/{id}")
    User.UserResponse getUser(@PathVariable String id) {
        return null;
    }

}

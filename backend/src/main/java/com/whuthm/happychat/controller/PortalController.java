package com.whuthm.happychat.controller;

import com.alibaba.fastjson.JSON;
import com.whuthm.happychat.utils.AuthenticationUtils;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
public class PortalController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    String index() {
        return "狗子们，嗨起来！";
    }

    @RequestMapping(value = "/create/{username}", method = {RequestMethod.GET})
    String test(@PathVariable("username") String username) throws NoSuchAlgorithmException {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(username);
        user.setAvatar("http://pic3.40017.cn/nongjiale/2015/02/10/10/ZmLXp4.jpg");
        user.setGender(1);
        user.setNick("狗子");
        String salt = "gouzi";
        user.setPassword(AuthenticationUtils.encryptPassword("123456", salt));
        user.setSalt(salt);
        user.setRole(User.Role.Normal.getValue());
        userRepository.save(user);
        return JSON.toJSONString(user);
    }

}

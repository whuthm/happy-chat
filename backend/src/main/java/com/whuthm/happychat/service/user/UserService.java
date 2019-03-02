package com.whuthm.happychat.service.user;

import com.whuthm.happychat.domain.model.User;

import java.util.List;

public interface UserService {

    User getUser(String userId);

    User getUserByName(String username);

    User addUser(User user);

    List<User> getUsers();

}

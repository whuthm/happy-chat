package com.whuthm.happychat.service.user;

import com.whuthm.happychat.domain.model.User;

public interface UserService {

    User getUser(String userId);

    User getUserByName(String username);

}
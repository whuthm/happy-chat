package com.whuthm.happychat.imlib.dao;

import com.whuthm.happychat.imlib.model.User;

import java.util.List;

public interface IUserDao {
    
    void addUsers(User... list);
    
    void addUsers(List<User> list);
    
    User getUsersById(String id);
    
    List<User> getUsersByIds(List<String> idList);
}

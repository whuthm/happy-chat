package com.whuthm.happychat.imlib.dao;

import com.whuthm.happychat.imlib.model.User;

import java.util.List;

public interface IUserDao {

    User getUser(String id);

    void deleteMessage(String id);

    InsertOrUpdateStatus insertOrUpdateMessage(User user);

}
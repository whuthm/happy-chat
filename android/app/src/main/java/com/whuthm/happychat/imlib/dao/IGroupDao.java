package com.whuthm.happychat.imlib.dao;

import com.whuthm.happychat.imlib.model.Group;

import java.util.List;

public interface IGroupDao {

    Group getGroup(String id);

    void deleteMessage(String id);

    InsertOrUpdateStatus insertOrUpdateMessage(Group group);

}

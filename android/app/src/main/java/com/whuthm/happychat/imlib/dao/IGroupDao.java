package com.whuthm.happychat.imlib.dao;

import com.whuthm.happychat.imlib.model.Group;

import java.util.List;

public interface IGroupDao {
    
    void addGroups(Group... list);
    
    void addGroups(List<Group> list);
    
    Group getGroupBy(String id);
}

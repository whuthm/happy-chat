package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.domain.model.Group;
import com.whuthm.happychat.domain.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
class GroupManager implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    private Map<String, Group> groups = new ConcurrentHashMap<>();

    @Override
    public Group getGroup(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            Optional<Group> groupOptional = groupRepository.findById(groupId);
            if (groupOptional.isPresent()) {
                group = groupOptional.get();
                groups.put(groupId, group);
            }
        }
        return group;
    }

}

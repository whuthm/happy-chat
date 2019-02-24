package com.whuthm.happychat.service.im;

import com.whuthm.happychat.domain.model.Group;
import com.whuthm.happychat.domain.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override
    public Group inviteIntoGroup(String groupId, String invitor, List<String> memberIds) throws Exception {
        return null;
    }

    @Override
    public Group quitGroup(String groupId, String who) throws Exception {
        return null;
    }

    @Override
    public Group applyIntoGroup(String groupId, String applicant) throws Exception {
        return null;
    }

    @Override
    public Group modifyGroupName(String groupId, String who, String groupName) {
        return null;
    }

    @Override
    public Group createGroup(String groupName, String groupType, String creator, List<String> memberIds) {
        return null;
    }

}

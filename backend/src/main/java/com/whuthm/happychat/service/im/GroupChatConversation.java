package com.whuthm.happychat.service.im;

import com.whuthm.happychat.domain.model.ConversationType;
import com.whuthm.happychat.domain.model.Group;
import com.whuthm.happychat.domain.model.GroupMember;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class GroupChatConversation extends Conversation {

    final GroupService groupService;

    protected GroupChatConversation(GroupService groupService, String groupId) {
        super(groupId, ConversationType.GROUP);
        this.groupService = groupService;
    }

    @Override
    Collection<String> getTargetIds() {
        Group group = groupService.getGroup(getId());
        List<GroupMember> groupMembers = group.getMembers();
        List<String> targetIds = new ArrayList<>(groupMembers != null ? groupMembers.size() : 0);
        if (groupMembers != null) {
            for (GroupMember groupMember : groupMembers) {
                targetIds.add(groupMember.getUserId());
            }
        }
        return targetIds;
    }
}

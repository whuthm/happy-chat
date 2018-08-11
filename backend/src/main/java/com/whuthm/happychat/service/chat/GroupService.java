package com.whuthm.happychat.service.chat;

import com.whuthm.happychat.domain.model.Group;


public interface GroupService {

    Group getGroup(String groupId);

//    Group inviteIntoGroup(String groupId, String invitor, List<String> memberIds) throws Exception;
//
//    Group quitGroup(String groupId, String who) throws Exception;
//
//    Group applyIntoGroup(String groupId, String applicant) throws Exception;
//
//    Group modifyGroupName(String groupId, String who, String groupName);
//
//    Group createGroup(String groupName, String groupType, String creator, List<String> memberIds);

}

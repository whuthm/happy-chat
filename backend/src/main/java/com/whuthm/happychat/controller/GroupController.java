package com.whuthm.happychat.controller;

import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.data.GroupProtos;
import com.whuthm.happychat.data.UserProtos;
import com.whuthm.happychat.domain.model.Group;
import com.whuthm.happychat.domain.model.User;
import com.whuthm.happychat.exception.NotFoundException;
import com.whuthm.happychat.service.im.GroupService;
import com.whuthm.happychat.utils.ApiBaseResponses;
import com.whuthm.happychat.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = {"application/x-protobuf"}, produces = {"application/x-protobuf"})
public class GroupController {

    @Autowired
    GroupService groupService;

    @RequestMapping(value = "/v1/group/create", method = {RequestMethod.POST})
    BaseProtos.BaseResponse create() {
        groupService.createGroup(null, null, null, null);
        return ApiBaseResponses.SUCCESS.getResponse();
    }

    @RequestMapping("/v1/group/apply")
    BaseProtos.BaseResponse apply(@PathVariable String id) {
        try {
            groupService.applyIntoGroup(id, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiBaseResponses.SERVER_ERROR.getResponse();
        }
        return ApiBaseResponses.SUCCESS.getResponse();
    }

    @RequestMapping("/v1/group/quit")
    BaseProtos.BaseResponse quite(@PathVariable String id) {
        try {
            groupService.quitGroup(id, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiBaseResponses.SERVER_ERROR.getResponse();
        }
        return ApiBaseResponses.SUCCESS.getResponse();
    }

    @RequestMapping("/v1/group/{id}")
    GroupProtos.GroupResponse getGroup(@PathVariable String id) {
        try {
            Group group = groupService.getGroup(id);
            if (group != null) {
                return GroupProtos.GroupResponse
                        .newBuilder()
                        .setResponse(ApiBaseResponses.SUCCESS.getResponse())
                        .setData(GroupProtos.GroupBean
                                .newBuilder()
                                .setId(group.getId())
                                .setName(group.getName())
                                .setCreator(group.getCreator())
                                .setDesc(group.getDescription())
                                .setType(group.getType())
                                .build())
                        .build();
            } else {
                throw new NotFoundException();
            }
        } catch (Exception ex) {
            return GroupProtos.GroupResponse
                    .newBuilder()
                    .setResponse(ApiUtils.getErrorResponse(ex))
                    .build();
        }
    }
}

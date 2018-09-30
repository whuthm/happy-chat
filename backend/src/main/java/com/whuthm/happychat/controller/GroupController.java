package com.whuthm.happychat.controller;

import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.service.chat.GroupService;
import com.whuthm.happychat.utils.ApiBaseResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = {"application/x-protobuf"}, produces = {"application/x-protobuf"})
public class GroupController {

    @Autowired
    GroupService groupService;

    @RequestMapping("/v1/group/create")
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
}

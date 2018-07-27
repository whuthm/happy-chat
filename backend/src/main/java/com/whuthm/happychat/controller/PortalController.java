package com.whuthm.happychat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortalController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    String index() {
        return "狗子们，嗨起来！";
    }
}

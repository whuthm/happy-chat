package com.whuthm.happychat.business.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

/**
 * 认证存根
 * 用于下次登录不需要校验，直接登录
 */
@Entity
public class AuthenticationStub {

    @Id
    private String userId;
    private String token;
    private String password;




}

package com.whuthm.happychat.app.model;

/**
 * 用户认证信息管理
 *
 * Created by tanwei on 2018/7/31.
 */

public class AuthenticationUser {

    private String userId;

    private String userToken;

    private String username;

    private String userNick;

    private String userAvatar;

    public AuthenticationUser() {
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

}

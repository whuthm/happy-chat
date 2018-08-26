package com.whuthm.happychat.imlib;

import com.whuthm.happychat.util.StringUtils;

import java.util.Arrays;

public class ChatConfiguration {

    private final String userId;
    private final String token;

    public ChatConfiguration(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    String getToken() {
        return token;
    }

    boolean isValid() {
        return !StringUtils.isEmpty(userId) && !StringUtils.isEmpty(token);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChatConfiguration) {
            ChatConfiguration o = (ChatConfiguration) obj;
            return userId.equals(o.userId) && token.equals(o.token);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{userId, token});
    }

    @Override
    public String toString() {
        return "ChatConfiguration(" + userId + ")";
    }

}

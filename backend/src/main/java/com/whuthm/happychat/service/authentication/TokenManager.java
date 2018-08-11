package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.utils.AuthenticationUtils;
import com.whuthm.happychat.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenManager {

    private StringRedisTemplate redis;

    @Autowired
    public void setRedis(StringRedisTemplate redis) {
        this.redis = redis;
    }

    String createToken(String userId) {
        //使用uuid作为源token
        String token = userId + "-" + UUID.randomUUID().toString().replace("-", "");
        redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return token;
    }


    void deleteToken(String token) {
        redis.delete(AuthenticationUtils.getUserIdFromToken(token));
    }

    boolean checkToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        final String userId = getUserIdByToken(token);
        return !StringUtils.isEmpty(userId) && token.equals(getToken(userId));
    }

    String getUserIdByToken(String token) {
        return AuthenticationUtils.getUserIdFromToken(token);
    }

    boolean checkUser(String userId) {
        return !StringUtils.isEmpty(userId) && !StringUtils.isEmpty(getToken(userId));
    }

    private String getToken(String userId) {
        return redis.boundValueOps(userId).get();
    }


}

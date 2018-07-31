package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.utils.Constants;
import com.whuthm.happychat.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTokenManager implements TokenManager {

    private StringRedisTemplate redis;

    @Autowired
    public void setRedis(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public String createToken(String userId) {
        //使用uuid作为源token
        String token = userId + "-"+ UUID.randomUUID().toString().replace("-", "");
        redis.boundValueOps(userId).set(token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return token;
    }

    @Override
    public void deleteToken(String token) {
        redis.delete(AuthenticationUtils.getUserIdFromToken(token));
    }

    @Override
    public boolean checkToken(String token) {
        if(StringUtils.isEmpty(token)) {
            return false;
        }

        final String userId = AuthenticationUtils.getUserIdFromToken(token);
        String tokenInRedis = redis.boundValueOps(userId).get();
        if (!token.equals(tokenInRedis)) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
        redis.boundValueOps(userId).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }
}

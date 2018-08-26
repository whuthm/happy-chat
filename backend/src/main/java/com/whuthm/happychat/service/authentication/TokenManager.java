package com.whuthm.happychat.service.authentication;

import com.whuthm.happychat.service.vo.Identifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class TokenManager {

    private StringRedisTemplate redis;

    @Autowired
    public void setRedis(StringRedisTemplate redis) {
        this.redis = redis;
    }

    String createToken(Identifier identifier) {
        //使用uuid作为源token
        String token = UUID.randomUUID().toString();
        redis.boundValueOps(token).set(identifier.toString());
        redis.boundValueOps(identifier.toString()).set(token);
        return token;
    }

    String getToken(Identifier identifier) {
        return redis.boundValueOps(identifier.toString()).get();
    }

}

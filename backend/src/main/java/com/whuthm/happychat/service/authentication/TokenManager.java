package com.whuthm.happychat.service.authentication;

public interface TokenManager {

    String createToken(String userId);

    void deleteToken(String token);

    boolean checkToken(String token);


}

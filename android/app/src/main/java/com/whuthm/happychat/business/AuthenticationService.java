package com.whuthm.happychat.business;

import com.whuthm.happychat.data.AuthenticationProtos;

import io.reactivex.Observable;

public interface AuthenticationService {
    /**
     * 登录成功后保存存根，并开始聊天连接
     * @param request
     * @return
     */
    Observable<String> login(AuthenticationProtos.LoginRequest request);
    Observable<String> register();

    Observable<String> logout();

    Observable<String> authenticate(String userId);
}

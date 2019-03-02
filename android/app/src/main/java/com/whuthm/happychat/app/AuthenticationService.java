package com.whuthm.happychat.app;

import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.data.AuthenticationProtos;

import io.reactivex.Observable;

public interface AuthenticationService {
    /**
     * 登录成功后保存存根，并开始聊天连接
     * @param request
     * @return
     */
    Observable<AuthenticationUser> login(AuthenticationProtos.LoginRequest request);

    Observable<AuthenticationUser> register(AuthenticationProtos.RegisterRequest request);

    Observable<AuthenticationUser> logout();

    AuthenticationUser getAuthenticationUser();

    AuthenticationStatus getAuthenticationStatus();

}

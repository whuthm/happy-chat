package com.whuthm.happychat.business;

import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.internal.context.AbstractApplicationService;
import com.whuthm.happychat.internal.context.ApplicationServiceContext;

import io.reactivex.Observable;

class AuthenticationManager extends AbstractApplicationService implements AuthenticationService {

    AuthenticationManager(ApplicationServiceContext applicationServiceContext) {
        super(applicationServiceContext);
    }

    @Override
    public Observable<String> login(AuthenticationProtos.LoginRequest request) {
        return null;
    }

    @Override
    public Observable<String> register() {
        return null;
    }

    @Override
    public Observable<String> authenticate(String userId) {
        return null;
    }
}

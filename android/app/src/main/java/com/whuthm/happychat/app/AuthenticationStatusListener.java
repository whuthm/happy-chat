package com.whuthm.happychat.app;

import com.whuthm.happychat.app.model.AuthenticationStatus;

public interface AuthenticationStatusListener {

    void onAuthenticationStatusChanged(AuthenticationStatus status);

}

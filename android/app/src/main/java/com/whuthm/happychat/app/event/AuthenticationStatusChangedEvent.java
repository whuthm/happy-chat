package com.whuthm.happychat.app.event;

import com.whuthm.happychat.app.model.AuthenticationStatus;

public class AuthenticationStatusChangedEvent {

    private final AuthenticationStatus status;

    public AuthenticationStatusChangedEvent(AuthenticationStatus status) {
        this.status = status;
    }

    public AuthenticationStatus getStatus() {
        return status;
    }
}

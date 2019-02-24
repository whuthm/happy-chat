package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.event.ConnectionEvent;
import com.whuthm.happychat.imlib.event.EventPoster;
import com.whuthm.happychat.imlib.event.EventPosterWrapper;
import com.whuthm.happychat.imlib.model.ConnectionStatus;

class ConnectionEventPoster extends EventPosterWrapper implements ConnectionEvent.Poster {

    public ConnectionEventPoster(EventPoster wrapped) {
        super(wrapped);
    }

    @Override
    public void postConnectionStatusChanged(ConnectionStatus connectionStatus) {
        post(new ConnectionEvent.StatusChangedEvent(connectionStatus));
    }
}

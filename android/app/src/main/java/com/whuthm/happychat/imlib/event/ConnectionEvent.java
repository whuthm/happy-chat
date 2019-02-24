package com.whuthm.happychat.imlib.event;

import com.whuthm.happychat.imlib.model.ConnectionStatus;

public class ConnectionEvent {

    public final static class StatusChangedEvent {
        private  final ConnectionStatus connectionStatus;

        public StatusChangedEvent(ConnectionStatus connectionStatus) {
            this.connectionStatus = connectionStatus;
        }

        public ConnectionStatus getConnectionStatus() {
            return connectionStatus;
        }
    }

    public interface Poster extends EventPoster {

        void postConnectionStatusChanged(ConnectionStatus connectionStatus);

    }

}

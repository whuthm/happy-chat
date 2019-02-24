package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.ConnectionStatus;

public interface ConnectionService {

    ConnectionStatus getConnectionStatus();

    void connect();

    void disconnect();

}

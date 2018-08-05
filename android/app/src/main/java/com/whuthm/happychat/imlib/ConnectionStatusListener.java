package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.ConnectionStatus;

public interface ConnectionStatusListener {

    void onConnectionStatusChanged(ConnectionStatus status);

}

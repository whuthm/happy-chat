package com.whuthm.happychat.imlib;

/**
 * Chat & Push
 */
public interface Connection {

    void connect() throws Exception;

    void disconnect() throws Exception;

    boolean isConnected();

}

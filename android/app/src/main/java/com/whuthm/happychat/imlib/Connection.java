package com.whuthm.happychat.imlib;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.whuthm.happychat.data.ClientProtos;
import com.whuthm.happychat.data.PacketProtos;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

class Connection {

    private final ConnectionManager connectionManager;

    private final OkHttpClient okHttpClient;

    private static final String TAG = Connection.class.getSimpleName();

    private WebSocket webSocket;

    Connection(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.okHttpClient = new OkHttpClient.Builder().build();
    }

    void connect() {
        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("ws")
                .append("://")
                .append(connectionManager.getImContext().getOptions().getImServer())
                .append(":")
                .append(connectionManager.getImContext().getOptions().getImPort())
                .append("/")
                .append("im")
                .append("/")
                .append(connectionManager.getImContext().getConfiguration().getUserId())
                .append("/")
                .append(ClientProtos.ClientResource.phone.name());
        connectionManager.handleConnecting(this);
        Request request = new Request.Builder()
                .header("token", connectionManager.getImContext().getConfiguration().getToken())
                .url(urlBuilder.toString())
                .build();
        webSocket = okHttpClient.newWebSocket(request, new SocketListener());

    }

    private WebSocket getWebSocket() {
        return webSocket;
    }

    void disconnect() throws Exception {
        Log.v(TAG, "disconnect");
        final WebSocket webSocket = getWebSocket();
        if (webSocket != null) {
            try {
                webSocket.close(1001, "close");
            } catch (Exception e) {
                Log.w(TAG, "sendPacket: " + e.getMessage());
                throw e;
            }
        }
    }

    void sendPacket(PacketProtos.Packet packet) throws Exception {
        Log.v(TAG, "sendPacket");
        final WebSocket webSocket = getWebSocket();
        try {
            webSocket.send(ByteString.of(packet.toByteArray()));
        } catch (Exception e) {
            Log.w(TAG, "sendPacket: " + e.getMessage());
            throw e;
        }
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.v(TAG, "onOpen");
            connectionManager.handleConnectionOpened(Connection.this);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.v(TAG, "onMessage : " + bytes);
            try {
                PacketProtos.Packet packet = PacketProtos.Packet.newBuilder()
                        .mergeFrom(bytes.toByteArray()).build();
                connectionManager.handlePacket(Connection.this, packet);
            } catch (InvalidProtocolBufferException e) {
                Log.w(TAG, "onMessage: " + e.getMessage());
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.v(TAG, "onClosing code:" + code + ", reason:" + reason);
            connectionManager.handleConnectionClosing(Connection.this, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.v(TAG, "onClosed code:" + code + ", reason:" + reason);
            connectionManager.handleConnectionClosed(Connection.this, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t,
                              @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.v(TAG, "onFailure : " + t.getMessage());
            connectionManager.handleConnectionFailed(Connection.this, t, response);
        }
    }

}

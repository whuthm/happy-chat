package com.whuthm.happychat.imlib;

import android.support.annotation.Nullable;

import com.barran.lib.utils.log.Logs;
import com.google.protobuf.InvalidProtocolBufferException;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.data.api.RetrofitClient;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

class AbstractConnection implements Connection {

    private boolean connected;
    private boolean authenticated;

    private static final String TAG = ChatConnection.class.getSimpleName();

    //private String url = "ws://echo.websocket.org";// websocket官网测试url
    // 模拟器访问127.0.0.1访问不成功，需要使用本机ip地址映射10.0.2.2
    private String url = "ws://10.0.2.2:8080/chat";// 模拟器联调本机后端

    private WebSocket webSocket;

    public AbstractConnection() {
    }

    public final boolean isConnected() {
        return this.connected;
    }

    protected void setConnected(boolean connected) {
        this.connected = connected;
    }

    public final boolean isAuthenticated() {
        return this.authenticated;
    }

    protected void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Override
    public void connect() {
        Logs.v(TAG, "connect");
        Request request = new Request.Builder().url(url).build();
        webSocket = RetrofitClient.okHttp().newWebSocket(request, new SocketListener());
    }

    private WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public void disconnect() throws Exception {
        Logs.v(TAG, "disconnect");
        final WebSocket webSocket = getWebSocket();
        if (webSocket != null) {
            try {
                webSocket.close(1001, "close");
            } catch (Exception e) {
                Logs.w(TAG, "sendPacket: " + e.getMessage());
                throw e;
            }
        }
    }

    public void sendPacket(PacketProtos.Packet packet) throws Exception {
        final WebSocket webSocket = getWebSocket();
        if (webSocket != null) {
            try {
                webSocket.send(ByteString.of(packet.toByteArray()));
            } catch (Exception e) {
                Logs.w(TAG, "sendPacket: " + e.getMessage());
                throw e;
            }
        }
    }

    private class SocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Logs.v(TAG, "onOpen");
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Logs.v(TAG, "onMessage : " + bytes);
            try {
                PacketProtos.Packet packet = PacketProtos.Packet.newBuilder().mergeFrom(bytes.toByteArray()).build();
            } catch (InvalidProtocolBufferException e) {
                Logs.w(TAG, "onMessage: " + e.getMessage());
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Logs.v(TAG, "onClosing code:" + code + ", reason:" + reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Logs.v(TAG, "onClosed code:" + code + ", reason:" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t,
                              @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Logs.v(TAG, "onFailure : " + t.getMessage());
            performDisconnected();
        }
    }

    private void performConnected() {
        setConnected(true);
    }

    private void performDisconnected() {
        setConnected(false);
        setAuthenticated(false);
    }
}

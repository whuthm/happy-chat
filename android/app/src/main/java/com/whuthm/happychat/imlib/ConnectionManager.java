package com.whuthm.happychat.imlib;

import android.support.annotation.Nullable;

import com.barran.lib.utils.log.Logs;
import com.google.protobuf.InvalidProtocolBufferException;
import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.model.ConnectionStatus;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.util.PacketUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

class ConnectionManager extends AbstractChatContextImplService implements ConnectionService, PacketSender {

    private static final String TAG = ConnectionManager.class.getSimpleName();

    private ConnectionStatus connectionStatus;
    private ChatConnection chatConnection;


    private IQPacketHandler iqPacketHandler;
    private MessagePacketHandler messagePacketHandler;

    private final Scheduler connectionScheduler;

    ConnectionManager(ChatContext chatContext) {
        super(chatContext);
        this.connectionScheduler = Schedulers.from(Executors.newSingleThreadExecutor(new RxThreadFactory("rx-connection")));
        this.connectionStatus = ConnectionStatus.DISCONNECTED;
    }

    public Scheduler getConnectionScheduler() {
        return connectionScheduler;
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setIqPacketHandler(IQPacketHandler iqPacketHandler) {
        this.iqPacketHandler = iqPacketHandler;
    }

    public void setMessagePacketHandler(MessagePacketHandler messagePacketHandler) {
        this.messagePacketHandler = messagePacketHandler;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    /**
     * 可能对外提供接口，暂时private
     */
    @Override
    public void connect() {
        runOnConnectionThread(new Runnable() {
            @Override
            public void run() {
                connectInternal();
            }
        });
    }

    private void sendHeartbeatPacketWhenConnected() {
        ChatConnection chatConnection = getChatConnection();
        if (getConnectionStatus() == ConnectionStatus.CONNECTED && chatConnection != null) {
            try {
                chatConnection.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.iq, PacketUtils.getPingIQ()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void runOnConnectionThread(final Runnable runnable) {
        Observable
                .fromCallable(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        runnable.run();
                        return ConnectionManager.this;
                    }
                })
                .subscribeOn(getConnectionScheduler())
                .observeOn(getConnectionScheduler())
                .subscribe(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(Object value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private synchronized void connectInternal() {
        Logs.v(TAG, "connectInternal: " + isConnectable());
        sendHeartbeatPacketWhenConnected();
        if (isConnectable()) {
            ChatConnection chatConnection = new ChatConnection(this);
            setChatConnection(chatConnection);
            performChangeConnectionStatus(ConnectionStatus.CONNECTING);
            chatConnection.connect();
        }
    }

    private ChatConnection getChatConnection() {
        return chatConnection;
    }

    private void setChatConnection(ChatConnection chatConnection) {
        this.chatConnection = chatConnection;
    }

    @Override
    public void disconnect() {
        runOnConnectionThread(new Runnable() {
            @Override
            public void run() {
                disconnectInternal();
            }
        });
    }

    private synchronized void disconnectInternal() {
        final ChatConnection chatConnection = getChatConnection();
        if (chatConnection != null) {
            try {
                chatConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setChatConnection(null);
        }
        performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
    }

    private void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @Override
    public void addConnectionStatusListener(ConnectionStatusListener listener) {

    }

    @Override
    public void removeConnectionStatusListener(ConnectionStatusListener listener) {

    }

    private void performChangeConnectionStatus(ConnectionStatus connectionStatus) {
        if (connectionStatus != getConnectionStatus()) {
            Logs.v("performChangeConnectionStatus:" + connectionStatus);
            setConnectionStatus(connectionStatus);
            // TODO: notify connection status changed
        }
    }

    /**
     * 是否可执行连接动作
     *
     * @return
     */
    private boolean isConnectable() {
        final ConnectionStatus connectionStatus = getConnectionStatus();
        return connectionStatus == ConnectionStatus.DISCONNECTED && isActive();
    }

    boolean isConnected() {
        return getConnectionStatus() == ConnectionStatus.CONNECTED;
    }

    synchronized void handlePacket(ChatConnection chatConnection, PacketProtos.Packet packet) {
        if (this.chatConnection != chatConnection) {
            return;
        }
        switch (packet.getType()) {
            case message:
                try {
                    MessageProtos.MessageBean messageBean = MessageProtos.MessageBean.newBuilder().mergeFrom(packet.getData()).build();
                    handleMessagePacket(chatConnection, packet, messageBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logs.w(TAG, "handleMessageBean:" + e.getMessage());
                }
                break;
            case iq:
                try {
                    IQProtos.IQ iq = IQProtos.IQ.newBuilder().mergeFrom(packet.getData()).build();
                    handleIQPacket(chatConnection, packet, iq);
                } catch (Exception e) {
                    Logs.w(TAG, "handleIQ:" + e.getMessage());
                }
                break;
        }
    }

    private void handleMessagePacket(ChatConnection chatConnection, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception {
        if (messagePacketHandler != null) {
            messagePacketHandler.handleMessagePacket(this, packet, messageBean);
        }
    }

    private void handleIQPacket(ChatConnection chatConnection, PacketProtos.Packet packet, IQProtos.IQ iq) throws Exception {
        switch (iq.getAction()) {
            case pong:
                //心跳应答
                break;
            default:
                if (iqPacketHandler != null) {
                    iqPacketHandler.handlerIQPacket(this, packet, iq);
                }
                break;
        }
    }

    synchronized void handleConnectionClosed(ChatConnection chatConnection, int code, String reason) {
        // TODO: 定义code， 如token未认证等
        Logs.v(TAG, "handleConnectionClosed: code=" + code + ", reason=" + reason);
        if (chatConnection == getChatConnection()) {
            setChatConnection(null);
            performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
        }
    }

    synchronized void handleConnectionClosing(ChatConnection chatConnection, int code, String reason) {
        // TODO: 定义code， 如token未认证等
        Logs.v(TAG, "handleConnectionClosing: code=" + code + ", reason=" + reason);
        if (chatConnection == getChatConnection()) {
            setChatConnection(null);
            performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
        }
    }

    synchronized void handleConnectionOpened(ChatConnection chatConnection) {
        Logs.v(TAG, "handleConnectionOpened");
        if (chatConnection == getChatConnection()) {
            performChangeConnectionStatus(ConnectionStatus.CONNECTED);
        }
    }

    synchronized void handleConnectionFailed(ChatConnection chatConnection, Throwable t,
                                             @Nullable Response response) {
        Logs.v(TAG, "handleConnectionFailed");
        if (chatConnection == getChatConnection()) {
            setChatConnection(null);
            performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
        }
    }

    @Override
    public void sendPacket(PacketProtos.Packet packet) throws Exception {
        final ChatConnection chatConnection = getChatConnection();
        final ConnectionStatus connectionStatus = getConnectionStatus();
        if (connectionStatus != ConnectionStatus.CONNECTED || chatConnection == null) {
            throw new Exception("Chat is not connected");
        }
        chatConnection.sendPacket(packet);
    }
}

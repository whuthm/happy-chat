package com.whuthm.happychat.imlib;

import android.support.annotation.Nullable;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.event.ConnectionEvent;
import com.whuthm.happychat.imlib.model.ConnectionStatus;
import com.whuthm.happychat.util.PacketUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.RxThreadFactory;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

class ConnectionManager extends AbstractIMService implements ConnectionService, PacketSender {

    private static final String TAG = ConnectionManager.class.getSimpleName();

    private ConnectionStatus connectionStatus;
    private Connection chatConnection;


    private final Set<IQPacketHandler> iqPacketHandlers;
    private final Set<MessagePacketHandler> messagePacketHandlers;

    private final Scheduler connectionScheduler;
    private final ConnectionEvent.Poster poster;

    ConnectionManager(IMContext chatContext, ConnectionEvent.Poster poster) {
        super(chatContext);
        this.poster = poster;
        this.connectionScheduler = Schedulers.from(Executors.newSingleThreadExecutor(new RxThreadFactory("rx-connection")));
        this.connectionStatus = ConnectionStatus.DISCONNECTED;
        this.iqPacketHandlers = new HashSet<>();
        this.messagePacketHandlers = new HashSet<>();
    }

    public Scheduler getConnectionScheduler() {
        return connectionScheduler;
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void addIqPacketHandler(IQPacketHandler iqPacketHandler) {
        this.iqPacketHandlers.add(iqPacketHandler);
    }

    public void addMessagePacketHandler(MessagePacketHandler messagePacketHandler) {
        this.messagePacketHandlers.add(messagePacketHandler);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iqPacketHandlers.clear();
        messagePacketHandlers.clear();
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
        final Connection chatConnection = getChatConnection();
        if (isConnected() && chatConnection != null) {
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
            Connection chatConnection = new Connection(this);
            setChatConnection(chatConnection);
            chatConnection.connect();
        }
    }

    private Connection getChatConnection() {
        return chatConnection;
    }

    private void setChatConnection(Connection chatConnection) {
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
        final Connection chatConnection = getChatConnection();
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

    private void performChangeConnectionStatus(ConnectionStatus connectionStatus) {
        if (connectionStatus != getConnectionStatus()) {
            Logs.v("performChangeConnectionStatus:" + connectionStatus);
            setConnectionStatus(connectionStatus);
            poster.postConnectionStatusChanged(connectionStatus);
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

    synchronized void handlePacket(Connection chatConnection, PacketProtos.Packet packet) {
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

    private void handleMessagePacket(Connection chatConnection, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception {
        if (chatConnection == getChatConnection()) {
            for (MessagePacketHandler messagePacketHandler : messagePacketHandlers) {
                messagePacketHandler.handleMessagePacket(this, packet, messageBean);
            }
        }
    }

    private void handleIQPacket(Connection chatConnection, PacketProtos.Packet packet, IQProtos.IQ iq) throws Exception {
        switch (iq.getAction()) {
            case pong:
                //心跳应答
                break;
            default:
                break;
        }
        if (chatConnection == getChatConnection()) {
            for (IQPacketHandler iqPacketHandler : iqPacketHandlers) {
                iqPacketHandler.handlerIQPacket(this, packet, iq);
            }
        }
    }

    synchronized void handleConnectionClosed(Connection chatConnection, int code, String reason) {
        // TODO: 定义code， 如token未认证等
        Logs.v(TAG, "handleConnectionClosed: code=" + code + ", reason=" + reason);
        if (chatConnection == getChatConnection()) {
            setChatConnection(null);
            performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
        }
    }

    synchronized void handleConnectionClosing(Connection chatConnection, int code, String reason) {
        // TODO: 定义code， 如token未认证等
        Logs.v(TAG, "handleConnectionClosing: code=" + code + ", reason=" + reason);
        if (chatConnection == getChatConnection()) {
            setChatConnection(null);
            performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
        }
    }

    synchronized void handleConnectionOpened(Connection chatConnection) {
        Logs.v(TAG, "handleConnectionOpened");
        if (chatConnection == getChatConnection()) {
            performChangeConnectionStatus(ConnectionStatus.CONNECTED);
        }
    }

    synchronized void handleConnectionFailed(Connection chatConnection, Throwable t,
                                             @Nullable Response response) {
        Logs.v(TAG, "handleConnectionFailed");
        if (chatConnection == getChatConnection()) {
            setChatConnection(null);
            performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
        }
    }

    synchronized void handleConnecting(Connection chatConnection) {
        if (chatConnection == getChatConnection()) {
            performChangeConnectionStatus(ConnectionStatus.CONNECTING);
        }
    }

    @Override
    public void sendPacket(PacketProtos.Packet packet) throws Exception {
        final Connection chatConnection = getChatConnection();
        final ConnectionStatus connectionStatus = getConnectionStatus();
        if (connectionStatus != ConnectionStatus.CONNECTED || chatConnection == null) {
            throw new Exception("Chat is not connected");
        }
        chatConnection.sendPacket(packet);
    }
}

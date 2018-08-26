package com.whuthm.happychat.imlib;

import android.support.annotation.Nullable;

import com.google.protobuf.InvalidProtocolBufferException;
import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.model.ConnectionStatus;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.util.PacketUtils;

import okhttp3.Response;

class ConnectionManager extends AbstractChatContextImplService<ConnectionService> implements ConnectionService {

    private ConnectionStatus connectionStatus;
    private ChatConnection chatConnection;

    private MessageReceiver messageReceiver;

    ConnectionManager(ChatContext chatContext) {
        super(chatContext);
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    void setMessageReceiver(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 可能对外提供接口，暂时private
     */
    private synchronized void connect() {
        if (isConnectable()) {
            ChatConnection chatConnection = new ChatConnection(this);
            setChatConnection(chatConnection);
            performChangeConnectionStatus(ConnectionStatus.CONNECTING);
            chatConnection.connect();
        }
    }

    private void setChatConnection(ChatConnection chatConnection) {
        this.chatConnection = chatConnection;
    }

    private synchronized void disconnect() {
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
            setConnectionStatus(connectionStatus);
            // TODO: notify connection status changed
        }
    }

    /**
     * 是否可执行连接动作
     * @return
     */
    private boolean isConnectable() {
        final ConnectionStatus connectionStatus = getConnectionStatus();
        return connectionStatus == ConnectionStatus.DISCONNECTED;
    }

    boolean isConnected() {
        return getConnectionStatus() == ConnectionStatus.CONNECTED;
    }

    void handlePacket(ChatConnection chatConnection, PacketProtos.Packet packet) {
        switch (packet.getType()) {
            case message:
                try {
                    MessageProtos.MessageBean messageBean = MessageProtos.MessageBean.newBuilder().mergeFrom(packet.getData()).build();
                    handleMessageBean(chatConnection, messageBean);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case iq:
                try {
                    IQProtos.IQ iq = IQProtos.IQ.newBuilder().mergeFrom(packet.getData()).build();
                    handleIQ(chatConnection, iq);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void handleMessageBean(ChatConnection chatConnection, MessageProtos.MessageBean messageBean) {
        Message message = new Message();
        message.setSid(messageBean.getSid());
        message.setType(messageBean.getType());
        message.setBody(messageBean.getBody());
        message.setFrom(messageBean.getFrom());
        message.setTo(messageBean.getTo());
        message.setAttrs(messageBean.getAttributes());
        message.setConversationType(messageBean.getConversationType());
        message.setReceiveTime(System.currentTimeMillis());
        message.setSendTime(messageBean.getSendTime());
        message.setExtra(messageBean.getExtra());
        messageReceiver.onMessageReceive(message);
        // 需要发送已接收回执
        try {
            chatConnection.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.iq,
                    PacketUtils.getMessageDeliveredACKIQ(IQProtos.MessageDeliveredACKIQ.newBuilder().setId(message.getUid()).build())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIQ(ChatConnection chatConnection, IQProtos.IQ iq) {

    }

    synchronized void handleConnectionClosed(ChatConnection chatConnection, int code, String reason) {
        // TODO: 定义code， token未认证
        performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);
        setChatConnection(null);
    }

    synchronized void handleConnectionOpened(ChatConnection chatConnection) {
        performChangeConnectionStatus(ConnectionStatus.CONNECTED);
    }

    synchronized void handleConnectionFailed(ChatConnection chatConnection, Throwable t,
                                @Nullable Response response) {
        performChangeConnectionStatus(ConnectionStatus.DISCONNECTED);

    }
}

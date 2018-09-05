package com.whuthm.happychat.imlib;

import android.util.Log;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;
import com.whuthm.happychat.util.PacketUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class MessageManager extends AbstractChatContextImplService implements MessageService, MessagePacketHandler {

    private final static String TAG = MessageManager.class.getSimpleName();
    private final Collection<MessageReceiver> messageReceivers;
    private final MessageSender messageSender;

    MessageManager(ChatContext chatContext, PacketSender packetSender) {
        super(chatContext);
        messageReceivers = new HashSet<>();
        this.messageSender = new MessageSender(packetSender);
    }

    private IMessageDao getMessageDao() {
        return getOpenHelper().getMessageDao();
    }

    @Override
    public Observable<List<Message>> getHistoryMessages(final HistoryMessagesRequest request) {
        return Observable
                .create(new ObservableOnSubscribe<List<Message>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Message>> e) throws Exception {
                        e.onNext(getMessageDao().getHistoryMessages(request));
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Message> sendMessage(final Message message) {
        return Observable
                .create(new ObservableOnSubscribe<Message>() {
                    @Override
                    public void subscribe(ObservableEmitter<Message> e) throws Exception {
                        try {
                            Message messageInDb = getMessageDao().getMessageByUid(message.getUid());
                            if (messageInDb != null) {
                                getMessageDao().updateMessage(message);
                            } else {
                                getMessageDao().insertMessage(message);
                            }
                            Log.i(TAG, "sendMessage insertOrUpdate : " + message);
                            messageSender.sendMessage(message);
                            e.onNext(message);
                            e.onComplete();
                        } catch (Exception ex) {
                            e.onError(ex);
                            Log.e(TAG, "sendMessage:" + message, ex);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Message> resendMessage(Message message) {
        return sendMessage(message);
    }

    @Override
    public Observable<Message> markMessagesOfConversationAsRead(String conversationId) {
        return null;
    }

    @Override
    public Observable<Message> markAllMessagesAsRead() {
        return null;
    }

    void addMessageReceiver(MessageReceiver messageReceiver) {
        messageReceivers.add(messageReceiver);
    }

    @Override
    public void handleMessagePacket(PacketSender packetSender, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception {
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

        try {
            long messageId = getOpenHelper().getMessageDao().insertMessage(message);
            message.setId(messageId);
            // notify Message added;
            for (MessageReceiver messageReceiver : messageReceivers) {
                messageReceiver.onMessageReceive(message);
            }
        } catch (Exception ex) {
            Message messageByUid = getOpenHelper().getMessageDao().getMessageByUid(message.getUid());
            // 消息在本地已存在
            if (messageByUid != null) {
                message.setId(messageByUid.getId());
            } else {
                Log.e(TAG, "onMessageReceive: " + message, ex);
                throw ex;
            }
        }
        // 需要发送已接收回执
        packetSender.sendPacket(PacketUtils.createPacket(PacketProtos.Packet.Type.iq,
                PacketUtils.getMessageDeliveredACKIQ(IQProtos.MessageDeliveredACKIQ.newBuilder().setId(message.getUid()).build())));

    }
}

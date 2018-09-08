package com.whuthm.happychat.imlib;

import android.util.Log;

import com.whuthm.happychat.data.IQProtos;
import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.data.PacketProtos;
import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;
import com.whuthm.happychat.util.PacketUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

class MessageManager extends AbstractChatContextImplService implements MessageService, MessagePacketHandler, IQPacketHandler {

    private final static String TAG = MessageManager.class.getSimpleName();
    private final MessageSender messageSender;

    MessageManager(ChatContext chatContext, PacketSender packetSender) {
        super(chatContext);
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
                            Log.i(TAG, "sendMessage sending");
                            message.setSentStatus(Message.SentStatus.SENDING);
                            saveMessageInternal(message);
                            messageSender.sendMessage(message);
                            e.onNext(message);
                            e.onComplete();
                        } catch (Exception ex) {
                            e.onError(ex);
                            Log.e(TAG, "sendMessage:" + message, ex);
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "sendMessage send failed " + Thread.currentThread(), throwable);
                        saveMessageInternal(message);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "sendMessage sent " + Thread.currentThread());
                        saveMessageInternal(message);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void saveMessageInternal(Message message) {
        Message messageInDb = getMessageDao().getMessageByUid(message.getUid());
        if (messageInDb != null) {
            getMessageDao().updateMessage(message);
            performMessageUpdated(message);
        } else {
            long messageId = getMessageDao().insertMessage(message);
            message.setId(messageId);
            performMessageAdded(message);
        }
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

    @Override
    public void handleMessagePacket(PacketSender packetSender, PacketProtos.Packet packet, MessageProtos.MessageBean messageBean) throws Exception {
        Message message = MessageUtils.getMessageFrom(messageBean, getCurrentUserId());
        try {
            long messageId = getOpenHelper().getMessageDao().insertMessage(message);
            message.setId(messageId);
            performMessageAdded(message);
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

    private void performMessageAdded(Message message) {
        // notify Message added;
        getEventPoster().postMessageAdded(message);
    }

    private void performMessageUpdated(Message message) {
        getEventPoster().postMessageUpdated(message);
    }

    @Override
    public void handlerIQPacket(PacketSender packetSender, PacketProtos.Packet packet, IQProtos.IQ iq) throws Exception {
        if (iq.getAction() == IQProtos.IQ.Action.messageDelivered) {
            IQProtos.MessageDeliveredIQ messageDeliveredIQ = IQProtos.MessageDeliveredIQ.newBuilder().mergeFrom(iq.getData()).build();
            messageSender.performMessageDelivered(messageDeliveredIQ.getId(), messageDeliveredIQ.getSid());
        }
    }
}

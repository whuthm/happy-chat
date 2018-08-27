package com.whuthm.happychat.imlib;

import android.util.Log;

import com.whuthm.happychat.imlib.db.IMessageDao;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class MessageManager extends AbstractChatContextImplService<MessageService> implements MessageService, MessageReceiver {

    private final static String TAG = MessageManager.class.getSimpleName();
    private final Collection<MessageReceiver> messageReceivers;

    MessageManager(ChatContext chatContext) {
        super(chatContext);
        messageReceivers = new HashSet<>();
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
    public Observable<Message> sendMessage(Message message) {
        return null;
    }

    @Override
    public Observable<Message> resendMessage(Message message) {
        return null;
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
    public void onMessageReceive(Message message) {
        try {
            long messageId = getOpenHelper().getMessageDao().insertMessage(message);
            message.setId(messageId);
            // notify Message added;
            for (MessageReceiver messageReceiver : messageReceivers) {
                messageReceiver.onMessageReceive(message);
            }
        } catch (Exception ex) {
            Message messageByUid = getOpenHelper().getMessageDao().getMessageByUid(message.getUid());
            if (messageByUid != null) {
                message.setId(messageByUid.getId());
            } else {
                Log.e(TAG, "onMessageReceive: " + message, ex);
                throw ex;
            }
        }
    }

    void addMessageReceiver(MessageReceiver messageReceiver) {
        messageReceivers.add(messageReceiver);
    }
}

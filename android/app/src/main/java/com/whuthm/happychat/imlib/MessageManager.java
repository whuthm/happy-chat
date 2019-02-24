package com.whuthm.happychat.imlib;

import android.text.TextUtils;
import android.util.Log;

import com.whuthm.happychat.common.rx.Observers;
import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

class MessageManager extends AbstractIMService implements MessageService, MessageReceiver {

    private final static String TAG = MessageManager.class.getSimpleName();
    private final MessageSender messageSender;
    private final MessageEvent.Poster poster;
    private final IMessageDao dao;
    private final Scheduler diskScheduler;

    MessageManager(IMContext chatContext, MessageSender messageSender, MessageEvent.Poster poster, IMessageDao dao, Scheduler diskScheduler) {
        super(chatContext);
        this.messageSender = messageSender;
        this.poster = poster;
        this.dao = dao;
        this.diskScheduler = diskScheduler;
    }

    private IMessageDao getMessageDao() {
        return dao;
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
                .subscribeOn(diskScheduler);
    }

    @Override
    public Observable<Message> sendMessage(final Message message) {
        if (message.getSentStatus() == Message.SentStatus.SENDING) {
            return Observable.error(new IllegalStateException("Message is Sending"));
        }
        return messageSender.sendMessage(message)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.i(TAG, "sendMessage sending");
                        if (TextUtils.isEmpty(message.getId())) {
                            message.setId(UUID.randomUUID().toString());
                        }
                        message.setSenderUserId(getCurrentUserId());
                        message.setDirection(Message.Direction.SEND);
                        if (message.getSendTime() <= 0) {
                            message.setSendTime(System.currentTimeMillis());
                        }
                        message.setSentStatus(Message.SentStatus.SENDING);
                        saveMessage(message);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "sendMessage send failed " + Thread.currentThread(), throwable);
                        message.setSentStatus(Message.SentStatus.FAILED);
                        saveMessage(message);
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "sendMessage sent " + Thread.currentThread());
                        message.setSentStatus(Message.SentStatus.SENT);
                        saveMessage(message);
                    }
                });
    }

    private void saveMessage(final Message message) {
        Observable
                .fromCallable(new Callable<Message>() {
                    @Override
                    public Message call() throws Exception {
                        saveMessageInternal(message);
                        return message;
                    }
                })
                .subscribeOn(diskScheduler)
                .subscribe(Observers.<Message>empty());
    }

    private void saveMessageInternal(Message message) {
        Message messageInDb = getMessageDao().getMessage(message.getId());
        if (messageInDb != null) {
            getMessageDao().updateMessage(message);
            performMessageUpdated(message);
        } else {
            getMessageDao().insertMessage(message);
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
    public boolean onReceive(Message message) {
        try {
            getMessageDao().insertMessage(message);
            performMessageAdded(message);
        } catch (Exception ex) {
            Message messageInLocal = getMessageDao().getMessage(message.getId());
            // 消息在本地已存在
            if (messageInLocal != null) {
                message.setId(messageInLocal.getId());
            } else {
                Log.e(TAG, "onMessageReceive: " + message, ex);
                return false;
            }
        }
        return true;
    }

    private void performMessageAdded(Message message) {
        // notify Message added;
        getEventPoster().postMessageAdded(message);
    }

    private void performMessageUpdated(Message message) {
        getEventPoster().postMessageUpdated(message);
    }

    private MessageEvent.Poster getEventPoster() {
        return poster;
    }

}

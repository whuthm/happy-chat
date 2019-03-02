package com.whuthm.happychat.imlib;

import android.util.Log;

import com.whuthm.happychat.imlib.dao.IConversationDao;
import com.whuthm.happychat.imlib.dao.IMessageDao;
import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.exception.NotFoundException;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageBody;
import com.whuthm.happychat.imlib.vo.ConversationProperties;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

class ConversationManager extends AbstractIMService
        implements ConversationService {

    private final static String TAG = ConversationManager.class.getSimpleName();
    private final ConversationEvent.Poster poster;
    private final IConversationDao dao;
    private final IMessageDao messageDao;
    private final Scheduler diskScheduler;

    ConversationManager(IMContext chatContext, ConversationEvent.Poster poster, IConversationDao dao, IMessageDao messageDao, Scheduler diskScheduler) {
        super(chatContext);
        this.poster = poster;
        this.dao = dao;
        this.messageDao = messageDao;
        this.diskScheduler = diskScheduler;
    }

    private IConversationDao getDao() {
        return dao;
    }

    @Override
    public Observable<Conversation> getConversation(final String conversationId) {
        return Observable
                .create(new ObservableOnSubscribe<Conversation>() {
                    @Override
                    public void subscribe(ObservableEmitter<Conversation> e) throws Exception {
                        Conversation conversation = getConversationInternal(conversationId);
                        if (conversation != null) {
                            e.onNext(conversation);
                            e.onComplete();
                        } else {
                            e.onError(new NotFoundException(
                                    "Conversation(" + conversationId + ") is not found!"));
                        }
                    }
                })
                .subscribeOn(diskScheduler);
    }

    private Conversation getConversationInternal(String conversationId) {
        return getDao().getConversation(conversationId);
    }

    @Override
    public Observable<List<Conversation>> getAllConversations() {
        return Observable
                .create(new ObservableOnSubscribe<List<Conversation>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Conversation>> e)
                            throws Exception {
                        e.onNext(getDao().getAllConversations());
                        e.onComplete();
                    }
                })
                .subscribeOn(diskScheduler)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "getAllConversations", throwable);
                    }
                });
    }

    @Override
    public Observable<Conversation> markAllMessagesOfConversationAsRead(final String conversationId) {
        return Observable
                .create(new ObservableOnSubscribe<Conversation>() {
                    @Override
                    public void subscribe(ObservableEmitter<Conversation> e) throws Exception {
                        Conversation conversation = getConversationInternal(conversationId);
                        messageDao.markMessagesOfConversationAsRead(conversationId);
                        conversation.setUnreadCount(0);
                        ConversationProperties properties = new ConversationProperties();
                        properties.addUnreadCountProperty();
                        getDao().insertOrUpdateConversation(conversation);
                        getEventPoster().postConversationUpdated(conversation, properties);
                        e.onNext(conversation);
                        e.onComplete();
                    }
                })
                .subscribeOn(diskScheduler)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "markAllMessagesOfConversationAsRead : " + conversationId, throwable);
                    }
                });
    }

    // 判断会话是否存在，不存在则创建
    // 判断消息是否已读，未读则未读数+1
    // 判断是否是最新消息
    @Subscribe
    public void onMessageAddedEvent(MessageEvent.AddedEvent event) {
        Log.i(TAG, "onMessageAddedEvent");
        final Message addedMessage = event.getMessage();
        Conversation conversation = getConversationInternal(addedMessage.getConversationId());
        boolean added = false;
        if (conversation == null) {
            added = true;
            conversation = new Conversation();
            conversation.setId(addedMessage.getConversationId());
            conversation.setType(addedMessage.getConversationType());
            conversation.setUnreadCount(messageDao.getUnreadCountOf(conversation.getId()));
        }

        final ConversationProperties properties = new ConversationProperties();
        if (!added
                && MessageBody.isCounted(addedMessage.getType())
                && addedMessage.getDirection() == Message.Direction.RECEIVE
                && !addedMessage.getReceivedStatus().isRead()) {
            properties.addUnreadCountProperty();
            conversation.setUnreadCount(conversation.getUnreadCount() + 1);
        }

        if (updateLatestMessage(conversation, addedMessage)) {
            properties.addLatestMessageProperty();
        }

        if (added || properties.containsProperties()) {
            getDao().insertOrUpdateConversation(conversation);
            if (added) {
                getEventPoster().postConversationAdded(conversation);
            } else {
                getEventPoster().postConversationUpdated(conversation, properties);
            }
        }
    }

    // 判断会话是否存在
    // 会话存在，则判断是否需要更新latestMessage
    @Subscribe
    public void onMessageUpdatedEvent(MessageEvent.UpdatedEvent event) {
        Log.i(TAG, "onMessageUpdatedEvent");
        Message updatedMessage = event.getMessage();
        final Conversation conversation = getConversationInternal(updatedMessage.getConversationId());
        if (conversation != null) {
            if (updateLatestMessage(conversation, updatedMessage)) {
                getDao().insertOrUpdateConversation(conversation);
                final ConversationProperties properties = new ConversationProperties();
                properties.addLatestMessageProperty();
                getEventPoster().postConversationUpdated(conversation, properties);
            }
        }
    }

    // 判断会话是否存在
    // 判断消息是否已读，已读则更新未读数
    // 判断删除的消息是否是最新消息，是则更新
    @Subscribe
    public void onMessageRemovedEvent(MessageEvent.RemovedEvent event) {
        Log.i(TAG, "onMessageRemovedEvent");
        Message removedMessage = event.getMessage();
        Conversation conversation = getConversationInternal(removedMessage.getConversationId());
        if (conversation != null) {
            final ConversationProperties properties = new ConversationProperties();
            if (removedMessage.getDirection() == Message.Direction.RECEIVE && !removedMessage.getReceivedStatus().isRead() && conversation.getUnreadCount() > 0) {
                conversation.setUnreadCount(conversation.getUnreadCount() - 1);
                properties.addUnreadCountProperty();
            }
            if (removedMessage.getId().equals(conversation.getLatestMessageId())) {
                properties.addLatestMessageProperty();
                setLatestMessage(conversation, messageDao.getLatestMessage());
            }
            if (properties.containsProperties()) {
                getEventPoster().postConversationUpdated(conversation, properties);
            }
        }
    }

    private boolean updateLatestMessage(Conversation conversation, Message message) {
        if (message.getId().equals(conversation.getLatestMessageId()) || message.getSendTime() > conversation.getLatestMessageTime()) {
            setLatestMessage(conversation, message);
            return true;
        }
        return false;
    }

    private void setLatestMessage(Conversation conversation, Message latestMessage) {
        conversation.setLatestMessage(latestMessage);
        if (latestMessage != null) {
            conversation.setLatestMessageId(latestMessage.getId());
            conversation.setLatestMessageTime(latestMessage.getSendTime());
        } else {
            conversation.setLatestMessageId(null);
        }
    }

    private ConversationEvent.Poster getEventPoster() {
        return poster;
    }
}

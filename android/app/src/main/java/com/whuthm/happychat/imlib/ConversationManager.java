package com.whuthm.happychat.imlib;

import android.util.Log;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.imlib.dao.IConversationDao;
import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.exception.NotFoundException;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;

class ConversationManager extends AbstractIMService
        implements ConversationService {

    private final static String TAG = ConversationManager.class.getSimpleName();
    private final ConversationEvent.Poster poster;
    private final IConversationDao dao;
    private final Scheduler diskScheduler;

    ConversationManager(IMContext chatContext, ConversationEvent.Poster poster, IConversationDao dao, Scheduler diskScheduler) {
        super(chatContext);
        this.poster = poster;
        this.dao = dao;
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
                .subscribeOn(diskScheduler);
    }

    @Subscribe
    public void onMessageAddedEvent(MessageEvent.AddedEvent event) {
        Log.i(TAG, "onMessageAddedEvent");
        updateConversationByAddedMessage(event.getMessage());
    }

    @Subscribe
    public void onMessageUpdatedEvent(MessageEvent.UpdatedEvent event) {
        Log.i(TAG, "onMessageUpdatedEvent");
        updateConversationByUpdatedMessage(event.getMessage());
    }

    @Subscribe
    public void onMessageRemovedEvent(MessageEvent.RemovedEvent event) {
        Log.i(TAG, "onMessageRemovedEvent");
    }

    private void updateConversationByAddedMessage(Message message) {
        Conversation conversation = getConversationInternal(message.getConversationId());
        boolean add = false;
        if (conversation == null) {
            add = true;
            conversation = new Conversation();
            conversation.setId(message.getConversationId());
            conversation.setType(message.getConversationType());
        }

        if (message.getSendTime() > conversation.getLatestMessageTime()) {
            conversation.setLatestMessage(message);
            conversation.setLatestMessageId(message.getId());
            conversation.setLatestMessageTime(message.getSendTime());
        }
        getDao().insertOrUpdateConversation(conversation);

        if (add) {
            getEventPoster().postConversationAdded(conversation);
        } else {
            getEventPoster().postConversationUpdated(conversation);
        }
    }

    private void updateConversationByUpdatedMessage(Message message) {
        Conversation conversation = getConversationInternal(message.getConversationId());
        boolean add = false;
        if (conversation == null) {
            add = true;
            conversation = new Conversation();
            conversation.setId(message.getConversationId());
            conversation.setType(message.getConversationType());
        }

        if (message.getSendTime() > conversation.getLatestMessageTime()) {
            conversation.setLatestMessage(message);
            conversation.setLatestMessageId(message.getId());
            conversation.setLatestMessageTime(message.getSendTime());
        }
        getDao().insertOrUpdateConversation(conversation);

        if (add) {
            getEventPoster().postConversationAdded(conversation);
        } else {
            getEventPoster().postConversationUpdated(conversation);
        }
    }

    private ConversationEvent.Poster getEventPoster() {
        return poster;
    }
}

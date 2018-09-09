package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.dao.IConversationDao;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.exception.NotFoundException;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class ConversationManager extends AbstractChatContextImplService implements ConversationService {

    private final static String TAG = ConnectionManager.class.getSimpleName();

    ConversationManager(ChatContext chatContext) {
        super(chatContext);
    }

    private IConversationDao getDao() {
        return getOpenHelper().getConversationDao();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
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
                            e.onError(new NotFoundException("Conversation(" + conversationId + ") is not found!"));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Conversation getConversationInternal(String conversationId) {
        return getDao().getConversation(conversationId);
    }

    @Override
    public Observable<List<Conversation>> getAllConversations() {
        return Observable
                .create(new ObservableOnSubscribe<List<Conversation>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Conversation>> e) throws Exception {
                        e.onNext(getDao().getAllConversations());
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Subscribe
    public void onMessageAddedEvent(MessageEvent.AddedEvent event) {
        updateConversationBy(event.getMessage());
    }

    @Subscribe
    public void onMessageUpdatedEvent(MessageEvent.UpdatedEvent event) {
        updateConversationBy(event.getMessage());
    }

    @Subscribe
    public void onMessageRemovedEvent(MessageEvent.RemovedEvent event) {
    }

    private void updateConversationBy(Message message) {
        Conversation conversation = getConversationInternal(message.getConversationId());
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setId(message.getConversationId());
            conversation.setType(message.getConversationType());
        }

        if (message.getId() > conversation.getLatestMessageId()) {
            conversation.setLatestMessage(message);
            conversation.setLatestMessageId(message.getId());
            conversation.setLatestMessageTime(message.getSendTime());
        }
        getDao().insertOrUpdateConversation(conversation);
    }
}

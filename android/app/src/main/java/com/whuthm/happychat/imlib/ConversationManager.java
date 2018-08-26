package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

class ConversationManager extends AbstractChatContextImplService<ConversationServiceV2> implements ConversationServiceV2, MessageReceiver {

    private final static String TAG = ConnectionManager.class.getSimpleName();
    private final Map<String, Conversation> allConversations;

    ConversationManager(ChatContext chatContext) {
        super(chatContext);
        this.allConversations = new ConcurrentHashMap<>();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    private void loadAllConversationsFronLocal() {
        Observable
                .create(new ObservableOnSubscribe<List<Conversation>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Conversation>> e) throws Exception {
                        e.onNext(getOpenHelper().getConversationDao().getAllConversations());
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<List<Conversation>>() {
                    @Override
                    public void onNext(List<Conversation> conversations) {
                        for (Conversation conversation : conversations) {
                            allConversations.put(conversation.getId(), conversation);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public Conversation getConversation(String conversationId) {
        return allConversations.get(conversationId);
    }

    @Override
    public List<Conversation> getAllConversations() {
        return new ArrayList<>(allConversations.values());
    }

    private Conversation getConversationInternal(String conversationId) {
        Conversation conversation = getConversation(conversationId);
        if (conversation == null) {
            conversation= getOpenHelper().getConversationDao().getConveration(conversationId);
        }
        return conversation;
    }

    @Override
    public void onMessageReceive(Message message) {
        Conversation conversation = getConversationInternal(message.getTo());
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setId(message.getTo());
            conversation.setType(message.getConversationType());
        }

        if (message.getId() > conversation.getLatestMessageId()) {
            conversation.setLatestMessage(message);
            conversation.setLatestMessageId(message.getId());
            conversation.setLatestMessageTime(message.getSendTime());
        }

    }
}

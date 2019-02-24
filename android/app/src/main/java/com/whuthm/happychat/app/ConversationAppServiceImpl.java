package com.whuthm.happychat.app;

import com.whuthm.happychat.common.rx.Observers;
import com.whuthm.happychat.imlib.AbstractIMService;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.ConversationService;
import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.model.Conversation;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

class ConversationAppServiceImpl extends AbstractIMService implements ConversationAppService {

    private final Map<String, Conversation> conversations = new ConcurrentHashMap<>();
    private final AtomicInteger totalUnreadCount = new AtomicInteger(0);

    private boolean allConversationsLoaded;

    ConversationAppServiceImpl(IMContext chatContext) {
        super(chatContext);
    }

    @Override
    public Observable<List<Conversation>> getAllConversations() {
        if (isAllConversationsLoaded()) {
            final List<Conversation> conversationList = new ArrayList<>(conversations.values());
            return Observable.just(conversationList);
        }
        return getImContext()
                .getService(ConversationService.class)
                .getAllConversations()
                .doOnNext(new Consumer<List<Conversation>>() {
                    @Override
                    public void accept(List<Conversation> value) throws Exception {
                        performLoadAllConversations(value);
                    }
                });
    }

    @Override
    public void syncAllConversations() {
        if (!isAllConversationsLoaded()) {
            getAllConversations().subscribe(Observers.<List<Conversation>>empty());
        }
    }

    private boolean isAllConversationsLoaded() {
        return allConversationsLoaded;
    }

    private void markAllConversationsLoaded() {
        allConversationsLoaded = true;
    }

    private void performLoadAllConversations(List<Conversation> value) {
        Map<String, Conversation> map = new HashMap<>();
        for (Conversation conversation : value) {
            map.put(conversation.getId(), conversation);
        }
        conversations.putAll(map);
        markAllConversationsLoaded();
    }

    @Override
    public int getTotalUnreadCount() {
        return totalUnreadCount.get();
    }

    @Subscribe
    public void onConversationAddedEvent(ConversationEvent.AddedEvent event) {
        conversations.put(event.getConversation().getId(), event.getConversation());
        calculateTotalUnreadCount();
    }

    @Subscribe
    public void onConversationRemovedEvent(ConversationEvent.RemovedEvent event) {
        conversations.remove(event.getConversation().getId());
        calculateTotalUnreadCount();
    }

    @Subscribe
    public void onConversationUpdatedEvent(ConversationEvent.UpdatedEvent event) {
        conversations.put(event.getConversation().getId(), event.getConversation());
        calculateTotalUnreadCount();
    }


    private void calculateTotalUnreadCount() {
        int count = 0;
        for (String key : conversations.keySet()) {
            Conversation conversation = conversations.get(key);
            if (conversation != null) {
                if (conversation.getNotificationStatus() != Conversation.NotificationStatus.DO_NOT_DISTURB) {
                    count += conversation.getUnreadCount();
                }
            }
        }
        int previousCount = totalUnreadCount.get();
        if (previousCount != count) {
            totalUnreadCount.set(count);
        }
    }

}

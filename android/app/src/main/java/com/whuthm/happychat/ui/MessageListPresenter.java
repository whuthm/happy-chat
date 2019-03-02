package com.whuthm.happychat.ui;

import android.support.annotation.MainThread;
import android.util.Log;

import com.whuthm.happychat.common.lifecycle.SimpleLifecycle;
import com.whuthm.happychat.common.rx.DisposableRegistry;
import com.whuthm.happychat.common.rx.Observers;
import com.whuthm.happychat.common.spec.Spec;
import com.whuthm.happychat.imlib.ConversationService;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.MessageService;
import com.whuthm.happychat.imlib.event.EventBusUtils;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.vo.HistoryMessagesRequest;
import com.whuthm.happychat.imlib.vo.LoadDataDirection;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class MessageListPresenter extends SimpleLifecycle {

    private final static String TAG = MessageListPresenter.class.getSimpleName();

    private final Map<LoadDataDirection, Boolean> hasMoreByDirection = new HashMap<>();  // 等待加载
    private final Map<LoadDataDirection, Boolean> loadingByDirection = new HashMap<>();  // 正在加载

    protected final String conversationId;
    protected final ConversationType conversationType;
    private final boolean ascending;
    private final IMContext imContext;
    private final Spec<Message> supportedSpec;

    public final static int PAGE_COUNT = 20;

    private final DisposableRegistry disposableRegistry;

    private final MessageListPresenterView presenterView;

    protected Message oldestMessage;
    protected Message newestMessage;

    // 用于判断发送已读回执
    private Message latestReceivedMessage;
    //private Message readReceiptMessage;
    private Message readMessage;

    private boolean resume;

    private final Comparator<Message> messageComparator;

    public MessageListPresenter(IMContext imContext, MessageListPresenterView presenterView, String conversationId, ConversationType conversationType, boolean ascending) {
        this.imContext = imContext;
        this.conversationId = conversationId;
        this.conversationType = conversationType;
        this.ascending = ascending;
        this.presenterView = presenterView;

        // 默认 设置有新消息和历史消息等待加载
        hasMoreByDirection.put(LoadDataDirection.Forward, true);
        hasMoreByDirection.put(LoadDataDirection.Backward, true);
        // 默认 设置没有正在加载的新消息和历史消息
        loadingByDirection.put(LoadDataDirection.Forward, false);
        loadingByDirection.put(LoadDataDirection.Backward, false);

        supportedSpec = new Spec<Message>() {
            @Override
            public boolean isSatisfiedBy(Message product) {
                return getConversationId().equals(product.getConversationId());
            }
        };

        disposableRegistry = new DisposableRegistry();
        this.messageComparator = new MessageComparator(ascending);
    }

    public MessageListPresenter(IMContext imContext, MessageListPresenterView presenterView, String conversationId, ConversationType conversationType) {
        this(imContext, presenterView, conversationId, conversationType, true);
    }

    public void onResume() {
        resume = true;
        markAllMessagesAsReadIfNecessary();
    }

    public void onPause() {
        resume = false;
    }

    public final boolean isResume() {
        return resume;
    }

    private void markAllMessagesAsReadIfNecessary() {
        if (isResume() && readMessage != null && latestReceivedMessage != null && !readMessage.getUid().equals(latestReceivedMessage.getUid())) {
            Log.i(TAG, "sendReadReceipt");
            readMessage = latestReceivedMessage;
        }
        imContext.getService(ConversationService.class).markAllMessagesOfConversationAsRead(getConversationId()).subscribe(Observers.<Conversation>empty());
    }

    private void markAllMessagesAsReadIfNecessary(Message message) {
        final boolean isSendMessage = message.getDirection() == Message.Direction.SEND;
        if (!isSendMessage && (latestReceivedMessage == null || (latestReceivedMessage.getSid() != null && message.getSid() != null && latestReceivedMessage.getSid() < message.getSid()))) {
            latestReceivedMessage = message;
        }
        markAllMessagesAsReadIfNecessary();
    }

    private MessageListPresenterView getPresenterView() {
        return presenterView;
    }

    public boolean isAscending() {
        return ascending;
    }

    public String getConversationId() {
        return conversationId;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        EventBusUtils.safeRegister(this);
        loadHistoryMessages(LoadDataDirection.Backward);
        loadHistoryMessages(LoadDataDirection.Forward);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.safeUnregister(this);
        disposableRegistry.unregisterAll();
    }

    /**
     * 加载历史消息
     *
     * @param direction
     */
    @MainThread
    public void loadHistoryMessages(final LoadDataDirection direction) {
        if (!isActive()) {
            return;
        }

        if (isLoading(direction)) {
            return;
        }

        if (!hasMore(direction)) {
            return;
        }
        loadingByDirection.put(direction, true);
        getPresenterView().onHistoryMessagesLoadStarted(direction);

        final DisposableObserver<List<Message>> disposableObserver = new DisposableObserver<List<Message>>() {
            @Override
            public void onNext(List<Message> messages) {
                Collections.sort(messages, messageComparator);
                // 不要在onComplete设置mLoadingByDirection
                loadingByDirection.put(direction, false);  // load结束
                hasMoreByDirection.put(direction, messages.size() > 0);  // 判断是否hasMore
                performMessagesLoaded(messages, direction);
                getPresenterView().onHistoryMessagesLoaded(direction, messages);
            }

            @Override
            public void onError(Throwable e) {
                loadingByDirection.put(direction, false);
                getPresenterView().onHistoryMessagesLoadFailed(direction, e);
            }

            @Override
            public void onComplete() {
            }
        };

        disposableRegistry.register(disposableObserver);

        onLoadHistoryMessages(direction)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);
    }

    private Observable<List<Message>> onLoadHistoryMessages(LoadDataDirection direction) {
        HistoryMessagesRequest request = new HistoryMessagesRequest.Builder()
                .setAscending(isAscending())
                .setLoadDataDirection(direction)
                .setBaseMessageId(getBaseMessageId(direction))
                .setConversationId(getConversationId())
                .setCount(PAGE_COUNT)
                .build();
        return imContext.getService(MessageService.class).getHistoryMessages(request);
    }

    private long getBaseMessageId(LoadDataDirection direction) {
        final boolean forward = direction == LoadDataDirection.Forward;
        if (forward) {
            if (isAscending()) {
                return newestMessage != null && newestMessage.getId() != null ? newestMessage.getId() : Long.MAX_VALUE;
            } else {
                return oldestMessage != null && oldestMessage.getId() != null ? oldestMessage.getId() : Long.MAX_VALUE;
            }
        } else {
            if (isAscending()) {
                return oldestMessage != null && oldestMessage.getId() != null ? oldestMessage.getId() : Long.MAX_VALUE;
            } else {
                return newestMessage != null && newestMessage.getId() != null ? newestMessage.getId() : Long.MAX_VALUE;
            }
        }
    }

    private void performMessagesLoaded(List<Message> messages, LoadDataDirection direction) {
        final boolean forward = direction == LoadDataDirection.Forward;
        if (forward) {
            if (isAscending()) {
                newestMessage = messages.size() > 0 ? messages.get(messages.size() - 1) : newestMessage;
            } else {
                oldestMessage = messages.size() > 0 ? messages.get(messages.size() - 1) : oldestMessage;
            }
        } else {
            if (isAscending()) {
                oldestMessage = messages.size() > 0 ? messages.get(0) : oldestMessage;
            } else {
                newestMessage = messages.size() > 0 ? messages.get(0) : newestMessage;
            }
        }
    }

    private boolean hasMore(final LoadDataDirection direction) {
        return hasMoreByDirection.get(direction) != null && hasMoreByDirection.get(direction);
    }

    private boolean isLoading(final LoadDataDirection direction) {
        return loadingByDirection.get(direction) != null && loadingByDirection.get(direction);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageAddedEvent(MessageEvent.AddedEvent event) {
        Log.i(TAG, "onMessageAddedEvent");
        if (supportedSpec.isSatisfiedBy(event.getMessage())) {
            markAllMessagesAsReadIfNecessary(event.getMessage());
            getPresenterView().onMessagesAdded(event.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageUpdatedEvent(MessageEvent.UpdatedEvent event) {
        Log.i(TAG, "onMessageUpdatedEvent");
        if (supportedSpec.isSatisfiedBy(event.getMessage())) {
            getPresenterView().onMessagesUpdated(event.getMessage());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageRemovedEvent(MessageEvent.RemovedEvent event) {
        Log.i(TAG, "onMessageRemovedEvent");
        if (supportedSpec.isSatisfiedBy(event.getMessage())) {
            getPresenterView().onMessagesRemoved(event.getMessage());
        }
    }

}

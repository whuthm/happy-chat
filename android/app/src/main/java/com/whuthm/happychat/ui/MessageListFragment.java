package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.whuthm.happychat.R;
import com.whuthm.happychat.app.MessageAppService;
import com.whuthm.happychat.common.rx.Observers;
import com.whuthm.happychat.common.util.ObjectUtils;
import com.whuthm.happychat.common.view.item.ItemAdapter;
import com.whuthm.happychat.common.view.item.ItemsRecyclerView;
import com.whuthm.happychat.common.view.item.TypedItem;
import com.whuthm.happychat.imlib.event.UserEvent;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.User;
import com.whuthm.happychat.imlib.vo.LoadDataDirection;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class MessageListFragment extends BaseConversationFragment implements MessageListPresenterView {
    
    private ItemsRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private ItemAdapter<TypedItem<?>> adapter;

    private MessageTypedItemProvider itemProvider;

    private ProgressBar backwardProgressBar;
    private ProgressBar forwardProgressBar;
    private MessageListPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_message_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backwardProgressBar = view.findViewById(R.id.pb_loading_backward);
        forwardProgressBar = view.findViewById(R.id.pb_loading_forward);

        adapter = new ItemAdapter<>(getContext());
        itemProvider = new MessageTypedItemProvider(imContext, MapperProviderFactory.get(imContext).messageItem());
        itemProvider.setMessageItemClickListener(new MessageItemClickListener() {
            @Override
            public void onBubbleClick(MessageItem messageItem) {

            }

            @Override
            public boolean onBubbleLongClick(MessageItem messageItem) {
                return true;
            }

            @Override
            public void onResendClick(MessageItem messageItem) {
                imContext.getService(MessageAppService.class).resendMessage(messageItem.getUid()).subscribe(Observers.<Message>empty());
            }

            @Override
            public void onSenderPortraitClick(MessageItem messageItem) {

            }
        });
        adapter.setItemViewProvider(itemProvider);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        // 根据RecyclerView的上下滚动来加载backward或者forward messages
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(-1)) {  // 加载backward messages
                    //onScrolledToTop()  Backward;
                    presenter.loadHistoryMessages(LoadDataDirection.Backward);
                } else if (!recyclerView.canScrollVertically(1)) {  // 加载forward messages
                    //onScrolledToBottom();
                    presenter.loadHistoryMessages(LoadDataDirection.Forward);
                }

            }
        });
        presenter = new MessageListPresenter(imContext, this, getConversationId(), getConversationType());
        presenter.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(UserEvent.ChangedEvent event) {
        Log.i(getTag(), "onUserChangedEvent");
        final User user = event.getUser();
        int count = adapter.getItemCount();
        int positionStart = -1;
        int positionEnd = -1;
        for (int i = 0; i < count; i++) {
            TypedItem<?> typedItem = adapter.getItem(i);
            if (typedItem != null
                    && typedItem.getItem() instanceof MessageItem) {
                MessageItem messageItem = (MessageItem) typedItem.getItem();
                if (user.getId().equals(messageItem.getSenderId())) {
                    final String displayName = PresenterUtils.getUserDisplayName(user);
                    boolean changed = false;
                    if (!ObjectUtils.equals(displayName, messageItem.getSenderDisplayName())) {
                        messageItem.setSenderDisplayName(displayName);
                        changed = true;
                    }
                    if (!ObjectUtils.equals(user.getPortraitUrl(), messageItem.getSenderPortraitUrl())) {
                        messageItem.setSenderPortraitUrl(user.getPortraitUrl());
                        changed = true;
                    }
                    if (user.getGender() != messageItem.getSenderGender()) {
                        messageItem.setSenderGender(user.getGender());
                        changed = true;
                    }
                    if (changed) {
                        if (i < positionStart || positionStart < 0) {
                            positionStart = i;
                        }
                        if (i > positionEnd) {
                            positionEnd = i;
                        }
                    }
                }
            }
        }
        if (positionStart>= 0 && positionEnd < count) {
            adapter.notifyItemRangeChanged(positionStart, positionEnd - positionStart + 1);
        }
    }

    @Override
    public void onHistoryMessagesLoaded(LoadDataDirection direction, List<Message> messages) {
        final boolean isForward = direction == LoadDataDirection.Forward;
        if (isForward) {
            if (forwardProgressBar != null) {
                forwardProgressBar.setVisibility(View.GONE);
            }
        } else {
            if (backwardProgressBar != null) {
                backwardProgressBar.setVisibility(View.GONE);
            }
        }

        if (messages.size() > 0) {
            final boolean isLastItemVisible = isLastItemVisible();
            final boolean isEmpty = adapter.getItemCount() <= 0;
            if (isForward) {
                adapter.addItems(itemProvider.getTypedItemsBy(messages));
            } else {
                adapter.addItems(0, itemProvider.getTypedItemsBy(messages));
            }
            if (isEmpty && presenter.isAscending()) {
                if (messages.size() <= 5) {
                    layoutManager.setStackFromEnd(false);
                } else {
                    layoutManager.setStackFromEnd(true);
                }
            }
            if (isLastItemVisible) {
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public void onHistoryMessagesLoadFailed(LoadDataDirection direction, Throwable tr) {
        if (direction == LoadDataDirection.Forward) {
            if (forwardProgressBar != null) {
                forwardProgressBar.setVisibility(View.GONE);
            }
        } else {
            if (backwardProgressBar != null) {
                backwardProgressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onHistoryMessagesLoadStarted(LoadDataDirection direction) {
        if (direction == LoadDataDirection.Forward) {
            if (forwardProgressBar != null) {
                forwardProgressBar.setVisibility(View.VISIBLE);
            }
        } else {
            if (backwardProgressBar != null) {
                backwardProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onMessagesAdded(Message message) {
        final boolean isLastItemVisible = isLastItemVisible();
        adapter.addItem(itemProvider.getTypedItemBy(message));
        if (isLastItemVisible) {
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageSentEvent(PresenterEvent.MessageSentEvent event) {
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onMessagesUpdated(Message message) {
        adapter.changeItem(itemProvider.getTypedItemBy(message));
    }

    @Override
    public void onMessagesRemoved(Message message) {
        adapter.removeItem(itemProvider.getTypedItemBy(message));
    }

    protected int getFirstVisibleItemPosition() {
        final LinearLayoutManager layoutManager = this.layoutManager;
        return layoutManager != null ? layoutManager.findFirstVisibleItemPosition() : NO_POSITION;
    }

    protected int getLastVisibleItemPosition() {
        final LinearLayoutManager layoutManager = this.layoutManager;
        return layoutManager != null ? layoutManager.findLastVisibleItemPosition() : NO_POSITION;
    }

    protected boolean isLastItemVisible() {
        final int lastVisibleItemPosition = getLastVisibleItemPosition();
        return lastVisibleItemPosition >= 0 && lastVisibleItemPosition >= adapter.getItemCount() - 1;
    }

    private boolean isFirstItemVisible() {
        return getFirstVisibleItemPosition() <= 0;
    }

}

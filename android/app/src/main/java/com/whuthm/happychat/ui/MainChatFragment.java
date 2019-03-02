package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.barran.lib.ui.recycler.VerticalDividerDecoration;
import com.barran.lib.view.text.ColorfulTextView;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.ConversationAppService;
import com.whuthm.happychat.common.util.ObjectUtils;
import com.whuthm.happychat.common.view.item.ItemAdapter;
import com.whuthm.happychat.common.view.item.ItemsRecyclerView;
import com.whuthm.happychat.imlib.ConversationService;
import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.event.UserEvent;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.User;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * 聊天列表
 * <p>
 * Created by huangming on 18/07/2018.
 */

public class MainChatFragment extends IMContextFragment {

    private ItemsRecyclerView recyclerView;

    private ColorfulTextView addConversation;

    private ItemAdapter<ConversationItem> adapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_conversation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_conversation_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        addConversation = view.findViewById(R.id.frag_conversation_list_add);

        adapter = new ItemAdapter<>(getContext());
        adapter.setItemViewProvider(new ItemAdapter.ItemViewProvider<ConversationItem>() {
            @Override
            public ItemAdapter.ItemViewHolder<ConversationItem> getItemViewHolder(ItemAdapter<ConversationItem> adapter, ViewGroup parent, int viewType) {
                return new ConversationItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.layout_conversation_item, parent, false), imContext);
            }

            @Override
            public int getItemViewType(ItemAdapter<ConversationItem> adapter, int position) {
                return 0;
            }
        });
        recyclerView.setOnItemClickListener(new ItemsRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View v, int position) {
                ConversationItem conversationItem = adapter.getItem(position);
                if (conversationItem != null) {
                    ConversationActivity.startConversation(getActivity(),
                            conversationItem.getId(), conversationItem.getType(), conversationItem.getTitle());
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalDividerDecoration(getActivity()));
        recyclerView.setAdapter(adapter);

        addConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        imContext.getService(ConversationAppService.class).getAllConversations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addDisposable(new DisposableObserver<List<Conversation>>() {

                    @Override
                    public void onNext(List<Conversation> value) {
                        List<ConversationItem> items = MapperProviderFactory.get(imContext).transform(MapperProviderFactory.get(imContext).conversationItem(), value);
                        Collections.sort(items, new ConversationItemComparator());
                        adapter.setItems(items);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConversationAddedEvent(ConversationEvent.AddedEvent event) {
        adapter.addItem(transform(event.getConversation()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConversationRemovedEvent(ConversationEvent.RemovedEvent event) {
        adapter.removeItem(transform(event.getConversation()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConversationUpdatedEvent(ConversationEvent.UpdatedEvent event) {
        adapter.changeItem(transform(event.getConversation()));
        if (event.getProperties().containsLatestMessageProperty()) {
            // TODO top
            if (adapter.getItems() != null) {
                Collections.sort(adapter.getItems(), new ConversationItemComparator());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(UserEvent.ChangedEvent event) {
        Log.i(getTag(), "onUserChangedEvent");
        final User user = event.getUser();
        int count = adapter.getItemCount();
        int positionStart = -1;
        int positionEnd = -1;
        for (int i = 0; i < count; i++) {
            boolean changed = false;
            ConversationItem conversationItem = adapter.getItem(i);
            if (conversationItem.getType() == ConversationType.PRIVATE) {
                final String displayName = PresenterUtils.getUserDisplayName(user);
                if (!ObjectUtils.equals(displayName, conversationItem.getTitle())) {
                    conversationItem.setTitle(displayName);
                    changed = true;
                }
                if (!ObjectUtils.equals(user.getPortraitUrl(), conversationItem.getPortraitUrl())) {
                    conversationItem.setPortraitUrl(user.getPortraitUrl());
                    changed = true;
                }
                if (user.getGender() != conversationItem.getTargetGender()) {
                    conversationItem.setTargetGender(user.getGender());
                    changed = true;
                }

            } else if (conversationItem.getType().isMultiUser()) {
                // 展示消息发送人
                String senderDisplayName = PresenterUtils.getMessageSenderDisplayName(imContext, conversationItem.getLatestMessage());
                if (!ObjectUtils.equals(senderDisplayName, conversationItem.getMessageSenderDisplayName())) {
                    conversationItem.setMessageSenderDisplayName(senderDisplayName);
                    changed = true;
                }
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
        if (positionStart>= 0 && positionEnd < count) {
            adapter.notifyItemRangeChanged(positionStart, positionEnd - positionStart + 1);
        }
    }

    private ConversationItem transform(Conversation conversation) {
        return MapperProviderFactory.get(imContext).conversationItem().transform(conversation);
    }


}

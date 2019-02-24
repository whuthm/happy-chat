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

    private ConversationService conversationService;
    private ConversationAppService conversationAppService;

    private ItemAdapter<ConversationItem> adapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_conversation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            ConversationActivity.startConversation(getContext(), "36bf78a8-881d-47ad-94a4-68abb05a60ac", ConversationType.PRIVATE, "vs");
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

        conversationService = imContext.getService(ConversationService.class);
        conversationAppService = imContext.getService(ConversationAppService.class);

        recyclerView = view.findViewById(R.id.recycler_view);
        addConversation = view.findViewById(R.id.frag_conversation_list_add);

        adapter = new ItemAdapter<>(getContext());
        adapter.setSortComparator(new ConversationItemComparator());
        adapter.setItemViewProvider(new ItemAdapter.ItemViewProvider<ConversationItem>() {
            @Override
            public ItemAdapter.ItemViewHolder<ConversationItem> getItemViewHolder(ItemAdapter<ConversationItem> adapter, ViewGroup parent, int viewType) {
                return new ConversationItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.layout_conversation_item, parent, false));
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

        conversationAppService.getAllConversations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addDisposable(new DisposableObserver<List<Conversation>>() {

                    @Override
                    public void onNext(List<Conversation> value) {
                        List<ConversationItem> items = MapperProviderFactory.get(imContext).transform(MapperProviderFactory.get(imContext).conversationItem(), value);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(UserEvent.ChangedEvent event) {
        Log.i(getTag(), "onUserChangedEvent");
        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            ConversationItem conversationItem = adapter.getItem(i);
            if (conversationItem != null && (event.getUser().getId().equals(conversationItem.getId()) /* || (event.getUser().getId().equals(conversationItem.getLatestMessageSenderUserId())**/)) {
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    private ConversationItem transform(Conversation conversation) {
        return MapperProviderFactory.get(imContext).conversationItem().transform(conversation);
    }


}

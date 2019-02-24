package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whuthm.happychat.R;
import com.whuthm.happychat.common.spec.Spec;
import com.whuthm.happychat.common.view.item.ItemAdapter;
import com.whuthm.happychat.common.view.item.ItemsRecyclerView;
import com.whuthm.happychat.common.view.item.TypedItem;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.event.UserEvent;
import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MessageListFragment extends BaseConversationFragment {
    
    private ItemsRecyclerView recyclerView;

    private Spec<Message> supportedSpec;

    private ItemAdapter<TypedItem<?>> adapter;

    private MessageTypedItemProvider itemProvider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_message_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ItemAdapter<>(getContext());
        itemProvider = new MessageTypedItemProvider(MapperProviderFactory.get(imContext).messageItem());
        adapter.setItemViewProvider(itemProvider);

        supportedSpec = new Spec<Message>() {
            @Override
            public boolean isSatisfiedBy(Message product) {
                return getConversationId().equals(product.getConversationId());
            }
        };
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageAddedEvent(MessageEvent.AddedEvent event) {
        Log.i(getTag(), "onMessageAddedEvent");
        if (supportedSpec.isSatisfiedBy(event.getMessage())) {
            adapter.addItem(itemProvider.getTypedItemBy(event.getMessage()));
        }
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageUpdatedEvent(MessageEvent.UpdatedEvent event) {
        Log.i(getTag(), "onMessageUpdatedEvent");
        if (supportedSpec.isSatisfiedBy(event.getMessage())) {
            adapter.changeItem(itemProvider.getTypedItemBy(event.getMessage()));
        }

    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageRemovedEvent(MessageEvent.RemovedEvent event) {
        Log.i(getTag(), "onMessageRemovedEvent");
        if (supportedSpec.isSatisfiedBy(event.getMessage())) {
            adapter.removeItem(itemProvider.getTypedItemBy(event.getMessage()));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserChangedEvent(UserEvent.ChangedEvent event) {
        Log.i(getTag(), "onUserChangedEvent");
        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            TypedItem<?> typedItem = adapter.getItem(i);
            if (typedItem != null
                    && typedItem.getItem() instanceof MessageItem) {
                MessageItem messageItem = (MessageItem) typedItem.getItem();
            }
        }
    }

}

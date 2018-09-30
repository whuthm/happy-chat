package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.barran.lib.adapter.BaseRecyclerAdapter;
import com.barran.lib.ui.recycler.VerticalDividerDecoration;
import com.barran.lib.view.text.ColorfulTextView;
import com.whuthm.happychat.R;
import com.whuthm.happychat.common.utils.EventBusUtils;
import com.whuthm.happychat.data.Constants;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.imlib.ConversationService;
import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 聊天列表
 * 
 * Created by huangming on 18/07/2018.
 */

public class MainConversationListFragment extends ChatContextFragment {
    
    private RecyclerView recyclerView;
    
    private ColorfulTextView addConversation;
    
    private ConversationAdapter mAdapter;
    
    private List<Conversation> conversationList;
    
    private ConversationService mConversationService;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        conversationList = new ArrayList<>();
        mConversationService = chatContext.getService(ConversationService.class);
        mConversationService.getAllConversations()
                .subscribe(new Observer<List<Conversation>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        
                    }
                    
                    @Override
                    public void onNext(List<Conversation> value) {
                        conversationList.addAll(value);
                        if (mAdapter != null) {
                            showConversations();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        
                    }
                    
                    @Override
                    public void onComplete() {
                        
                    }
                });
        
        EventBusUtils.safeRegister(new Object() {
            @Subscribe(threadMode = ThreadMode.MAIN)
            public void onConversationAddedEvent(ConversationEvent.AddedEvent event) {
                conversationList.add(0, event.getConversation());
                mAdapter.notifyItemInserted(0);
            }
            
            @Subscribe(threadMode = ThreadMode.MAIN)
            public void onConversationUpdatedEvent(ConversationEvent.UpdatedEvent event) {
                int size = conversationList.size();
                for (int i = 0; i < size; i++) {
                    if (conversationList.get(i).getId()
                            .equals(event.getConversation().getId())) {
                        mAdapter.notifyItemChanged(i);
                    }
                }
            }
            
            @Subscribe(threadMode = ThreadMode.MAIN)
            public void onConversationRemovedEvent(ConversationEvent.RemovedEvent event) {
                int size = conversationList.size();
                for (int i = 0; i < size; i++) {
                    if (conversationList.get(i).getId()
                            .equals(event.getConversation().getId())) {
                        mAdapter.notifyItemMoved(i, i);
                    }
                }
            }
        });
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_conversation, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            // TODO 创建群聊
            RetrofitClient.api().createGroup();
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
        
        recyclerView = view.findViewById(R.id.frag_conversation_list_recycler_view);
        addConversation = view.findViewById(R.id.frag_conversation_list_add);
        
        mAdapter = new ConversationAdapter();
        mAdapter.setItemClickListener(
                new BaseRecyclerAdapter.RecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Conversation conversation = conversationList.get(position);
                        ConversationActivity.startConversation(getActivity(),
                                conversation.getId(), conversation.getType());
                    }
                });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalDividerDecoration(getActivity()));
        recyclerView.setAdapter(mAdapter);
        
        showConversations();
        
        addConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 加入默认聊天室
                final String id = "1";
                ConversationActivity.startConversation(getActivity(), id,
                        ConversationType.GROUP);
            }
        });
    }
    
    private void showConversations() {
        if (conversationList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            addConversation.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            addConversation.setVisibility(View.GONE);
        }
    }
    
    private class ConversationAdapter extends BaseRecyclerAdapter<ConversationHolder> {
        
        @Override
        protected ConversationHolder createHolder(ViewGroup parent, int viewType) {
            return new ConversationHolder(LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_conversation_item, parent, false));
        }
        
        @Override
        public void onBindViewHolder(@NonNull ConversationHolder holder, int position) {
            holder.update(conversationList.get(position));
        }
        
        @Override
        public int getItemCount() {
            return conversationList != null ? conversationList.size() : 0;
        }
    }
    
    private class ConversationHolder extends RecyclerView.ViewHolder {
        
        private ImageView icon;
        
        private TextView name;
        
        private TextView lastMessage;
        
        public ConversationHolder(View itemView) {
            super(itemView);
            
            icon = itemView.findViewById(R.id.layout_conversation_icon);
            name = itemView.findViewById(R.id.layout_conversation_name);
            lastMessage = itemView.findViewById(R.id.layout_conversation_content);
        }
        
        private void update(Conversation conversation) {
            // TODO NEED FIX latestMessage is null
            Message latestMessage = conversation.getLatestMessage();
            if (latestMessage != null) {
                name.setText(latestMessage.getSenderUserId());
                lastMessage.setText(latestMessage.getBody());
            }
            else {
                name.setText("id:" + conversation.getId());
                lastMessage.setText("type:" + conversation.getType().getValue());
            }
        }
    }
}

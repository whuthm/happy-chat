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
import com.barran.lib.app.BaseFragment;
import com.barran.lib.ui.recycler.VerticalDividerDecoration;
import com.barran.lib.view.text.ColorfulTextView;
import com.whuthm.happychat.R;
import com.whuthm.happychat.data.Constants;
import com.whuthm.happychat.data.DBOperator;
import com.whuthm.happychat.domain.model.Conversation;

import java.util.List;

/**
 * 聊天列表
 * 
 * Created by huangming on 18/07/2018.
 */

public class MainConversationFragment extends BaseFragment {
    
    private RecyclerView recyclerView;
    
    private ColorfulTextView addConversation;
    
    private ConversationAdapter mAdapter;
    
    private List<Conversation> conversationList;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        conversationList = DBOperator.getConversations();
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_conversation, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            addConversation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // test
    private void addConversation() {
        List<Conversation> conversations = DBOperator.getConversations(1);
        String id;
        if (conversations.size() > 0) {
            try {
                int idInt = Integer.parseInt(conversations.get(0).getConversionId());
                id = String.valueOf(idInt + 1);
            } catch (NumberFormatException e) {
                id = "0";
            }
        }
        else {
            id = String.valueOf(1);
        }
        Conversation conversation = new Conversation();
        conversation.setConversionId(id);
        conversation.setConversionName("狗子起来嗨 " + id);
        conversation.setCreateTime(System.currentTimeMillis());
        conversation.setConversationType(Constants.ConversationType.SINGLE.ordinal());
        DBOperator.addConversation(conversation);
        
        conversationList.add(conversation);
        mAdapter.notifyDataSetChanged();
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
        mAdapter.setItemClickListener(new BaseRecyclerAdapter.RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(Constants.KEY_CONVErSATION_ID,
                                conversationList.get(position).getConversionId());
                startActivity(intent);
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new VerticalDividerDecoration(getActivity()));
        recyclerView.setAdapter(mAdapter);
        
        if (conversationList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            addConversation.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            addConversation.setVisibility(View.GONE);
        }
        
        addConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 前期加入默认聊天室，后期可以创建聊天室
                addConversation();
            }
        });
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
            name.setText(conversation.getConversionName());
            lastMessage.setText(conversation.getLastMessageBody());
        }
    }
}

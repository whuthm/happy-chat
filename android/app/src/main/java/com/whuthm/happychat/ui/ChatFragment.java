package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.barran.lib.adapter.BaseRecyclerAdapter;
import com.barran.lib.app.BaseFragment;
import com.barran.lib.view.text.ColorfulTextView;
import com.whuthm.happychat.R;
import com.whuthm.happychat.data.Constants;
import com.whuthm.happychat.data.DBOperator;
import com.whuthm.happychat.imlib.ConversationService;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.ui.item.TextMessageItem;

import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 聊天界面
 *
 * Created by huangming on 18/07/2018.
 */

public class ChatFragment extends BaseFragment {
    
    private String conversationId;
    
    private List<Message> messageList;
    
    private RecyclerView recyclerView;
    
    private MessageAdapter mAdapter;
    
    private EditText input;
    
    private ColorfulTextView btnSend;
    
    private IFragAction action;
    
    private Disposable mDisposable;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        conversationId = getArguments().getString(Constants.KEY_CONVERSATION_ID);
        if (TextUtils.isEmpty(conversationId)) {
            getActivity().finish();
            return;
        }
        
        messageList = DBOperator.getMessages(conversationId,
                Constants.MESSAGE_PAGE_COUNT);
        if (messageList.size() > 0) {
            Collections.reverse(messageList);
        }

        // TODO FIX
//        mDisposable = ConversationService.instance()
//                .subscribeMessage(new Consumer<Message>() {
//                    @Override
//                    public void accept(Message message) throws Exception {
//
//                        int index;
//                        switch (message.getStatus()) {
//                            case Constants.ACTION_ADD:
//                                messageList.add(message);
//                                mAdapter.notifyItemInserted(messageList.size() - 1);
//                                break;
//                            case Constants.ACTION_DELETE:
//                                index = messageList.indexOf(message);
//                                if (index >= 0) {
//                                    messageList.remove(message);
//                                    mAdapter.notifyItemRemoved(index);
//                                }
//                                break;
//                            case Constants.ACTION_UPDATE:
//                                index = messageList.indexOf(message);
//                                if (index >= 0) {
//                                    messageList.set(index, message);
//                                    mAdapter.notifyItemChanged(index);
//                                }
//                                break;
//                        }
//                    }
//                });
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
    
    public void setFragAction(IFragAction action) {
        this.action = action;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat_setting, menu);
    }
    
    // test
    private void addMessage() {
        addMessage(null);
    }
    
    private void addMessage(String text) {
        List<Message> messages = DBOperator.getMessages(conversationId, 1);
        long id;
        if (messages.size() > 0) {
            id = messages.get(0).getMessageId();
        }
        else {
            id = 1L;
        }
        Message message = new Message();
        message.setMessageId(id);
        if (text == null) {
            message.setBody("狗子起来嗨 " + id);
        }
        else {
            message.setBody(text);
        }
        message.setFromUserId("111");
        message.setTime(System.currentTimeMillis());
        message.setToUserId(conversationId);
        DBOperator.addMessage(message);
        
        messageList.add(message);
        mAdapter.notifyDataSetChanged();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_chat_setting) {
            if (action != null) {
                action.setting();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_chat, container, false);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        final SwipeRefreshLayout swipeRefreshLayout = view
                .findViewById(R.id.frag_chat_swipe_refresh_layout);
        swipeRefreshLayout
                .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        List<Message> messages = DBOperator.getMessages(conversationId,
                                Constants.MESSAGE_PAGE_COUNT, messageList.size());
                        if (messages.size() > 0) {
                            Collections.reverse(messages);
                            messageList.addAll(0, messages);
                            mAdapter.notifyDataSetChanged();
                        }
                        
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
        
        recyclerView = view.findViewById(R.id.frag_message_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        recyclerView.setAdapter(mAdapter = new MessageAdapter());
        
        input = view.findViewById(R.id.frag_chat_input);
        btnSend = view.findViewById(R.id.frag_chat_send);
        
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    addMessage(text);
                    input.setText("");
                }
                else {
                    addMessage();
                }
            }
        });
    }
    
    private class MessageAdapter extends BaseRecyclerAdapter<TextHolder> {
        
        @Override
        protected TextHolder createHolder(ViewGroup parent, int viewType) {
            return new TextHolder(new TextMessageItem(getContext()));
        }
        
        @Override
        public void onBindViewHolder(@NonNull TextHolder holder, int position) {
            holder.update(messageList.get(position));
        }
        
        @Override
        public int getItemCount() {
            return messageList != null ? messageList.size() : 0;
        }
    }
    
    private class TextHolder extends RecyclerView.ViewHolder {
        
        public TextHolder(View itemView) {
            super(itemView);
        }
        
        public void update(Message message) {
            ((TextMessageItem) itemView).showMessage(message);
        }
    }
    
    public static interface IFragAction {
        void setting();
    }
}

package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barran.lib.adapter.BaseRecyclerAdapter;
import com.whuthm.happychat.R;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.ui.item.TextMessageItem;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment extends BaseConversationFragment {

    private List<Message> messageList;

    private RecyclerView recyclerView;

    private MessageAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.frag_message_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
        recyclerView.setAdapter(mAdapter = new MessageAdapter());
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

}

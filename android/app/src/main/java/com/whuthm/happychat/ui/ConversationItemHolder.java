package com.whuthm.happychat.ui;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whuthm.happychat.R;
import com.whuthm.happychat.common.view.item.ItemAdapter;

public class ConversationItemHolder extends ItemAdapter.ItemViewHolder<ConversationItem> {

    private ImageView iconView;
    private TextView titleTextView;
    private TextView contentTextView;

    public ConversationItemHolder(View itemView) {
        super(itemView);
        iconView = itemView.findViewById(R.id.layout_conversation_icon);
        titleTextView = itemView.findViewById(R.id.layout_conversation_title);
        contentTextView = itemView.findViewById(R.id.layout_conversation_content);
    }

    @Override
    protected void bindView(ConversationItem conversationItem, int position) {
        super.bindView(conversationItem, position);
        Log.i("WHUTHM", conversationItem.getTitle() + ",,," + conversationItem.getPortraitUrl());
        titleTextView.setText(conversationItem.getTitle());
        contentTextView.setText(conversationItem.getContent());

        Glide.with(context)
                .load(conversationItem.getPortraitUrl())
                .apply(RequestOptions.centerCropTransform()
                        .placeholder(R.drawable.dog1)
                        .error(R.drawable.dog1))
                .into(iconView);

    }
}

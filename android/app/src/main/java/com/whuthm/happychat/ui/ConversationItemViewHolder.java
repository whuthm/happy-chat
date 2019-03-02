package com.whuthm.happychat.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.UserAppService;
import com.whuthm.happychat.common.view.item.ItemAdapter;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.ConversationType;
import com.whuthm.happychat.utils.DateUtils;

public class ConversationItemViewHolder extends ItemAdapter.ItemViewHolder<ConversationItem> {

    private ImageView portraitView;
    private TextView titleTextView;
    private TextView contentTextView;
    private TextView timeTextView;
    private View redDotView;
    private TextView digitalRedDotView;

    private final IMContext imContext;

    public ConversationItemViewHolder(View itemView, IMContext imContext) {
        super(itemView);
        portraitView = itemView.findViewById(R.id.iv_portrait);
        titleTextView = itemView.findViewById(R.id.tv_title);
        contentTextView = itemView.findViewById(R.id.tv_content);
        timeTextView = itemView.findViewById(R.id.tv_time);
        redDotView = itemView.findViewById(R.id.view_red_dot);
        digitalRedDotView = itemView.findViewById(R.id.tv_digital_red_dot);
        this.imContext = imContext;
    }

    @Override
    protected void bindView(ConversationItem conversationItem, int position) {
        super.bindView(conversationItem, position);
        titleTextView.setText(conversationItem.getTitle());
        contentTextView.setText(conversationItem.getContent());
        timeTextView.setText(DateUtils.getTimestampString(conversationItem.getLatestMessageTime()));
        Glide.with(context)
                .load(conversationItem.getPortraitUrl())
                .apply(RequestOptions.centerCropTransform()
                        .placeholder(R.drawable.dog1)
                        .error(R.drawable.dog1))
                .into(portraitView);

        final int unreadCount = conversationItem.getUnreadCount();
        if (unreadCount > 0) {
            if (conversationItem.getNotificationStatus() == Conversation.NotificationStatus.DO_NOT_DISTURB) {
                redDotView.setVisibility(View.VISIBLE);
                digitalRedDotView.setVisibility(View.GONE);
            } else {
                digitalRedDotView.setText(Integer.toString(unreadCount));
                redDotView.setVisibility(View.GONE);
                digitalRedDotView.setVisibility(View.VISIBLE);
            }
        } else {
            redDotView.setVisibility(View.GONE);
            digitalRedDotView.setVisibility(View.GONE);
        }

        if (conversationItem.getType() == ConversationType.PRIVATE) {
            imContext.getService(UserAppService.class).getOrFetchUser(conversationItem.getId());
        } if (conversationItem.getType() == ConversationType.GROUP) {
            // TODO: getOrFetchGroup
        }

    }
}

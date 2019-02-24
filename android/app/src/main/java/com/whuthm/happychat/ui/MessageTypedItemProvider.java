package com.whuthm.happychat.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.whuthm.happychat.R;
import com.whuthm.happychat.common.view.item.ItemAdapter;
import com.whuthm.happychat.common.view.item.TypedItem;
import com.whuthm.happychat.common.view.item.TypedItemViewHolder;
import com.whuthm.happychat.common.view.item.TypedItemViewProvider;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageBody;
import com.whuthm.happychat.imlib.model.MessageTag;
import com.whuthm.happychat.imlib.model.message.TextMessageBody;

public class MessageTypedItemProvider extends TypedItemViewProvider {

    public static final int TYPE_EMPTY = 100;

    //未读分割线
    public static final int TYPE_UNREAD_DIVIDER = TYPE_EMPTY - 1;

    // 正常消息
    public static final int TYPE_UNKNOWN_SEND = TYPE_EMPTY + 1;
    public static final int TYPE_UNKNOWN_RECEIVE = TYPE_UNKNOWN_SEND + 1;
    public static final int TYPE_TXT_SEND = TYPE_UNKNOWN_RECEIVE + 1;
    public static final int TYPE_TXT_RECEIVE = TYPE_TXT_SEND + 1;
    public static final int TYPE_IMG_SEND = TYPE_TXT_RECEIVE + 1;
    public static final int TYPE_IMG_RECEIVE = TYPE_IMG_SEND + 1;

    MessageItemClickListener messageItemClickListener;

    public void setMessageItemClickListener(MessageItemClickListener messageItemClickListener) {
        this.messageItemClickListener = messageItemClickListener;
    }

    @Override
    protected ItemAdapter.ItemViewHolder<?> onCreateItemViewHolder(ItemAdapter<TypedItem<?>> adapter, ViewGroup parent, int viewType) {
        final Context context = adapter.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case TYPE_TXT_SEND:
                return new TxtMessageViewHolder(inflater.inflate(R.layout.message_item_txt_send, parent, false));
            case TYPE_TXT_RECEIVE:
                return new TxtMessageViewHolder(inflater.inflate(R.layout.message_item_txt_receive, parent, false));
        }
        return null;
    }

    @Override
    protected void onItemViewHolderCreated(ItemAdapter.ItemViewHolder<?> holder) {
        super.onItemViewHolderCreated(holder);
        if (holder instanceof MessageViewHolder) {
            ((MessageViewHolder) holder).setItemClickListener(messageItemClickListener);
        }
    }

    TypedItem<?> getTypedItemBy(Message message) {
        if (message == null || message.getBodyObject() == null || message.getType() == null) {
            return new TypedItem<>(TYPE_EMPTY, message);
        }
        final String messageType = message.getType();
        final boolean isSendDirection = message.getDirection() == Message.Direction.SEND;

        final int type;
        switch (messageType) {
            case MessageTag.TYPE_TXT:
                type = isSendDirection ? TYPE_TXT_SEND : TYPE_TXT_RECEIVE;
                break;
            case MessageTag.TYPE_IMG:
                type = isSendDirection ? TYPE_TXT_SEND : TYPE_TXT_RECEIVE;
                break;
            default:
                type = isSendDirection ? TYPE_UNKNOWN_SEND : TYPE_UNKNOWN_RECEIVE;
                break;
        }
        return new TypedItem<>(type, new MessageItem(message));
    }

    public abstract static class MessageViewHolder<BODY extends MessageBody> extends ItemAdapter.ItemViewHolder<MessageItem> {

        protected TextView senderNameView;
        protected ImageView senderPortraitView;
        protected View bubbleLayout;

        private  MessageItemClickListener itemClickListener;

        public MessageViewHolder(View itemView) {
            super(itemView);
        }

        public void setItemClickListener(MessageItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void bindView(final MessageItem messageItem, int position) {
            super.bindView(messageItem, position);
            final MessageBody body = messageItem.getBody();
            if (body != null) {
                bindView(messageItem, (BODY) body, position);
            }

            if (senderNameView != null) {
                senderNameView.setText(messageItem.getSenderDisplayName());
            }

            if (senderPortraitView != null) {
                Glide.with(context)
                        .load(messageItem.getSenderPortraitUrl())
                        .apply(RequestOptions.centerCropTransform()
                                .placeholder(R.drawable.dog1)
                                .error(R.drawable.ic_launcher))
                        .into(senderPortraitView);
            }

            if (bubbleLayout != null) {
                bubbleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.onBubbleClick(messageItem);
                        }
                    }
                });
            }

            if (bubbleLayout != null) {
                bubbleLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return itemClickListener != null && itemClickListener.onBubbleLongClick(messageItem);
                    }
                });
            }

            if (senderPortraitView != null) {
                senderPortraitView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.onSenderPortraitClick(messageItem);
                        }
                    }
                });
            }
        }

        protected abstract void bindView(MessageItem messageItem, BODY body, int position);
    }

    public static class TxtMessageViewHolder extends MessageViewHolder<TextMessageBody> {

        private TextView txtView;

        public TxtMessageViewHolder(View itemView) {
            super(itemView);
            txtView = findViewById(R.id.tv_txt);
        }

        @Override
        protected void bindView(MessageItem messageItem, TextMessageBody body, int position) {
            txtView.setText(body.getText());
        }
    }

}

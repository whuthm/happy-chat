package com.whuthm.happychat.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.UserAppService;
import com.whuthm.happychat.common.util.Mapper;
import com.whuthm.happychat.common.view.item.ItemAdapter;
import com.whuthm.happychat.common.view.item.TypedItem;
import com.whuthm.happychat.common.view.item.TypedItemViewProvider;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Message;
import com.whuthm.happychat.imlib.model.MessageBody;
import com.whuthm.happychat.imlib.model.MessageTag;
import com.whuthm.happychat.imlib.model.message.TextMessageBody;
import com.whuthm.happychat.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

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
    private final Mapper<Message, MessageItem> mapper;
    private final IMContext imContext;

    public MessageTypedItemProvider(IMContext imContext, Mapper<Message, MessageItem> mapper) {
        this.imContext = imContext;
        this.mapper = mapper;
    }

    public void setMessageItemClickListener(MessageItemClickListener messageItemClickListener) {
        this.messageItemClickListener = messageItemClickListener;
    }

    @Override
    protected ItemAdapter.ItemViewHolder<?> onCreateItemViewHolder(ItemAdapter<TypedItem<?>> adapter, ViewGroup parent, int viewType) {
        final Context context = adapter.getContext();

        final LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case TYPE_TXT_SEND:
                return new TextMessageItemViewHolder(inflater.inflate(R.layout.message_item_txt_send, parent, false));
            case TYPE_TXT_RECEIVE:
                return new TextMessageItemViewHolder(inflater.inflate(R.layout.message_item_txt_receive, parent, false));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onItemViewHolderCreated(ItemAdapter<TypedItem<?>> adapter, ItemAdapter.ItemViewHolder<?> holder) {
        super.onItemViewHolderCreated(adapter, holder);
        if (holder instanceof MessageItemViewHolder) {
            ((MessageItemViewHolder) holder).setItemClickListener(messageItemClickListener);
            ((MessageItemViewHolder) holder).setItemAdapter(adapter);
            ((MessageItemViewHolder) holder).setImContext(imContext);
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
        return new TypedItem<>(type, mapper.transform(message));
    }

    List<TypedItem<?>> getTypedItemsBy(List<Message> messages) {
        List<TypedItem<?>> items = new ArrayList<>();
        for (Message message : messages) {
            items.add(getTypedItemBy(message));
        }
        return items;
    }

    public abstract static class MessageItemViewHolder<BODY extends MessageBody> extends ItemAdapter.ItemViewHolder<MessageItem> {

        protected TextView senderNameView;
        protected ImageView senderPortraitView;
        protected View bubbleLayout;
        protected TextView timeView;
        protected View resendView;
        protected View sendingView;

        private  MessageItemClickListener itemClickListener;
        private ItemAdapter<TypedItem<?>> itemAdapter;
        private IMContext imContext;

        public MessageItemViewHolder(View itemView) {
            super(itemView);
            senderNameView = findViewById(R.id.tv_sender_name);
            senderPortraitView = findViewById(R.id.iv_sender_portrait);
            bubbleLayout = findViewById(R.id.layout_bubble);
            timeView = findViewById(R.id.tv_time);
            resendView = findViewById(R.id.iv_resend);
            sendingView = findViewById(R.id.pb_sending);
        }

        public void setItemClickListener(MessageItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setItemAdapter(ItemAdapter<TypedItem<?>> itemAdapter) {
            this.itemAdapter = itemAdapter;
        }

        public void setImContext(IMContext imContext) {
            this.imContext = imContext;
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
                if (messageItem.getConversationType().isMultiUser()) {
                    senderNameView.setText(messageItem.getSenderDisplayName());
                    senderNameView.setVisibility(View.VISIBLE);
                } else {
                    senderNameView.setVisibility(View.GONE);
                }
            }

            if (timeView != null) {
                if (position == 0) {
                    timeView.setText(DateUtils.getTimestampString(messageItem.getSendTime()));
                    timeView.setVisibility(View.VISIBLE);
                } else {
                    // show time stamp if interval with last message is > 30 seconds
                    final TypedItem<?> previousTypedItem = itemAdapter != null ? itemAdapter.getItem(position - 1) : null;
                    final MessageItem previousMessageItem = previousTypedItem != null && previousTypedItem.getItem() instanceof MessageItem ? (MessageItem) previousTypedItem.getItem() : null;
                    if (previousMessageItem != null && DateUtils.isCloseEnough(messageItem.getSendTime(), previousMessageItem.getSendTime())) {
                        timeView.setVisibility(View.GONE);
                    } else {
                        timeView.setText(DateUtils.getTimestampString(messageItem.getSendTime()));
                        timeView.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (senderPortraitView != null) {
                Glide.with(context)
                        .load(messageItem.getSenderPortraitUrl())
                        .apply(RequestOptions.centerCropTransform()
                                .placeholder(R.drawable.dog1)
                                .error(R.drawable.dog1))
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

            if (resendView != null) {
                resendView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.onResendClick(messageItem);
                        }
                    }
                });
                resendView.setVisibility(messageItem.getSentStatus() == Message.SentStatus.FAILED ? View.VISIBLE : View.GONE);
            }

            if (sendingView != null) {
                sendingView.setVisibility(messageItem.getSentStatus() == Message.SentStatus.SENDING ? View.VISIBLE : View.GONE);
            }


            if ((senderPortraitView != null || senderNameView != null) && imContext != null) {
                imContext.getService(UserAppService.class).getOrFetchUser(messageItem.getSenderId());
            }
        }

        protected abstract void bindView(MessageItem messageItem, BODY body, int position);

    }

    public static class TextMessageItemViewHolder extends MessageItemViewHolder<TextMessageBody> {

        private TextView txtView;

        public TextMessageItemViewHolder(View itemView) {
            super(itemView);
            txtView = findViewById(R.id.tv_txt);
        }

        @Override
        protected void bindView(MessageItem messageItem, TextMessageBody body, int position) {
            txtView.setText(body.getText());
        }
    }

}

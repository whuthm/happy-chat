package com.whuthm.happychat.ui;

public interface MessageItemClickListener {

    void onBubbleClick(MessageItem messageItem);

    boolean onBubbleLongClick(MessageItem messageItem);

    void onResendClick(MessageItem messageItem);

    void onSenderPortraitClick(MessageItem messageItem);

}

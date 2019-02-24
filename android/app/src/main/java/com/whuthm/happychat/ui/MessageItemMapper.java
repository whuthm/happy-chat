package com.whuthm.happychat.ui;

import com.whuthm.happychat.common.util.Mapper;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Message;

class MessageItemMapper implements Mapper<Message, MessageItem> {

    private final IMContext imContext;

    MessageItemMapper(IMContext imContext) {
        this.imContext = imContext;
    }

    @Override
    public MessageItem transform(Message message) {

        return new MessageItem(message);
    }

}

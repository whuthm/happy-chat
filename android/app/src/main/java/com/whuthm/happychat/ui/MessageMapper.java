package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageMapper {


    static MessageItem transform(Message message) {
        return new MessageItem(message);
    }

    static List<MessageItem> transform(List<Message> messages) {
        final List<MessageItem> items = new ArrayList<>(messages.size());
        for (Message message : messages) {
            items.add(transform(message));
        }
        return items;
    }

}

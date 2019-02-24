package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Conversation;

import org.greenrobot.greendao.annotation.Convert;

import java.util.ArrayList;
import java.util.List;

class ConversationMapper {

    static ConversationItem transform(Conversation conversation) {
        return new ConversationItem(conversation);
    }

    static List<ConversationItem> transform(List<Conversation> conversations) {
        final List<ConversationItem> items = new ArrayList<>(conversations.size());
        for (Conversation conversation : conversations) {
            items.add(transform(conversation));
        }
        return items;
    }

}

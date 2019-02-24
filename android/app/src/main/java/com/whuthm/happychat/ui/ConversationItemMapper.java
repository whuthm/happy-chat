package com.whuthm.happychat.ui;

import com.whuthm.happychat.common.util.Mapper;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Conversation;

class ConversationItemMapper implements Mapper<Conversation, ConversationItem>{

    private final IMContext imContext;

    ConversationItemMapper(IMContext imContext) {
        this.imContext = imContext;
    }

    @Override
    public ConversationItem transform(Conversation conversation) {
        return new ConversationItem(imContext, conversation);
    }


}

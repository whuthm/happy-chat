package com.whuthm.happychat.ui;

import com.whuthm.happychat.common.util.Mapper;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MapperProvider {

    private static final String KEY_MESSAGE_ITEM = "message_item";
    private static final String KEY_CONVERSATION_ITEM = "conversation_item";

    private final IMContext imContext;

    private final  WeakHashMap<String, Mapper<?, ?>> mappers = new WeakHashMap<>();

    public MapperProvider(IMContext imContext) {
        this.imContext = imContext;
    }

    <FROM, TO> List<TO> transform(Mapper<FROM, TO> mapper, List<FROM> froms) {
        final List<TO> items = new ArrayList<>(froms.size());
        for (FROM from : froms) {
            items.add(mapper.transform(from));
        }
        return items;
    }

    Mapper<Conversation, ConversationItem> conversationItem() {
        Mapper<Conversation, ConversationItem> mapper = getMapperBy(KEY_CONVERSATION_ITEM);
        if (mapper == null) {
            mapper = new ConversationItemMapper(imContext);
            mappers.put(KEY_CONVERSATION_ITEM, mapper);
        }
        return mapper;
    }

     Mapper<Message, MessageItem> messageItem() {
        Mapper<Message, MessageItem> mapper = getMapperBy(KEY_MESSAGE_ITEM);
        if (mapper == null) {
            mapper = new MessageItemMapper(imContext);
            mappers.put(KEY_MESSAGE_ITEM, mapper);
        }
        return mapper;
    }

    @SuppressWarnings("unchecked")
    private  <FROM, TO> Mapper<FROM, TO> getMapperBy(String key) {
        return (Mapper<FROM, TO>) mappers.get(key);
    }


}

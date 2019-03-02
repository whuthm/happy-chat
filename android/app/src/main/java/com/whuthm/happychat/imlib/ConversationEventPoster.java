package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.event.EventPoster;
import com.whuthm.happychat.imlib.event.EventPosterWrapper;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.vo.ConversationProperties;

class ConversationEventPoster extends EventPosterWrapper implements ConversationEvent.Poster {
    public ConversationEventPoster(EventPoster wrapped) {
        super(wrapped);
    }

    @Override
    public void postConversationUpdated(Conversation conversation, ConversationProperties properties) {
        if (conversation == null || properties == null || !properties.containsProperties()) {
            return;
        }
        post(new ConversationEvent.UpdatedEvent(conversation, properties));
    }

    @Override
    public void postConversationRemoved(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        post(new ConversationEvent.RemovedEvent(conversation));
    }

    @Override
    public void postConversationAdded(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        post(new ConversationEvent.AddedEvent(conversation));
    }

}

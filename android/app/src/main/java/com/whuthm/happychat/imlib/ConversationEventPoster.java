package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.event.EventPoster;
import com.whuthm.happychat.imlib.event.EventPosterWrapper;
import com.whuthm.happychat.imlib.model.Conversation;

class ConversationEventPoster extends EventPosterWrapper implements ConversationEvent.Poster {
    public ConversationEventPoster(EventPoster wrapped) {
        super(wrapped);
    }

    @Override
    public void postConversationUpdated(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        post(new ConversationEvent.UpdatedEvent(conversation));
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

    @Override
    public void postConversationUnreadCount(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        post(new ConversationEvent.UpdatedEvent(conversation, ConversationEvent.UpdatedEvent.Type.UnreadCount));
    }
}

package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.event.EventPoster;
import com.whuthm.happychat.imlib.event.EventPosterWrapper;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.model.Message;

class MessageEventPoster extends EventPosterWrapper implements MessageEvent.Poster  {
    public MessageEventPoster(EventPoster wrapped) {
        super(wrapped);
    }

    @Override
    public void postMessageAdded(Message message) {
        if (message == null) {
            return;
        }
        post(new MessageEvent.AddedEvent(message));
    }

    @Override
    public void postMessageRemoved(Message message) {
        if (message == null) {
            return;
        }
        post(new MessageEvent.RemovedEvent(message));
    }

    @Override
    public void postMessageUpdated(Message message) {
        if (message == null) {
            return;
        }
        post(new MessageEvent.UpdatedEvent(message));
    }
}

package com.whuthm.happychat.imlib.event;


import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by huangming on 2017/8/18.
 */

public class MessageEvent {

    private final Message message;

    private MessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public static class AddedEvent extends MessageEvent {

        public AddedEvent(Message message) {
            super(message);
        }
    }

    public static class RemovedEvent extends MessageEvent {

        public RemovedEvent(Message message) {
            super(message);
        }
    }

    public static class UpdatedEvent extends MessageEvent {

        public UpdatedEvent(Message message) {
            super(message);
        }

    }

    public interface Poster extends EventPoster {
        void postMessageAdded(Message message);

        void postMessageRemoved(Message message);

        void postMessageUpdated(Message message);
    }

}

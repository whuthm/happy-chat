package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Message;

public class PresenterEvent {

    public static class MessageSentEvent {
        private final Message message;

        public MessageSentEvent(Message message) {
            this.message = message;
        }

        public Message getMessage() {
            return message;
        }
    }

}

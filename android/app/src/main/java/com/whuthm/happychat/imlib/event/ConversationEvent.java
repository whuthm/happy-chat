package com.whuthm.happychat.imlib.event;


import com.whuthm.happychat.imlib.model.Conversation;

/**
 * Created by huangming on 2017/8/18.
 */

public class ConversationEvent {

    private final Conversation conversation;

    private ConversationEvent(Conversation conversation) {
        this.conversation = conversation;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public static class RemovedEvent extends ConversationEvent {

        public RemovedEvent(Conversation conversation) {
            super(conversation);
        }
    }

    public static class AddedEvent extends ConversationEvent {

        public AddedEvent(Conversation conversation) {
            super(conversation);
        }
    }

    public static class UpdatedEvent extends ConversationEvent {

        public enum Type {
            UnreadCount,
            Other
        }

        private final Type type;

        public UpdatedEvent(Conversation conversation) {
            this(conversation, Type.Other);
        }

        public UpdatedEvent(Conversation conversation, Type type) {
            super(conversation);
            this.type = type;
        }

        public Type getType() {
            return type;
        }
    }


}

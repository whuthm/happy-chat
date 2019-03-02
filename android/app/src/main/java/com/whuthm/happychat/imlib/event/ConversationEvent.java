package com.whuthm.happychat.imlib.event;


import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.vo.ConversationProperties;

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

        public final ConversationProperties properties;

        public UpdatedEvent(Conversation conversation, ConversationProperties properties) {
            super(conversation);
            this.properties = properties;
        }

        public ConversationProperties getProperties() {
            return properties;
        }
    }

    public interface Poster extends EventPoster {
        void postConversationUpdated(Conversation conversation, ConversationProperties properties);

        void postConversationRemoved(Conversation conversation);

        void postConversationAdded(Conversation conversation);

    }


}

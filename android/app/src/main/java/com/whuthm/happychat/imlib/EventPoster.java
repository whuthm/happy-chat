package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.event.ChatEvent;
import com.whuthm.happychat.imlib.event.ConversationEvent;
import com.whuthm.happychat.imlib.event.MessageEvent;
import com.whuthm.happychat.imlib.model.Conversation;
import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by huangming on 2017/9/9.
 */

class EventPoster {

    void postConversationUpdated(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        EventBus.getDefault().post(new ConversationEvent.UpdatedEvent(conversation));
    }

    void postConversationRemoved(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        EventBus.getDefault().post(new ConversationEvent.RemovedEvent(conversation));
    }

    void postConversationAdded(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        EventBus.getDefault().post(new ConversationEvent.AddedEvent(conversation));
    }

    void postConversationUnreadCount(Conversation conversation) {
        if (conversation == null) {
            return;
        }
        EventBus.getDefault().post(new ConversationEvent.UpdatedEvent(conversation, ConversationEvent.UpdatedEvent.Type.UnreadCount));
    }

    void postMessageAdded(Message message) {
        if (message == null) {
            return;
        }
        EventBus.getDefault().post(new MessageEvent.AddedEvent(message));
    }

    void postMessageRemoved(Message message) {
        if (message == null) {
            return;
        }
        EventBus.getDefault().post(new MessageEvent.RemovedEvent(message));
    }

    void postMessageUpdated(Message message) {
        if (message == null) {
            return;
        }
        EventBus.getDefault().post(new MessageEvent.UpdatedEvent(message));
    }

    void postTotalUnreadCount(int totalUnreadCount) {
        EventBus.getDefault().post(new ChatEvent.TotalUnreadCountEvent(totalUnreadCount));
    }

}

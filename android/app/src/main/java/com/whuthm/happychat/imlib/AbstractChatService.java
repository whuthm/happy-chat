package com.whuthm.happychat.imlib;

import android.arch.lifecycle.LifecycleObserver;

import com.whuthm.happychat.internal.lifecycle.SimpleLifecycle;

public class AbstractChatService<T>  extends SimpleLifecycle implements LifecycleObserver {

    private final ChatContext chatContext;

    AbstractChatService(ChatContext chatContext) {
        this.chatContext = chatContext;
    }

    protected ChatContext getChatContext() {
        return chatContext;
    }

}

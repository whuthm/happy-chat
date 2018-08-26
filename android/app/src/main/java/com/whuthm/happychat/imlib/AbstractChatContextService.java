package com.whuthm.happychat.imlib;

import android.arch.lifecycle.LifecycleObserver;

import com.whuthm.happychat.internal.SimpleLifecycle;

public class AbstractChatContextService<T>  extends SimpleLifecycle implements ChatContext.ServiceProvider<T>, LifecycleObserver {

    private final ChatContext chatContext;

    AbstractChatContextService(ChatContext chatContext) {
        this.chatContext = chatContext;
    }

    protected ChatContext getChatContext() {
        return chatContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T provideService(ChatContext chatContext) {
        return (T) this;
    }


}

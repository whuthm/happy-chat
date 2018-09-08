package com.whuthm.happychat.imlib;

import android.arch.lifecycle.LifecycleObserver;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.common.lifecycle.SimpleLifecycle;

public class AbstractChatService  extends SimpleLifecycle implements LifecycleObserver {

    private final ChatContext chatContext;

    AbstractChatService(ChatContext chatContext) {
        this.chatContext = chatContext;
    }

    protected ChatContext getChatContext() {
        return chatContext;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Logs.i("AbstractChatService", getClass().getSimpleName() + " create");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logs.i("AbstractChatService", getClass().getSimpleName() + " destroy");
    }

    protected String getCurrentUserId() {
        return getChatContext().getConfiguration().getUserId();
    }
}

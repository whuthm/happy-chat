package com.whuthm.happychat.imlib;

import android.content.Context;

import com.whuthm.happychat.internal.context.ServiceContext;

public interface ChatContext extends ServiceContext {

    Context getAndroidContext();

    ChatConfiguration getConfiguration();

    interface Initializer {
        void initialize(ChatContext chatContext);
    }


}

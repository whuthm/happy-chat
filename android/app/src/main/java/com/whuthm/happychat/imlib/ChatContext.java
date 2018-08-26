package com.whuthm.happychat.imlib;

import android.content.Context;

public interface ChatContext {

    Context getAndroidContext();

    ChatConfiguration getConfiguration();

    <T> T getService(Class<T> clazz);

    <T> void registerServiceProvider(Class<T> clazz, ChatContext.ServiceProvider<T> provider);

    interface ServiceProvider<T> {
        T provideService(ChatContext chatContext);
    }

    interface Initializer {
        void initialize(ChatContext chatContext);
    }


}

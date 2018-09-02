package com.whuthm.happychat.imlib;

import android.content.Context;
import android.support.annotation.MainThread;
import android.util.Log;

import com.whuthm.happychat.imlib.dao.greendao.GreenDaoOpenHelper;

/**
 * 获取ChatContext
 */
public class ChatManager {

    private final static String TAG = ChatManager.class.getSimpleName();
    private final Context androidContext;
    private static ChatManager sInstance;
    private ChatContextImpl chatContext;

    private static final ChatConfiguration NO_CONFIGURATION = new ChatConfiguration("", "");

    private final ChatContext.Initializer internalInitializer;
    private final ChatContext.Initializer initializer;

    private ChatManager(Context androidContext, ChatContext.Initializer initializer) {
        this.androidContext = androidContext;
        this.initializer = initializer;
        this.internalInitializer = new ChatContextInitializerImpl();
    }

    /**
     * only call one-time
     * @param androidContext: Application
     */
    @MainThread
    public static void init(Context androidContext, ChatContext.Initializer initializer) {
        if (sInstance == null) {
            sInstance = new ChatManager(androidContext.getApplicationContext(), initializer);
            sInstance.connect(NO_CONFIGURATION);
        }
    }

    public static ChatManager getInstance() {
        return sInstance;
    }

    public synchronized void connect(ChatConfiguration configuration) {
        performChangeContext(configuration);
    }

    public synchronized void disconnect() {
        performChangeContext(NO_CONFIGURATION);
    }

    private void performChangeContext(ChatConfiguration configuration) {
        final ChatContextImpl previousContext = this.chatContext;
        final ChatConfiguration previousConfiguration = previousContext != null ? previousContext.getConfiguration() : null;
        if (previousConfiguration == null || !previousConfiguration.equals(configuration)) {
            if (previousConfiguration != null && previousConfiguration.isValid()) {
                Log.i(TAG, "destroy context:" + previousContext);
                previousContext.destroy();
            }

            final ChatContextImpl currentChatContext = new ChatContextImpl(androidContext, configuration);
            currentChatContext.setDaoFactory(new GreenDaoOpenHelper(androidContext, configuration.getUserId()));
            internalInitializer.initialize(currentChatContext);
            if (initializer != null) {
                initializer.initialize(currentChatContext);
            }
            this.chatContext = currentChatContext;
            if (configuration.isValid()) {
                Log.i(TAG, "create context:" + currentChatContext);
                currentChatContext.create();
            }
        } else {
            Log.d(TAG, configuration + " duplicate");
        }
    }

    public synchronized ChatContext getChatContext() {
        return chatContext;
    }
}

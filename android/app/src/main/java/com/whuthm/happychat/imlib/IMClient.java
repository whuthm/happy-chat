package com.whuthm.happychat.imlib;

import android.content.Context;
import android.support.annotation.MainThread;
import android.util.Log;

import com.whuthm.happychat.imlib.dao.greendao.GreenDaoOpenHelper;

/**
 * 获取ChatContext
 */
public class IMClient {

    private final static String TAG = IMClient.class.getSimpleName();
    private static IMClient sInstance;
    private IMContextImpl chatContext;
    private final IMOptions options;

    private static final ConnectionConfiguration NO_CONFIGURATION = new ConnectionConfiguration("", "");

    private IMClient(IMOptions options) {
        this.options = options;
    }

    /**
     * only call one-time
     */
    @MainThread
    public static void init(IMOptions options) {
        if (sInstance == null) {
            sInstance = new IMClient(options);
            sInstance.connect(NO_CONFIGURATION);
        }
    }

    public static IMClient getInstance() {
        return sInstance;
    }

    public synchronized void connect(ConnectionConfiguration configuration) {
        performChangeContext(configuration);
    }

    public synchronized void disconnect() {
        performChangeContext(NO_CONFIGURATION);
    }

    private void performChangeContext(ConnectionConfiguration configuration) {
        final IMContextImpl previousContext = this.chatContext;
        final ConnectionConfiguration previousConfiguration = previousContext != null ? previousContext.getConfiguration() : null;
        if (previousConfiguration == null || !previousConfiguration.equals(configuration)) {
            if (previousConfiguration != null && previousConfiguration.isValid()) {
                Log.i(TAG, "destroy context:" + previousContext);
                previousContext.destroy();
            }

            final IMContextImpl currentChatContext = new IMContextImpl(options, configuration);
            IMContextInitializerImpl chatContextInitializer = new IMContextInitializerImpl(new GreenDaoOpenHelper(options.getAndroidContext(), configuration.getUserId()));
            chatContextInitializer.initialize(currentChatContext);
            final IMContext.Initializer initializer = options.getInitializer();
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

    public synchronized IMContext getIMContext() {
        return chatContext;
    }
}

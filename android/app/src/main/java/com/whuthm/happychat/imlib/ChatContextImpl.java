package com.whuthm.happychat.imlib;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.whuthm.happychat.imlib.dao.IMDaoFactory;
import com.whuthm.happychat.common.context.AbstractServiceContext;

final class ChatContextImpl extends AbstractServiceContext implements ChatContext, LifecycleOwner {
    private static final String TAG = ChatContextImpl.class.getSimpleName();

    private final ChatConfiguration configuration;

    private final LifecycleRegistry lifecycleRegistry;

    private final Context androidContext;

    private IMDaoFactory daoFactory;
    private EventPoster eventPoster;

    ChatContextImpl(Context androidContext, ChatConfiguration configuration) {
        this.androidContext  = androidContext;
        this.configuration = configuration;
        this.lifecycleRegistry = new LifecycleRegistry(this);
        this.eventPoster = new EventPoster();
    }

    @Override
    public Context getAndroidContext() {
        return androidContext;
    }

    @Override
    public ChatConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    protected <T> void onRegisterService(Class<T> clazz, T service) {
        super.onRegisterService(clazz, service);
        if (service instanceof LifecycleObserver) {
            lifecycleRegistry.addObserver((LifecycleObserver) service);
        }
    }

    void create() {
        if (lifecycleRegistry.getCurrentState() == Lifecycle.State.INITIALIZED) {
            Log.i(TAG, "context created");
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        }
    }

    void destroy() {
        if (lifecycleRegistry.getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
            Log.i(TAG, "context destroyed");
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        }
    }

    @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    IMDaoFactory getDaoFactory() {
        return daoFactory;
    }

    EventPoster getEventPoster() {
        return eventPoster;
    }

    void setDaoFactory(IMDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}

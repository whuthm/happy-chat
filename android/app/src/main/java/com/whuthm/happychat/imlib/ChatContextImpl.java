package com.whuthm.happychat.imlib;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.annotation.NonNull;

import com.whuthm.happychat.imlib.db.IOpenHelper;
import com.whuthm.happychat.internal.context.AbstractServiceContext;

final class ChatContextImpl extends AbstractServiceContext implements ChatContext, LifecycleOwner {

    private final ChatConfiguration configuration;

    private final LifecycleRegistry lifecycleRegistry;

    private final Context androidContext;

    private IOpenHelper openHelper;

    ChatContextImpl(Context androidContext, ChatConfiguration configuration) {
        this.androidContext  = androidContext;
        this.configuration = configuration;
        this.lifecycleRegistry = new LifecycleRegistry(this);
    }

    @Override
    public Context getAndroidContext() {
        return null;
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
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        }
    }

    void destroy() {
        if (lifecycleRegistry.getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        }
    }

    @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    IOpenHelper getOpenHelper() {
        return openHelper;
    }

    void setOpenHelper(IOpenHelper openHelper) {
        this.openHelper = openHelper;
    }
}

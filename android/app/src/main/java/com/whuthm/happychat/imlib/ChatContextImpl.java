package com.whuthm.happychat.imlib;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.annotation.NonNull;

import com.whuthm.happychat.imlib.db.IOpenHelper;

import java.util.HashMap;
import java.util.Map;

final class ChatContextImpl implements ChatContext, LifecycleOwner {

    private final ChatConfiguration configuration;

    private final LifecycleRegistry lifecycleRegistry;

    private final Map<Class<?>, Object> services;

    private final Context androidContext;

    private IOpenHelper openHelper;

    ChatContextImpl(Context androidContext, ChatConfiguration configuration) {
        this.androidContext  = androidContext;
        this.configuration = configuration;
        this.lifecycleRegistry = new LifecycleRegistry(this);
        this.services = new HashMap<>();
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
    public <T> T getService(Class<T> clazz) {
        return null;
    }

    @Override
    public <T> void registerServiceProvider(Class<T> clazz, ChatContext.ServiceProvider<T> provider) {
        T service = provider.provideService(this);
        if (service instanceof LifecycleObserver) {
            lifecycleRegistry.addObserver((LifecycleObserver) service);
        }
        services.put(clazz, service);
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

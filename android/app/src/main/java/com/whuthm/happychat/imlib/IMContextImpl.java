package com.whuthm.happychat.imlib;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.whuthm.happychat.common.context.AbstractServiceContext;

final class IMContextImpl extends AbstractServiceContext implements IMContext, LifecycleOwner {
    private static final String TAG = IMContextImpl.class.getSimpleName();

    private final ConnectionConfiguration configuration;

    private final LifecycleRegistry lifecycleRegistry;

    private IMOptions options;

    IMContextImpl(IMOptions options, ConnectionConfiguration configuration) {
        this.options  = options;
        this.configuration = configuration;
        this.lifecycleRegistry = new LifecycleRegistry(this);
    }

    @Override
    public Context getAndroidContext() {
        return options.getAndroidContext();
    }

    @Override
    public IMOptions getOptions() {
        return options;
    }

    @Override
    public ConnectionConfiguration getConfiguration() {
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

}

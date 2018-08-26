package com.whuthm.happychat.internal;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class SimpleLifecycle implements LifecycleObserver {

    private boolean created;
    private boolean destroyed;

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public final void create() {
        if (!isCreated() && !isDestroyed()) {
            setCreated(true);
            onCreate();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public final void destroy() {
        if(isCreated() && !isDestroyed()) {
            setDestroyed(true);
            onDestroy();
        }
    }

    protected void onCreate() {
        Log.i(getClass().getSimpleName(), "onCreate");
    }


    protected void onDestroy() {
        Log.i(getClass().getSimpleName(), "onDestroy");
    }

    protected final boolean isCreated() {
        return created;
    }

    private void setCreated(boolean created) {
        this.created = created;
    }

    protected final boolean isDestroyed() {
        return destroyed;
    }

    private void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}

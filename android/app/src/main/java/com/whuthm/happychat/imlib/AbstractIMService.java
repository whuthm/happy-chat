package com.whuthm.happychat.imlib;

import android.arch.lifecycle.LifecycleObserver;

import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.common.lifecycle.SimpleLifecycle;
import com.whuthm.happychat.imlib.event.EventBusUtils;

public class AbstractIMService extends SimpleLifecycle implements LifecycleObserver {

    private final IMContext imContext;

    protected AbstractIMService(IMContext imContext) {
        this.imContext = imContext;
    }

    protected IMContext getImContext() {
        return imContext;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Logs.i("AbstractChatService", getClass().getSimpleName() + " create");
        EventBusUtils.safeRegister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logs.i("AbstractChatService", getClass().getSimpleName() + " destroy");
        EventBusUtils.safeUnregister(this);
    }

    protected String getCurrentUserId() {
        return getImContext().getConfiguration().getUserId();
    }
}

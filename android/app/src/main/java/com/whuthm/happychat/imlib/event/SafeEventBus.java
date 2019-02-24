package com.whuthm.happychat.imlib.event;

/**
 * Created by huangming on 2018/11/12.
 */

public class SafeEventBus implements EventPoster {

    @Override
    public void register(Object subscriber) {
        EventBusUtils.safeRegister(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        EventBusUtils.safeUnregister(subscriber);
    }

    @Override
    public void post(Object event) {
        EventBusUtils.safePost(event);
    }
}

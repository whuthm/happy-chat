package com.whuthm.happychat.imlib.event;

/**
 * Created by huangming on 2018/11/19.
 */

public class EventPosterWrapper implements EventPoster {

    private final EventPoster wrapped;

    public EventPosterWrapper(EventPoster wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void register(Object subscriber) {
        wrapped.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        wrapped.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        wrapped.post(event);
    }
}

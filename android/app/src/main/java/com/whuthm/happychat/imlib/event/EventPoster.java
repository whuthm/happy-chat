package com.whuthm.happychat.imlib.event;

public interface EventPoster {

    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Object event);

}

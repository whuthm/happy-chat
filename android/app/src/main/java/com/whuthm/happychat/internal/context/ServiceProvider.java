package com.whuthm.happychat.internal.context;

public interface ServiceProvider<T, C extends ServiceContext> {

    T provideService(C serviceContext);
}

package com.whuthm.happychat.common.context;

public interface ServiceProvider<T, C extends ServiceContext> {

    T provideService(C serviceContext);
}

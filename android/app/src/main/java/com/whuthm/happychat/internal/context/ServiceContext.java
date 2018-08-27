package com.whuthm.happychat.internal.context;

public interface ServiceContext {

     <T> T getService(Class<T> clazz);

     <T> void registerService(Class<T> clazz, T service);

}

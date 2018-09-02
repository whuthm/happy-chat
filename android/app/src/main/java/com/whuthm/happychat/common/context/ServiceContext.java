package com.whuthm.happychat.common.context;

public interface ServiceContext {

     <T> T getService(Class<T> clazz);

     <T> void registerService(Class<T> clazz, T service);

}

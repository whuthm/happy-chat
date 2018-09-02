package com.whuthm.happychat.common.context;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractServiceContext implements ServiceContext {

    private final Map<Class<?>, Object> services;

    protected AbstractServiceContext() {
        this.services = new HashMap<>();
    }


    @SuppressWarnings("unchecked")
    @Override
    public final  <T> T getService(Class<T> clazz) {
        return (T) services.get(clazz);
    }

    public final  <T> void registerService(Class<T> clazz, T service) {
        onRegisterService(clazz, service);
        services.put(clazz, service);
    }

    protected <T> void onRegisterService(Class<T> clazz, T service) {
    }

}

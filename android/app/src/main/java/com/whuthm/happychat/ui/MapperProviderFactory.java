package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.IMContext;

import java.util.WeakHashMap;

public class MapperProviderFactory {

    private final static WeakHashMap<IMContext, MapperProvider> mapperProvidersByIMContext = new WeakHashMap<>();


    public static MapperProvider get(IMContext imContext) {
        MapperProvider mapperProvider = mapperProvidersByIMContext.get(imContext);
        if (mapperProvider == null) {
            mapperProvider = new MapperProvider(imContext);
            mapperProvidersByIMContext.put(imContext, mapperProvider);
        }
        return mapperProvider;
    }
}

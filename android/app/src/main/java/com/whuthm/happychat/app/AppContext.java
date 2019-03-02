package com.whuthm.happychat.app;

import android.app.Application;

import com.whuthm.happychat.app.dao.AppDaoFactory;
import com.whuthm.happychat.app.dao.impl.AppDaoFactoryImpl;
import com.whuthm.happychat.common.context.ApplicationServiceContext;

class AppContext extends ApplicationServiceContext {

    private final AppDaoFactory daoFactory;
    public AppContext(Application application) {
        super(application);
        daoFactory = new AppDaoFactoryImpl(application);
    }

    AppDaoFactory getDaoFactory() {
        return daoFactory;
    }
}

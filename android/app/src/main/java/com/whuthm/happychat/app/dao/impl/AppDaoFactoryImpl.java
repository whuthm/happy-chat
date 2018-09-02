package com.whuthm.happychat.app.dao.impl;

import android.content.Context;

import com.whuthm.happychat.app.dao.AppDaoFactory;
import com.whuthm.happychat.app.dao.IAuthenticationUserDao;

public class AppDaoFactoryImpl implements AppDaoFactory {

    private final Context context;
    private final IAuthenticationUserDao authenticationUserDao;

    public AppDaoFactoryImpl(Context context) {
        this.context = context;
        this.authenticationUserDao = new AuthenticationUserDaoImpl(context);
    }

    @Override
    public IAuthenticationUserDao getAuthenticationUserDao() {
        return authenticationUserDao;
    }
}

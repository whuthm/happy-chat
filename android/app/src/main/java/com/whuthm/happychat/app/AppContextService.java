package com.whuthm.happychat.app;


import com.whuthm.happychat.app.dao.AppDaoFactory;

class AppContextService  {

    private final AppContext appContext;

     AppContextService(AppContext appContext) {
         this.appContext = appContext;
     }

     protected AppContext getAppContext() {
         return appContext;
     }

     protected AppDaoFactory getAppDaoFactory() {
         return appContext.getDaoFactory();
     }
 }

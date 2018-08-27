package com.whuthm.happychat;

import android.app.Application;

import com.barran.lib.utils.DisplayUtil;
import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.data.DBOperator;
import com.whuthm.happychat.data.UserAccount;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.internal.context.ApplicationServiceContext;

/**
 * 程序入口
 *
 * Created by tanwei on 2018/7/20.
 */

public class HappyApp extends Application implements ApplicationServiceContext.Provider {

    private ApplicationServiceContext applicationContext;
    
    @Override
    public void onCreate() {
        initEnv();
        Logs.v("app onCreate");
        applicationContext = new ApplicationServiceContext(this);
        
        super.onCreate();
        
        initApp();
    }
    
    private void initEnv() {
        Logs.init(this);
        Logs.setDebug(true);
        DisplayUtil.init(this);
        UserAccount.init(this);
    }
    
    private void initApp() {
        RetrofitClient.initRetrofit(this);
        DBOperator.init(this);
    }

    @Override
    public ApplicationServiceContext provideApplicationContext() {
        return applicationContext;
    }
}

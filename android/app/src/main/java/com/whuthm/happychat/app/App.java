package com.whuthm.happychat.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.barran.lib.utils.DisplayUtil;
import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.data.DBOperator;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.imlib.ChatConfiguration;
import com.whuthm.happychat.imlib.ChatContext;
import com.whuthm.happychat.imlib.ChatManager;

/**
 * 程序入口
 *
 * Created by tanwei on 2018/7/20.
 */

public class App extends Application implements ApplicationServiceContext.Provider {

    private ApplicationServiceContext applicationContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        initEnv();
        Logs.v("app onCreate");
        super.onCreate();

        initApp();
    }

    private void initEnv() {
        Logs.init(this);
        Logs.setDebug(true);
        DisplayUtil.init(this);
    }

    private void connectChat() {
        final AuthenticationService authenticationService = applicationContext.getService(AuthenticationService.class);
        ChatManager.getInstance()
                .connect(new ChatConfiguration(authenticationService.getAuthenticationUser().getUserId(), authenticationService.getAuthenticationUser().getUserToken()));
    }

    private void disconnectChat() {
        ChatManager.getInstance().disconnect();
    }

    private void initApp() {
        applicationContext = new AppContext(this);

        ChatManager.init(this, new ChatContext.Initializer() {
            @Override
            public void initialize(ChatContext chatContext) {

            }
        });

        RetrofitClient.initRetrofit(this);
        DBOperator.init(this);

        final AuthenticationService authenticationService = applicationContext.getService(AuthenticationService.class);
        if (authenticationService.getAuthenticationStatus() == AuthenticationStatus.LoggedIn) {
            connectChat();
        }
        authenticationService.addAuthenticationStatusListener(new AuthenticationStatusListener() {
            @Override
            public void onAuthenticationStatusChanged(AuthenticationStatus status) {
                switch (status) {
                    case LoggedIn:
                        connectChat();
                        break;
                    case Logout:
                        disconnectChat();
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public ApplicationServiceContext provideApplicationContext() {
        return applicationContext;
    }
}

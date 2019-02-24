package com.whuthm.happychat.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.barran.lib.utils.DisplayUtil;
import com.barran.lib.utils.log.Logs;
import com.whuthm.happychat.BuildConfig;
import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.data.DBOperator;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.imlib.ConnectionConfiguration;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.IMClient;
import com.whuthm.happychat.imlib.IMOptions;

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
        IMClient.getInstance()
                .connect(new ConnectionConfiguration(authenticationService.getAuthenticationUser().getUserId(), authenticationService.getAuthenticationUser().getUserToken()));
    }

    private void disconnectChat() {
        IMClient.getInstance().disconnect();
    }

    private void initApp() {
        applicationContext = new AppContext(this);
        RetrofitClient.initRetrofit(this);

        DBOperator.init(this);

        IMOptions imOptions = new IMOptions.Builder(this)
                .setImServer(BuildConfig.IM_SERVER)
                .setImPort(BuildConfig.IM_PORT)
                .setInitializer(new IMContext.Initializer() {
                    @Override
                    public void initialize(IMContext chatContext) {
                        chatContext.registerService(ConversationAppService.class, new ConversationAppServiceImpl(chatContext));
                        chatContext.registerService(UserAppService.class, new UserAppServiceImpl(chatContext));
                        chatContext.registerService(MessageAppService.class, new MessageAppServiceImpl(chatContext));
                    }
                })
                .build();

        IMClient.init(imOptions);

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

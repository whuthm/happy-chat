package com.whuthm.happychat.app;

import android.text.TextUtils;

import com.whuthm.happychat.app.dao.IAuthenticationUserDao;
import com.whuthm.happychat.app.event.AuthenticationStatusChangedEvent;
import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.common.rx.Observers;
import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.data.api.ApiUtils;
import com.whuthm.happychat.data.api.RetrofitClient;
import com.whuthm.happychat.imlib.ConnectionConfiguration;
import com.whuthm.happychat.imlib.IMClient;
import com.whuthm.happychat.imlib.event.ConnectionEvent;
import com.whuthm.happychat.imlib.event.EventBusUtils;
import com.whuthm.happychat.imlib.model.ConnectionStatus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class AuthenticationManager extends AppContextService implements AuthenticationService {

    private AuthenticationUser authenticationUser;
    private AuthenticationStatus authenticationStatus = AuthenticationStatus.None;

    private AuthenticationManager(AppContext appContext) {
        super(appContext);
         this.authenticationUser = getDao().getAuthenticationUser();
        if (authenticationUser != null && !TextUtils.isEmpty(authenticationUser.getUserId()) && !TextUtils.isEmpty(authenticationUser.getUserToken())) {
            this.authenticationStatus = AuthenticationStatus.LoggedIn;
        }
    }

    public static void init(AppContext appContext) {
        AuthenticationManager authenticationManager = new AuthenticationManager(appContext);
        appContext.registerService(AuthenticationService.class, authenticationManager);
        EventBusUtils.safeRegister(authenticationManager);
        if (authenticationManager.getAuthenticationStatus() == AuthenticationStatus.LoggedIn) {
            authenticationManager.connectChat();
        }
    }

    private IAuthenticationUserDao getDao() {
        return getAppDaoFactory().getAuthenticationUserDao();
    }

    @Subscribe
    public void onConnectionStatusChangedEvent(ConnectionEvent.StatusChangedEvent event) {
        if (event.getConnectionStatus() == ConnectionStatus.UNAUTHORIZED) {
            logout().subscribe(Observers.<AuthenticationUser>empty());
        }
    }

    @Override
    public Observable<AuthenticationUser> login(final AuthenticationProtos.LoginRequest request) {
        return RetrofitClient.api()
                .login(request)
                .subscribeOn(Schedulers.io())
                .map(new Function<AuthenticationProtos.LoginResponse, AuthenticationUser>() {
                    @Override
                    public AuthenticationUser apply(AuthenticationProtos.LoginResponse loginResponse) throws Exception {
                        ApiUtils.requireProtoResponseSuccessful(loginResponse);
                        AuthenticationUser authenticationUser = new AuthenticationUser();
                        authenticationUser.setUserId(loginResponse.getUserId());
                        authenticationUser.setUserToken(loginResponse.getToken());
                        authenticationUser.setUsername(request.getUsername());
                        performChangeAuthenticationUserAndStatus(authenticationUser, AuthenticationStatus.LoggedIn);
                        return authenticationUser;
                    }
                });
    }

    @Override
    public Observable<AuthenticationUser> register(final AuthenticationProtos.RegisterRequest request) {
        return RetrofitClient.api()
                .register(request)
                .subscribeOn(Schedulers.io())
                .map(new Function<BaseProtos.BaseResponse, AuthenticationUser>() {
                    @Override
                    public AuthenticationUser apply(BaseProtos.BaseResponse baseResponse) throws Exception {
                        ApiUtils.requireProtoResponseSuccessful(baseResponse);
                        AuthenticationUser authenticationUser = new AuthenticationUser();
                        authenticationUser.setUsername(request.getUsername());
                        performChangeAuthenticationUserAndStatus(authenticationUser, AuthenticationStatus.Registered);
                        return authenticationUser;
                    }
                });
    }

    @Override
    public Observable<AuthenticationUser> logout() {
        return Observable
                .create(new ObservableOnSubscribe<AuthenticationUser>() {
                    @Override
                    public void subscribe(ObservableEmitter<AuthenticationUser> e) throws Exception {
                        AuthenticationUser authenticationUser = new AuthenticationUser();
                        authenticationUser.setUsername(getAuthenticationUser().getUsername());
                        performChangeAuthenticationUserAndStatus(authenticationUser, AuthenticationStatus.Logout);
                        e.onNext(authenticationUser);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public AuthenticationUser getAuthenticationUser() {
        return authenticationUser;
    }

    @Override
    public AuthenticationStatus getAuthenticationStatus() {
        return authenticationStatus;
    }

    private synchronized void performChangeAuthenticationUserAndStatus(AuthenticationUser authenticationUser, AuthenticationStatus authenticationStatus) {
        getDao().saveAuthenticationUser(authenticationUser);
        this.authenticationUser = authenticationUser;
        performChangeAuthenticationStatus(authenticationStatus);
    }

    private synchronized void performChangeAuthenticationStatus(AuthenticationStatus authenticationStatus) {
        if (this.authenticationStatus != authenticationStatus) {
            this.authenticationStatus = authenticationStatus;
            EventBusUtils.safePost(new AuthenticationStatusChangedEvent(authenticationStatus));
            switch (authenticationStatus) {
                case LoggedIn:
                    connectChat();
                    break;
                case Logout:
                    disconnectChat();
                    break;
                default:
                    break;
            }
        }
    }

    private void connectChat() {
        IMClient.getInstance()
                .connect(new ConnectionConfiguration(getAuthenticationUser().getUserId(), getAuthenticationUser().getUserToken()));
    }

    private void disconnectChat() {
        IMClient.getInstance().disconnect();
    }
}



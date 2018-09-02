package com.whuthm.happychat.app;

import android.text.TextUtils;

import com.whuthm.happychat.app.dao.IAuthenticationUserDao;
import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.data.api.ApiUtils;
import com.whuthm.happychat.data.api.RetrofitClient;

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
    private Set<AuthenticationStatusListener> authenticationStatusListeners;

    AuthenticationManager(AppContext appContext) {
        super(appContext);
        this.authenticationStatusListeners = new HashSet<>();
         this.authenticationUser = getDao().getAuthenticationUser();
        if (authenticationUser != null && !TextUtils.isEmpty(authenticationUser.getUserId()) && !TextUtils.isEmpty(authenticationUser.getUserToken())) {
            this.authenticationStatus = AuthenticationStatus.LoggedIn;
        }
    }

    private IAuthenticationUserDao getDao() {
        return getAppDaoFactory().getAuthenticationUserDao();
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
                })
                .observeOn(AndroidSchedulers.mainThread());
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
                })
                .observeOn(AndroidSchedulers.mainThread());
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public AuthenticationUser getAuthenticationUser() {
        return authenticationUser;
    }

    @Override
    public AuthenticationStatus getAuthenticationStatus() {
        return authenticationStatus;
    }

    @Override
    public synchronized void addAuthenticationStatusListener(AuthenticationStatusListener l) {
        this.authenticationStatusListeners.add(l);
    }

    @Override
    public synchronized void removeAuthenticationStatusListener(AuthenticationStatusListener l) {
        this.authenticationStatusListeners.remove(l);
    }

    private synchronized void performChangeAuthenticationUserAndStatus(AuthenticationUser authenticationUser, AuthenticationStatus authenticationStatus) {
        getDao().saveAuthenticationUser(authenticationUser);
        this.authenticationUser = authenticationUser;
        performChangeAuthenticationStatus(authenticationStatus);
    }

    private synchronized void performChangeAuthenticationStatus(AuthenticationStatus authenticationStatus) {
        if (this.authenticationStatus != authenticationStatus) {
            this.authenticationStatus = authenticationStatus;
            for (AuthenticationStatusListener l : this.authenticationStatusListeners) {
                l.onAuthenticationStatusChanged(authenticationStatus);
            }
        }
    }
}



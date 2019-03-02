package com.whuthm.happychat.data.api;

import android.util.Log;

import com.whuthm.happychat.data.AuthenticationProtos;
import com.whuthm.happychat.data.BaseProtos;
import com.whuthm.happychat.data.UserProtos;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

//TODO 是否可以使用动态代理
public class ApiServiceWrapper implements ApiService {

    private static final String TAG = ApiServiceWrapper.class.getSimpleName();

    private final ApiService wrapped;
    private final Scheduler subscribeOn;

    public ApiServiceWrapper(ApiService wrapped, Scheduler subscribeOn) {
        this.wrapped = wrapped;
        this.subscribeOn = subscribeOn;
    }

    @Override
    public Observable<AuthenticationProtos.LoginResponse> login(AuthenticationProtos.LoginRequest request) {
        return wrap(wrapped.login(request)).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "login", throwable);
            }
        });
    }

    @Override
    public Observable<BaseProtos.BaseResponse> getEmailValidationCode(BaseProtos.StringBean request) {
        return wrap(wrapped.getEmailValidationCode(request));
    }

    @Override
    public Observable<BaseProtos.BaseResponse> register(AuthenticationProtos.RegisterRequest request) {
        return wrap(wrapped.register(request));
    }

    @Override
    public Observable<UserProtos.UserResponse> getUserById(String userId) {
        return wrap(wrapped.getUserById(userId));
    }

    @Override
    public Observable<UserProtos.UsersResponse> getUsers() {
        return wrap(wrapped.getUsers());
    }

    @Override
    public Observable<BaseProtos.BaseResponse> createGroup() {
        return wrap(wrapped.createGroup());
    }

    @Override
    public Observable<BaseProtos.BaseResponse> quitGroup(String id) {
        return wrap(wrapped.quitGroup(id));
    }

    private <T> Observable<T> wrap(Observable<T> wrapped) {
        return wrapped.subscribeOn(subscribeOn);
    }
}

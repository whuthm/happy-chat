package com.whuthm.happychat.data.api;

import com.barran.lib.utils.log.Logs;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * api 接口返回基类，处理部分通用错误
 *
 * Created by tanwei on 2018/7/23.
 */

public abstract class ApiResponseObserver<T> implements Observer<T> {


    private final FailureHandler failureHandler;

    public ApiResponseObserver(FailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    public ApiResponseObserver() {
        this(null);
    }

    protected Disposable mDisposable;

    @Override
    public final void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public final void onNext(T value) {
        onSuccess(value);
    }

    @Override
    public final void onError(Throwable e) {
        Logs.w("ApiResponseObserver", e.getClass().getName() + ":" + e.getMessage() + ", " + Thread.currentThread());

        if (!onFailure(e) && failureHandler != null) {
            failureHandler.handle(e);
        }

    }

    @Override
    public final void onComplete() {

    }

    public interface FailureHandler {

        void handle(Throwable error);
    }


    public abstract void onSuccess(T value);

    public abstract boolean onFailure(Throwable error);


}

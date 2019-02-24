package com.whuthm.happychat.common.rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public final class Observers {

    public static <T> Observer<T> empty() {
        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(T t) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public static <T> Observer<T> emptyDisposable() {
        return new DisposableObserver<T>() {
            @Override
            public void onNext(T t) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public static <T> Observer<T> wrapDisposable(final DisposableRegistry disposables, final DisposableObserver<T> wrapped) {
        return new DisposableObserver<T>() {

            @Override
            public void onNext(T value) {
                wrapped.onNext(value);
            }

            @Override
            public void onError(Throwable e) {
                wrapped.onError(e);
                disposables.unregister(this);
            }

            @Override
            public void onComplete() {
                wrapped.onComplete();
                disposables.unregister(this);
            }
        };
    }

}

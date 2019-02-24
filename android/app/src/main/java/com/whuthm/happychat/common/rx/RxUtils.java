package com.whuthm.happychat.common.rx;

import io.reactivex.ObservableEmitter;

public class RxUtils {

    private RxUtils() {

    }

    public static <T> void safeEmitNext(ObservableEmitter<T> emitter, T t) {
        if (emitter != null && !emitter.isDisposed() && t != null) {
            emitter.onNext(t);
        }
    }

    public static <T> void safeEmitComplete(ObservableEmitter<T> emitter) {
        if (emitter != null && !emitter.isDisposed()) {
            emitter.onComplete();
        }
    }

    public static <T> void safeEmitError(ObservableEmitter<T> emitter, Throwable tr) {
        if (emitter != null && !emitter.isDisposed()) {
            emitter.onError(tr);
        }
    }

    public static <T> void safeEmitNextAndComplete(ObservableEmitter<T> emitter, T t) {
        safeEmitNext(emitter, t);
        safeEmitComplete(emitter);
    }

}

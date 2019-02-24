package com.whuthm.happychat.common.rx;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class DisposableRegistry {

    private final Set<Disposable> disposables;

    public DisposableRegistry() {
        disposables = Collections.synchronizedSet(new HashSet<Disposable>());
    }

    public void unregisterAll() {
        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        disposables.clear();
    }

    public void register(Disposable disposable) {
        disposables.add(disposable);
    }

    public void unregister(Disposable disposable) {
        disposables.remove(disposable);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public interface Owner {
        DisposableRegistry getDisposables();
    }

    public static DisposableRegistry of(Object obj) {
        if (obj instanceof Owner) {
            return ((Owner) obj).getDisposables();
        }
        return null;
    }

}

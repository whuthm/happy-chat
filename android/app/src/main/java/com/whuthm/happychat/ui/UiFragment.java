package com.whuthm.happychat.ui;


import android.view.View;

import com.barran.lib.app.BaseFragment;
import com.whuthm.happychat.common.rx.DisposableRegistry;

import io.reactivex.disposables.Disposable;

public class UiFragment extends BaseFragment implements DisposableRegistry.Owner {

    private DisposableRegistry disposables = new DisposableRegistry();

    public <T extends Disposable> T addDisposable(T disposable) {
        disposables.register(disposable);
        return disposable;
    }

    public <T extends View> T findViewById(int viewId) {
        if (getView() != null) {
            return getView().findViewById(viewId);
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.unregisterAll();
    }

    @Override
    public DisposableRegistry getDisposables() {
        return disposables;
    }
}

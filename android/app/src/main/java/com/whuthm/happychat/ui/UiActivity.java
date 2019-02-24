package com.whuthm.happychat.ui;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.common.rx.DisposableRegistry;

public class UiActivity extends BaseActivity implements DisposableRegistry.Owner  {

    private DisposableRegistry disposables = new DisposableRegistry();



    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.unregisterAll();
    }

    @Override
    public DisposableRegistry getDisposables() {
        return disposables;
    }
}

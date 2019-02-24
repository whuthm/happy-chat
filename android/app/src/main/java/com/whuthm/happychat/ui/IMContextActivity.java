package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.imlib.IMContext;
import com.whuthm.happychat.imlib.IMClient;
import com.whuthm.happychat.imlib.event.EventBusUtils;

public abstract class IMContextActivity extends BaseActivity {

    protected IMContext imContext;
    protected ApplicationServiceContext applicationServiceContext;

    @Override
    @CallSuper
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationServiceContext = ApplicationServiceContext.of(this);
        imContext = IMClient.getInstance().getIMContext();
        EventBusUtils.safeRegister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.safeUnregister(this);
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        if (applicationServiceContext.getService(AuthenticationService.class).getAuthenticationStatus() != AuthenticationStatus.LoggedIn) {
            finish();
        }
    }
}

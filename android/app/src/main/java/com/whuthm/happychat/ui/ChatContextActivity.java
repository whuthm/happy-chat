package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.app.model.AuthenticationStatus;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.imlib.ChatContext;
import com.whuthm.happychat.imlib.ChatManager;

public abstract class ChatContextActivity extends BaseActivity {

    protected ChatContext chatContext;
    protected ApplicationServiceContext applicationServiceContext;

    @Override
    @CallSuper
    protected  void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationServiceContext = ApplicationServiceContext.of(this);
        chatContext = ChatManager.getInstance().getChatContext();
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

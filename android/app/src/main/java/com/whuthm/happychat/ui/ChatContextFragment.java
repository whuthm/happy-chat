package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.barran.lib.app.BaseFragment;
import com.whuthm.happychat.common.context.ApplicationServiceContext;
import com.whuthm.happychat.imlib.ChatContext;
import com.whuthm.happychat.imlib.ChatManager;

public class ChatContextFragment extends BaseFragment {

    protected ChatContext chatContext;
    protected ApplicationServiceContext applicationServiceContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationServiceContext = ApplicationServiceContext.of(getContext());
        chatContext = ChatManager.getInstance().getChatContext();
    }
}

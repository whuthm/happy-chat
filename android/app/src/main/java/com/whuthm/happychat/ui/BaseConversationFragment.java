package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whuthm.happychat.app.AuthenticationService;
import com.whuthm.happychat.imlib.model.ConversationType;

public class BaseConversationFragment extends ChatContextFragment {

    public static final String KEY_CONVERSATION_ID = BaseConversationActivity.KEY_CONVERSATION_ID;
    public static final String KEY_CONVERSATION_TYPE = BaseConversationActivity.KEY_CONVERSATION_TYPE;

    protected String conversationId;
    protected ConversationType conversationType;

    protected String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationId = getArguments().getString(KEY_CONVERSATION_ID);
        conversationType = (ConversationType) getArguments().getSerializable(KEY_CONVERSATION_TYPE);

        userId = applicationServiceContext.getService(AuthenticationService.class)
                .getAuthenticationUser().getUserId();
    }

    public String getConversationId() {
        return conversationId;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }
}

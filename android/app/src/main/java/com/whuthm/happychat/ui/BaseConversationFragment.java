package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class BaseConversationFragment extends ChatContextFragment {

    public static final String KEY_CONVERSATION_ID = BaseConversationActivity.KEY_CONVERSATION_ID;
    public static final String KEY_CONVERSATION_TYPE = BaseConversationActivity.KEY_CONVERSATION_TYPE;

    protected String conversationId;
    protected String conversationType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationId = getArguments().getString(KEY_CONVERSATION_ID);
        conversationType = getArguments().getString(KEY_CONVERSATION_TYPE);
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getConversationType() {
        return conversationType;
    }
}

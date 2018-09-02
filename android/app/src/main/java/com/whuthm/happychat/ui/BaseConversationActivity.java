package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public abstract class BaseConversationActivity extends ChatContextActivity  {

    public static final String KEY_CONVERSATION_ID = "conversation_id";
    public static final String KEY_CONVERSATION_TYPE = "conversation_type";

    protected String conversationId;
    protected String conversationType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInitializeViews();
        Intent intent = getIntent();
        if (intent != null) {
            initializeArguments(intent);
        } else {
            finish();
        }
    }

    protected final void initializeArguments(@NonNull Intent intent) {
        conversationId = intent.getStringExtra(KEY_CONVERSATION_ID);
        conversationType = intent.getStringExtra(KEY_CONVERSATION_TYPE);
        if (checkArguments()) {
            onArgumentsInitialized();
        } else  {
            finish();
        }
    }

    protected void onArgumentsInitialized() {
    }

    protected void onInitializeViews() {

    }

    private boolean checkArguments() {
        return  !TextUtils.isEmpty(conversationId) && !TextUtils.isEmpty(conversationType);
    }

}

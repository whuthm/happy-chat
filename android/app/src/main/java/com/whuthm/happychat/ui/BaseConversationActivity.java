package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.whuthm.happychat.imlib.model.ConversationType;

public abstract class BaseConversationActivity extends IMContextActivity {

    public static final String KEY_CONVERSATION_ID = "conversation_id";
    public static final String KEY_CONVERSATION_TYPE = "conversation_type";
    public static final String KEY_CONVERSATION_TITLE = "conversation_title";

    protected String conversationId;
    protected ConversationType conversationType;

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
        onInitializeArguments(intent);
        if (checkArguments()) {
            onArgumentsInitialized();
        } else  {
            finish();
        }
    }

    protected void onArgumentsInitialized() {
    }

    protected void onInitializeArguments(@NonNull Intent intent) {
        conversationId = intent.getStringExtra(KEY_CONVERSATION_ID);
        conversationType = (ConversationType) intent.getSerializableExtra(KEY_CONVERSATION_TYPE);
    }

    protected void onInitializeViews() {

    }

    private boolean checkArguments() {
        return  !TextUtils.isEmpty(conversationId) && conversationType != null;
    }

    public String getConversationId() {
        return conversationId;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }
}

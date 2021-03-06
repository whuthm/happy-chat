package com.whuthm.happychat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.whuthm.happychat.R;
import com.whuthm.happychat.imlib.model.ConversationType;

/**
 * 聊天界面
 *
 * Created by huangming on 18/07/2018.
 */

public class ConversationActivity extends BaseConversationActivity {
    
    private ConversationFragment conversationFragment;
    
    @Override
    protected void onInitializeViews() {
        super.onInitializeViews();
        setContentView(R.layout.activity_full_frag);
    }

    @Override
    protected void onInitializeArguments(@NonNull Intent intent) {
        super.onInitializeArguments(intent);
        String title = intent.getStringExtra(KEY_CONVERSATION_TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }

    @Override
    protected void onArgumentsInitialized() {
        super.onArgumentsInitialized();
        conversationFragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CONVERSATION_ID, getConversationId());
        args.putSerializable(KEY_CONVERSATION_TYPE, getConversationType());
        conversationFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_full_frag_container, conversationFragment)
                .commitAllowingStateLoss();
    }

    public static void startConversation(Context context, String conversationId, ConversationType conversationType) {
        startConversation(context, conversationId, conversationType, "");
    }

    public static void startConversation(Context context, String conversationId, ConversationType conversationType, String conversationTitle) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(KEY_CONVERSATION_ID, conversationId);
        intent.putExtra(KEY_CONVERSATION_TYPE, conversationType);
        intent.putExtra(KEY_CONVERSATION_TITLE, conversationTitle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String conversationId = getIntent().getStringExtra(KEY_CONVERSATION_ID);
        if (!this.conversationId.equals(conversationId)) {
            if (conversationFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(conversationFragment)
                        .commitAllowingStateLoss();
            }
            initializeArguments(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (conversationFragment == null || !conversationFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}

package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.barran.lib.app.BaseActivity;
import com.whuthm.happychat.R;
import com.whuthm.happychat.data.Constants;

/**
 * 聊天界面
 *
 * Created by huangming on 18/07/2018.
 */

public class ChatActivity extends BaseActivity {
    
    private String conversationId;
    
    private ChatFragment messageFrag;
    
    private ChatSettingFragment settingFragment;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_frag);
        
        conversationId = getIntent().getStringExtra(Constants.KEY_CONVErSATION_ID);
        if (TextUtils.isEmpty(conversationId)) {
            finish();
            return;
        }
        
        mFragmentStack.setGroupID(R.id.activity_full_frag_container);
        pushMessageFrag();
    }
    
    private void pushMessageFrag() {
        messageFrag = new ChatFragment();
        messageFrag.setFragAction(new ChatFragment.IFragAction() {
            @Override
            public void setting() {
                pushSettingFrag();
            }
        });
        messageFrag.setArguments(getIntent().getExtras());
        mFragmentStack.setBottom(messageFrag);
    }
    
    private void pushSettingFrag() {
        if (settingFragment == null) {
            settingFragment = new ChatSettingFragment();
            messageFrag.setArguments(getIntent().getExtras());
        }
        mFragmentStack.push(settingFragment);
    }
}

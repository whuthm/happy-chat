package com.whuthm.happychat.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.barran.lib.app.BaseActivity;
import com.barran.lib.app.FragmentStack;
import com.whuthm.happychat.R;

public class MainActivity extends BaseActivity {
    
    private static final int TAB_CONVERSATION = 0;
    private static final int TAB_ME = 1;
    
    private TabLayout mTabLayout;
    
    private MainConversationFragment conversationFragment;
    
    private MainMeFragment meFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mFragmentStack.setFragOPMode(FragmentStack.FragOPMode.MODE_SWITCH);
        mFragmentStack.setGroupID(R.id.activity_main_container);
        mTabLayout = findViewById(R.id.activity_main_tab_layout);
        
        init();
    }
    
    private void init() {
        
        conversationFragment = new MainConversationFragment();
        meFragment = new MainMeFragment();
        
        mTabLayout.addTab(
                mTabLayout.newTab().setText(R.string.text_main_conversation_list),
                TAB_CONVERSATION, true);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.text_main_me), TAB_ME);
        
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_CONVERSATION:
                        mFragmentStack.switchTo(conversationFragment);
                        break;
                    case TAB_ME:
                        mFragmentStack.switchTo(meFragment);
                        break;
                }
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                
            }
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                
            }
        });

        mFragmentStack.switchTo(conversationFragment);
    }
    
}

package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.barran.lib.app.FragmentStack;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.ConversationAppService;
import com.whuthm.happychat.imlib.ConnectionService;
import com.whuthm.happychat.imlib.event.ConnectionEvent;
import com.whuthm.happychat.imlib.model.ConnectionStatus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends IMContextActivity {

    private static final int TAB_CHAT = 0;
    private static final int TAB_CONTACTS = 1;
    private static final int TAB_GROUPS = 2;
    private static final int TAB_ME = 3;
    private static final int TAB_TEST = 4;

    private TabLayout mTabLayout;

    private MainChatFragment conversationFragment;

    private MainMeFragment meFragment;

    private MainContactsFragment contactsFragment;

    private MainGroupsFragment groupsFragment;

    private MainTestFragment testFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentStack.setFragOPMode(FragmentStack.FragOPMode.MODE_SWITCH);
        mFragmentStack.setGroupID(R.id.activity_main_container);
        mTabLayout = findViewById(R.id.activity_main_tab_layout);

        init();
        updateConnectionViews(imContext.getService(ConnectionService.class).getConnectionStatus());

        imContext.getService(ConversationAppService.class).syncAllConversations();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectionStatusChangedEvent(ConnectionEvent.StatusChangedEvent event) {
        updateConnectionViews(event.getConnectionStatus());
    }

    private void updateConnectionViews(ConnectionStatus connectionStatus) {
        if (connectionStatus == ConnectionStatus.CONNECTED) {
            setTitle(R.string.app_name);
        } else {
            if (connectionStatus == ConnectionStatus.CONNECTING) {
                setTitle(getString(R.string.app_name_with_connection_status, "连接中..."));
            } else {
                setTitle(getString(R.string.app_name_with_connection_status, "未连接"));
            }
            if (connectionStatus == ConnectionStatus.UNAUTHORIZED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ConnectionService connectionService = imContext.getService(ConnectionService.class);
        if (connectionService.getConnectionStatus() != ConnectionStatus.CONNECTED) {
            connectionService.connect();
        }
    }

    private void init() {

        conversationFragment = new MainChatFragment();
        meFragment = new MainMeFragment();
        contactsFragment = new MainContactsFragment();
        groupsFragment = new MainGroupsFragment();
        testFragment = new MainTestFragment();

        mTabLayout.addTab(
                mTabLayout.newTab().setText(R.string.text_main_chat),
                TAB_CHAT, true);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.text_main_contacts), TAB_CONTACTS);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.text_main_groups), TAB_GROUPS);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.text_main_me), TAB_ME);
        //mTabLayout.addTab(mTabLayout.newTab().setText(R.string.text_main_test), TAB_TEST);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case TAB_CHAT:
                        mFragmentStack.switchTo(conversationFragment);
                        break;
                    case TAB_ME:
                        mFragmentStack.switchTo(meFragment);
                        break;
                    case TAB_CONTACTS:
                        mFragmentStack.switchTo(contactsFragment);
                        break;
                    case TAB_GROUPS:
                        mFragmentStack.switchTo(groupsFragment);
                        break;
                    case TAB_TEST:
                        mFragmentStack.switchTo(testFragment);
                        break;
                    default:
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

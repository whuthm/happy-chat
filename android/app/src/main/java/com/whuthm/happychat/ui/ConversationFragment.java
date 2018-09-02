package com.whuthm.happychat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.whuthm.happychat.R;

/**
 * 聊天界面: 可以拆分为ConversationTitleFragment(如果使用Toolbar， 可不定义), MessageListFragment, MessageInputFragment
 *
 * Created by huangming on 18/07/2018.
 */

public class ConversationFragment extends BaseConversationFragment {

    private MessageListFragment messageListFragment;
    private MessageInputFragment messageInputFragment;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_conversation_settings, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            messageListFragment = new MessageListFragment();
            messageListFragment.setArguments(args);
            messageInputFragment = new MessageInputFragment();
            messageInputFragment.setArguments(args);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_layout_message_list, messageListFragment)
                    .add(R.id.fragment_layout_message_input, messageInputFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            final Bundle args = getArguments();
            if (args != null) {
                startActivity(new Intent(getContext(), ConversationSettingsActivity.class).putExtras(getArguments()));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_conversation, container, false);
    }

    @Override
    public boolean onBackPressed() {
        if (messageInputFragment != null && messageInputFragment.onBackPressed()) {
            return false;
        }
        if (messageListFragment != null && messageListFragment.onBackPressed()) {
            return false;
        }
        return super.onBackPressed();
    }
}

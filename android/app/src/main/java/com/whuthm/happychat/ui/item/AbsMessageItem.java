package com.whuthm.happychat.ui.item;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.barran.lib.utils.DateUtil;
import com.barran.lib.utils.DisplayUtil;
import com.whuthm.happychat.BuildConfig;
import com.whuthm.happychat.R;
import com.whuthm.happychat.app.model.AuthenticationUser;
import com.whuthm.happychat.imlib.model.Message;

import java.util.Random;

/**
 * 通用消息显示组件
 * 
 * Created by tanwei on 2018/7/23.
 */

public abstract class AbsMessageItem extends ConstraintLayout {
    
    private ImageView mAvatar, mAvatarSelf;
    
    private TextView mTvNick, mTvTime;
    
    protected ViewStub contentStub, contentStubSelf;

    protected View contentView, contentViewSelf;
    
    protected boolean isSendBySelf;
    
    public AbsMessageItem(Context context) {
        super(context);
        installView();
    }
    
    public AbsMessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        installView();
    }
    
    private void installView() {

        // TODO test
        if (BuildConfig.DEBUG) {
            isSendBySelf = new Random().nextBoolean();
        }

        LayoutInflater.from(getContext()).inflate(R.layout.layout_base_message_item,
                this);

        mAvatar = findViewById(R.id.layout_message_user_avatar);
        mTvNick = findViewById(R.id.layout_message_user_nick);
        mTvTime = findViewById(R.id.layout_message_time);
        contentStub = findViewById(R.id.layout_message_content);

        mAvatarSelf = findViewById(R.id.layout_message_avatar_self);
        contentStubSelf = findViewById(R.id.layout_message_content_self);
        
        int padding = DisplayUtil.dp2px(12);
        setPadding(padding, padding / 2, padding, padding / 2);
        

        inflateContent();
    }
    
    protected abstract void inflateContent();
    
    public void showMessage(Message message) {

//        if (!BuildConfig.DEBUG) {
//            isSendBySelf = !TextUtils.isEmpty(AuthenticationUser.getUserId())
//                    && AuthenticationUser.getUserId().equals(message.getFrom());
//        }
        
        if (isSendBySelf) {
            
            mAvatar.setVisibility(GONE);
            mTvNick.setVisibility(GONE);
            mTvTime.setVisibility(GONE);

            mAvatarSelf.setVisibility(VISIBLE);
        }
        else {
            mAvatar.setVisibility(VISIBLE);
            mTvNick.setVisibility(VISIBLE);
            mTvTime.setVisibility(VISIBLE);

            mAvatarSelf.setVisibility(GONE);

            mTvNick.setText(message.getFrom());
            mTvTime.setText(DateUtil.formatDate(message.getSendTime()));
        }
    }
}

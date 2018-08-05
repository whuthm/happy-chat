package com.whuthm.happychat.ui.item;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.TextView;

import com.whuthm.happychat.R;
import com.whuthm.happychat.imlib.model.Message;

/**
 * 文本消息item
 *
 * Created by tanwei on 2018/7/23.
 */

public class TextMessageItem extends AbsMessageItem {
    
    private TextView tvContent, tvContentSelf;
    
    public TextMessageItem(Context context) {
        super(context);
    }
    
    public TextMessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void inflateContent() {
        
        contentStub.setLayoutResource(R.layout.layout_message_content_text);
        contentView = contentStub.inflate();
        tvContent = contentView.findViewById(R.id.layout_message_content_text);
        
        contentStubSelf.setLayoutResource(R.layout.layout_message_content_text);
        contentViewSelf = contentStubSelf.inflate();
        tvContentSelf = contentViewSelf.findViewById(R.id.layout_message_content_text);
        
    }
    
    public void setText(@StringRes int resId) {
        setText(getResources().getString(resId));
    }
    
    public void setText(String text) {
        if (isSendBySelf) {
            contentView.setVisibility(GONE);
            contentViewSelf.setVisibility(VISIBLE);
            
            tvContentSelf.setText(text);
        }
        else {
            contentView.setVisibility(VISIBLE);
            contentViewSelf.setVisibility(GONE);
            
            tvContent.setText(text);
        }
    }
    
    @Override
    public void showMessage(Message message) {
        super.showMessage(message);
        
        setText(message.getBody());
    }
}

package com.whuthm.happychat.ui.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.whuthm.happychat.GlideApp;
import com.whuthm.happychat.R;
import com.whuthm.happychat.imlib.model.Message;

/**
 * 图片消息item
 * 
 * Created by tanwei on 2018/7/23.
 */

public class ImageMessageItem extends AbsMessageItem {
    
    private ImageView imageView, imageViewSelf;
    
    public ImageMessageItem(Context context) {
        super(context);
    }
    
    public ImageMessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void inflateContent() {
        contentStub.setLayoutResource(R.layout.layout_message_content_image);
        contentView = contentStub.inflate();
        imageView = contentView.findViewById(R.id.layout_message_content_image);
        
        contentStubSelf.setLayoutResource(R.layout.layout_message_content_image);
        contentViewSelf = contentStubSelf.inflate();
        imageViewSelf = contentViewSelf.findViewById(R.id.layout_message_content_image);
    }
    
    public void setImageUrl(String url) {
        if (isSendBySelf) {
            contentView.setVisibility(GONE);
            contentViewSelf.setVisibility(VISIBLE);
            GlideApp.with(getContext()).load(url).centerCrop()
                    .placeholder(R.drawable.ic_launcher).into(imageViewSelf);
        }
        else {
            contentView.setVisibility(VISIBLE);
            contentViewSelf.setVisibility(GONE);
            GlideApp.with(getContext()).load(url).centerCrop()
                    .placeholder(R.drawable.ic_launcher).into(imageView);
        }
    }
    
    @Override
    public void showMessage(Message message, boolean isSendBySelf) {
        super.showMessage(message, isSendBySelf);
        
        setImageUrl(message.getBody());
    }
}

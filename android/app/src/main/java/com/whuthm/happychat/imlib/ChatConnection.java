package com.whuthm.happychat.imlib;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.imlib.model.Message;

/**
 * Created by huangming on 18/07/2018.
 *
 * WebSocket
 */

public class ChatConnection extends AbstractConnection {
    
    private IMessageListener mListener;
    
    public void setMessageListener(IMessageListener l) {
        mListener = l;
    }
    
    @Override
    protected void onMessageReceived(MessageProtos.MessageBean messageBean) {
        super.onMessageReceived(messageBean);

        Message message = new Message();
        message.setId(messageBean.getId());
        message.setSid(messageBean.getSid());
        message.setType(messageBean.getType());
        message.setBody(messageBean.getBody());
        message.setFrom(messageBean.getFrom());
        message.setTo(messageBean.getTo());
        message.setAttrs(messageBean.getAttributes());
        message.setConversationType(messageBean.getConversationType());
        message.setReceiveTime(System.currentTimeMillis());
        message.setSendTime(messageBean.getSendTime());
        message.setExtra(messageBean.getExtra());
        
        if (mListener != null) {
            mListener.onMessageReceived(message);
        }
    }
    
    @Override
    protected void performDisconnected(Throwable t) {
        super.performDisconnected(t);
        
        if (mListener != null) {
            mListener.onFinish(t);
        }
    }
}

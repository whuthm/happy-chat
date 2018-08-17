package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Message;

/**
 * 定义
 *
 * Created by tanwei on 2018/8/16.
 */

public interface IMessageListener {
    void onMessageReceived(Message message);
    
    void onFinish(Throwable error);
}

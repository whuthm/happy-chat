package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Message;

interface MessageReceiver {

    void onMessageReceive(Message message);

}

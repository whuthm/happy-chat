package com.whuthm.happychat.imlib;

import com.whuthm.happychat.imlib.model.Message;

interface MessageReceiver {

    boolean onReceive(Message message);

}

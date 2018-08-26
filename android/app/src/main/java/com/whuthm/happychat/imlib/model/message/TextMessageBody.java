package com.whuthm.happychat.imlib.model.message;

import com.whuthm.happychat.imlib.model.MessageBody;

public class TextMessageBody extends MessageBody {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package com.whuthm.happychat.imlib.model.message;

import com.whuthm.happychat.imlib.model.MessageTag;
import com.whuthm.happychat.imlib.model.MessageBody;

@MessageTag(type = "txt", flag = MessageTag.FLAG_COUNTED)
public class TextMessageBody extends MessageBody {

    private static final long serialVersionUID = -6378351195648281166L;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

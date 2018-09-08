package com.whuthm.happychat.imlib.model.message;

import com.whuthm.happychat.imlib.model.MessageTag;
import com.whuthm.happychat.imlib.model.MessageBody;

@MessageTag(type = MessageTag.TYPE_IMG, flag = MessageTag.FLAG_COUNTED)
public class ImageMessageBody extends MessageBody {

    private static final long serialVersionUID = 8872463275375934585L;
    private String url;
    private String localUrl;
    private int width;
    private int height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

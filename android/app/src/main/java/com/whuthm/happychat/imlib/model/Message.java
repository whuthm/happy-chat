package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 消息实体
 * 
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Message {
    
    @Id
    private long messageId;
    
    private String type;
    
    private String body;
    
    private String fromUserId;
    
    private String toUserId;
    
    private long time;
    
    private String attrs;
    
    private boolean read;
    
    private int status;
    
    public Message() {
        
    }
    
    @Generated(hash = 1186870233)
    public Message(long messageId, String type, String body, String fromUserId,
            String toUserId, long time, String attrs, boolean read, int status) {
        this.messageId = messageId;
        this.type = type;
        this.body = body;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.time = time;
        this.attrs = attrs;
        this.read = read;
        this.status = status;
    }

    public long getMessageId() {
        return this.messageId;
    }
    
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getBody() {
        return this.body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void setTime(long time) {
        this.time = time;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getFromUserId() {
        return this.fromUserId;
    }
    
    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
    
    public String getToUserId() {
        return this.toUserId;
    }
    
    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
    
    public boolean getRead() {
        return this.read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
    
    public String getAttrs() {
        return this.attrs;
    }
    
    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }
}

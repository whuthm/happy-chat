package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 会话实体
 * 
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Conversation implements Serializable {

    @Id
    private String id;
    
    private String type;
    
    private long latestMessageId;

    @ToOne(joinProperty = "latestMessageId")
    private Message latestMessage;
    
    private long latestMessageTime;

    public Conversation() {
        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Conversation) {
            return id.equals(((Conversation) obj).id);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{id});
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public long getLatestMessageId() {
        return latestMessageId;
    }

    public void setLatestMessageId(long latestMessageId) {
        this.latestMessageId = latestMessageId;
    }

    public Message getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(Message latestMessage) {
        this.latestMessage = latestMessage;
    }

    public long getLatestMessageTime() {
        return latestMessageTime;
    }

    public void setLatestMessageTime(long latestMessageTime) {
        this.latestMessageTime = latestMessageTime;
    }
}

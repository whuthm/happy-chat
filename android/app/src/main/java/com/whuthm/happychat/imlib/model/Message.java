package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Arrays;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 消息实体
 *
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Message implements Serializable {

    private static final long serialVersionUID = 8769663248677565606L;
    /**
     * 客戶端数据库表ID
     */
    @Id(autoincrement = true)
    private Long id;

    /**
     * 客戶端生成uuid传递给服务端，用于校验服务器已送达
     */
    @Unique
    private String uid;

    /**
     * 服务端生成传递给客户端，客户端可以根据此字段判断消息已读（PC和phone共存时）
     */
    private long sid;

    private String type;

    private String conversationType;

    private String from;

    /**
     * 建索引，快速查询某一会话的历史消息
     */
    @Index
    private String to;


    private String body;

    private long sendTime;
    private long receiveTime;

    private String attrs;

    private String extra;

    private boolean read;

    private int status;

    @Generated(hash = 1751850340)
    public Message(Long id, String uid, long sid, String type,
            String conversationType, String from, String to, String body,
            long sendTime, long receiveTime, String attrs, String extra,
            boolean read, int status) {
        this.id = id;
        this.uid = uid;
        this.sid = sid;
        this.type = type;
        this.conversationType = conversationType;
        this.from = from;
        this.to = to;
        this.body = body;
        this.sendTime = sendTime;
        this.receiveTime = receiveTime;
        this.attrs = attrs;
        this.extra = extra;
        this.read = read;
        this.status = status;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            return id ==((Message) obj).id;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Message(" + id + ")";
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new long[]{id});
    }


    public long getSid() {
        return this.sid;
    }


    public void setSid(long sid) {
        this.sid = sid;
    }


    public String getConversationType() {
        return this.conversationType;
    }


    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }


    public long getSendTime() {
        return this.sendTime;
    }


    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }


    public long getReceiveTime() {
        return this.receiveTime;
    }


    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }


    public String getExtra() {
        return this.extra;
    }


    public void setExtra(String extra) {
        this.extra = extra;
    }


    public String getUid() {
        return this.uid;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }
}

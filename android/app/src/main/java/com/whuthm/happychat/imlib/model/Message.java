package com.whuthm.happychat.imlib.model;

import android.text.TextUtils;

import com.whuthm.happychat.util.StringUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Arrays;
import java.util.Objects;

/**
 * 消息实体
 *
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Message {

    @Id
    private String id;

    private long sid;

    private String type;

    private String conversationType;

    private String from;

    private String to;


    private String body;

    private long sendTime;
    private long receiveTime;

    private String attrs;

    private String extra;

    private boolean read;

    private int status;

    public Message() {

    }


    @Generated(hash = 1831186955)
    public Message(String id, long sid, String type, String conversationType,
            String from, String to, String body, long sendTime, long receiveTime,
            String attrs, String extra, boolean read, int status) {
        this.id = id;
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



    public String getId() {
        return id;
    }

    public void setId(String id) {
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
            return TextUtils.equals(id, ((Message) obj).id);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{id});
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
}

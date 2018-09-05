package com.whuthm.happychat.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "t_message")
public class Message {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "sid", unique = true)
    private long sid;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "from_who", nullable = false)
    private String from;
    @Column(name = "to_who", nullable = false)
    private String to;
    @Column(name = "conversation_type", nullable = false)
    private String conversationType;
    @Column(name = "body")
    private String body;
    @Column(name = "attributes")
    private String attributes;
    @Column(name = "extra")
    private String extra;
    @Column(name = "create_time", nullable = false)
    private long createTime;

    @Column(name = "update_time", nullable = false)
    private long updateTime;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private MentionedInfo mentionedInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public MentionedInfo getMentionedInfo() {
        return mentionedInfo;
    }

    public void setMentionedInfo(MentionedInfo mentionedInfo) {
        this.mentionedInfo = mentionedInfo;
    }
}

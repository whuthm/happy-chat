package com.whuthm.happychat.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "t_offline_message")
public class OfflineMessage {

    @Id
    @Column(name = "m_id")
    private String mid;

    @Column(name = "to")
    private String to;

    @OneToOne(fetch = FetchType.EAGER)
    private Message message;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

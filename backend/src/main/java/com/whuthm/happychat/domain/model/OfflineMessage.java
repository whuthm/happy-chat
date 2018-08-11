package com.whuthm.happychat.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "t_offline_message")
public class OfflineMessage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "to")
    private String to;

    @OneToOne(fetch = FetchType.EAGER)
    private Message message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

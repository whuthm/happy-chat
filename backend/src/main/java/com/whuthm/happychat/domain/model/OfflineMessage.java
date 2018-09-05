package com.whuthm.happychat.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "t_offline_message")
public class OfflineMessage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "to_who")
    private String to;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private Message message;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

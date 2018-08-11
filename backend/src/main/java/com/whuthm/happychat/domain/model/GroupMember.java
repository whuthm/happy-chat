package com.whuthm.happychat.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "t_group_member")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "user_name")
    private String username;
    @Column(name = "user_nick")
    private String userNick;
    @Column(name = "role")
    private int role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}

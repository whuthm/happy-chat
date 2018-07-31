package com.whuthm.happychat.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "t_group_member")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "user_id", nullable = false)
    private String user_id;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

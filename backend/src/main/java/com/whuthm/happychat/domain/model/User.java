package com.whuthm.happychat.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @Column(name = "id")
    private String id;

	@Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "nick")
    private String nick;

    @Column(name = "gender")
    private int gender;

    @Column(name = "avatar")
    private String avatar;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "salt", nullable = false)
	private String salt;

    @Column(name = "role", nullable = false)
    private int role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public enum Role {

        SuperAdmin(1),
        Admin(10001),
        Normal(20001);

        final int value;

        Role(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}

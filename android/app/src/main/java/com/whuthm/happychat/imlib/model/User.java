package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户实体
 *
 * Created by tanwei on 2018/8/15.
 */

@Entity
public class User {
    
    @Id
    private String id;
    
    private String nick;
    
    private String avatar;
    
    private int gender;

    @Generated(hash = 280302525)
    public User(String id, String nick, String avatar, int gender) {
        this.id = id;
        this.nick = nick;
        this.avatar = avatar;
        this.gender = gender;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}

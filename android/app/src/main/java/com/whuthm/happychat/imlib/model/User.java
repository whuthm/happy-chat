package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * 用户实体
 *
 * Created by tanwei on 2018/8/15.
 */

@Entity
public class User implements Serializable  {

    private static final long serialVersionUID = 6535994572436649430L;
    @Id
    private String id;

    private String name;
    
    private String nick;
    
    private String portraitUrl;
    
    private int gender;


    @Generated(hash = 284437546)
    public User(String id, String name, String nick, String portraitUrl,
            int gender) {
        this.id = id;
        this.name = name;
        this.nick = nick;
        this.portraitUrl = portraitUrl;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}

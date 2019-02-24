package com.whuthm.happychat.imlib.model;

import java.io.Serializable;

public class UserInfo implements Serializable  {
    private static final long serialVersionUID = -5709330578168546162L;
    private String id;
    private String name;
    private String portraitUrl;
    private int gender;

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

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}

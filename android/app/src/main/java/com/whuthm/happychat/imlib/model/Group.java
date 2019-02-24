package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 群组实体
 *
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Group implements Serializable {
    private static final long serialVersionUID = -949859878812387621L;
    @Id
    private String id;
    
    private String name;

    private String portraitUrl;
    
    /** 类型：GroupChat, Discussion */
    private String type;
    
    private String desc;
    
    private String creator;

    @Generated(hash = 180130277)
    public Group(String id, String name, String portraitUrl, String type,
            String desc, String creator) {
        this.id = id;
        this.name = name;
        this.portraitUrl = portraitUrl;
        this.type = type;
        this.desc = desc;
        this.creator = creator;
    }

    @Generated(hash = 117982048)
    public Group() {
    }
    
//    private Set<String> memberIds;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

}

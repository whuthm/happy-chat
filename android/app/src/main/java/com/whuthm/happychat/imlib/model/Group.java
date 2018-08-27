package com.whuthm.happychat.imlib.model;

import com.whuthm.happychat.data.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
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
    
    /** 类型：GroupChat, Discussion */
    private String type;
    
    private String desc;
    
    private String creator;
    
    @Convert(columnType = String.class, converter = StringConverter.class)
    private Set<String> userIds;
    
    @Transient
    private List<User> users;

    @Generated(hash = 908926262)
    public Group(String id, String name, String type, String desc, String creator,
            Set<String> userIds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.creator = creator;
        this.userIds = userIds;
    }

    @Generated(hash = 117982048)
    public Group() {
    }

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

    public Set<String> getUserIds() {
        return this.userIds;
    }

    public void setUserIds(Set<String> userIds) {
        this.userIds = userIds;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

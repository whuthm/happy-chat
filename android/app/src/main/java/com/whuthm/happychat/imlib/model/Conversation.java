package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.Arrays;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * 会话实体
 * 
 * Created by huangming on 18/07/2018.
 */


@Entity
public class Conversation  implements Serializable {

    private static final long serialVersionUID = 6636927818298424517L;
    @Id
    private String id;

    @Index
    @NotNull
    @Convert(columnType = String.class, converter = ConversationType.Converter.class)
    private ConversationType type;
    
    private long latestMessageId;

    @ToOne(joinProperty = "latestMessageId")
    private Message latestMessage;
    
    private long latestMessageTime;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 151466175)
    private transient ConversationDao myDao;
    @Generated(hash = 1231441308)
    private transient Long latestMessage__resolvedKey;


    public Conversation() {
        
    }

    @Generated(hash = 266679905)
    public Conversation(String id, @NotNull ConversationType type, long latestMessageId,
            long latestMessageTime) {
        this.id = id;
        this.type = type;
        this.latestMessageId = latestMessageId;
        this.latestMessageTime = latestMessageTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Conversation) {
            return id.equals(((Conversation) obj).id);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{id});
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public ConversationType getType() {
        return this.type;
    }
    public void setType(ConversationType type) {
        this.type = type;
    }

    public long getLatestMessageId() {
        return latestMessageId;
    }

    public void setLatestMessageId(long latestMessageId) {
        this.latestMessageId = latestMessageId;
    }

    @Keep
    public Message getLatestMessage() {
        return latestMessage;
    }

    @Keep
    public void setLatestMessage(Message latestMessage) {
        this.latestMessage = latestMessage;
    }

    public long getLatestMessageTime() {
        return latestMessageTime;
    }

    public void setLatestMessageTime(long latestMessageTime) {
        this.latestMessageTime = latestMessageTime;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1878162230)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConversationDao() : null;
    }


}

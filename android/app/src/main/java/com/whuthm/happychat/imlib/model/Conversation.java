package com.whuthm.happychat.imlib.model;

import com.whuthm.happychat.imlib.converter.ConversationNotificationStatusConverter;
import com.whuthm.happychat.imlib.converter.ConversationTypeConverter;

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
    @Convert(columnType = String.class, converter = ConversationTypeConverter.class)
    private ConversationType type;
    private String title;
    private String portraitUrl;
    private int unreadCount;

    private Long latestMessageId;

    @ToOne(joinProperty = "latestMessageId")
    private Message latestMessage;
    
    private long latestMessageTime;
    @Convert(columnType = Integer.class, converter = ConversationNotificationStatusConverter.class)
    private NotificationStatus notificationStatus;

    private boolean top;

    private String draft;
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

    @Generated(hash = 104694593)
    public Conversation(String id, @NotNull ConversationType type, String title, String portraitUrl,
            int unreadCount, Long latestMessageId, long latestMessageTime,
            NotificationStatus notificationStatus, boolean top, String draft) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.portraitUrl = portraitUrl;
        this.unreadCount = unreadCount;
        this.latestMessageId = latestMessageId;
        this.latestMessageTime = latestMessageTime;
        this.notificationStatus = notificationStatus;
        this.top = top;
        this.draft = draft;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Conversation) {
            return id.equals(((Conversation) obj).id);
        }
        return super.equals(obj);
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
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

    public Long getLatestMessageId() {
        return latestMessageId;
    }

    public void setLatestMessageId(Long latestMessageId) {
        this.latestMessageId = latestMessageId;
    }

    public long getLatestMessageTime() {
        return latestMessageTime;
    }

    public void setLatestMessageTime(long latestMessageTime) {
        this.latestMessageTime = latestMessageTime;
    }

    public  enum NotificationStatus {
        DO_NOT_DISTURB(0),
        NOTIFY(1);

        private final int value;

        NotificationStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static NotificationStatus from(int value) {
            for (NotificationStatus status : NotificationStatus.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            return null;
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPortraitUrl() {
        return this.portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean getTop() {
        return this.top;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1740249692)
    public Message getLatestMessage() {
        Long __key = this.latestMessageId;
        if (latestMessage__resolvedKey == null || !latestMessage__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MessageDao targetDao = daoSession.getMessageDao();
            Message latestMessageNew = targetDao.load(__key);
            synchronized (this) {
                latestMessage = latestMessageNew;
                latestMessage__resolvedKey = __key;
            }
        }
        return latestMessage;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 952483817)
    public void setLatestMessage(Message latestMessage) {
        synchronized (this) {
            this.latestMessage = latestMessage;
            latestMessageId = latestMessage == null ? null : latestMessage.getId();
            latestMessage__resolvedKey = latestMessageId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1878162230)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConversationDao() : null;
    }

}

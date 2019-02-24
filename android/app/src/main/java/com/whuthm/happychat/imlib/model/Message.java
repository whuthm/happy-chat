package com.whuthm.happychat.imlib.model;

import android.support.annotation.NonNull;

import com.whuthm.happychat.imlib.converter.ConversationTypeConverter;
import com.whuthm.happychat.imlib.converter.MessageDirectionConverter;
import com.whuthm.happychat.imlib.converter.MessageReceivedStatusConverter;
import com.whuthm.happychat.imlib.converter.MessageSentStatusConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import java.io.Serializable;
import java.util.Arrays;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * 消息实体
 *
 * Created by huangming on 18/07/2018.
 */

@Entity
public class Message implements Serializable {

    private static final long serialVersionUID = 8769663248677565606L;
    /**
     * 客戶端生成uuid传递给服务端，用于校验服务器已送达
     */
    @Id
    private String id;

    @Convert(columnType = String.class, converter = MessageDirectionConverter.class)
    private Direction direction;

    /**
     * 服务端生成传递给客户端，客户端可以根据此字段判断消息已读（PC和phone共存时）
     */
    private Long sid;

    @NotNull
    private String type;

    @NotNull
    @Convert(columnType = String.class, converter = ConversationTypeConverter.class)
    private ConversationType conversationType;

    /**
     * 建索引，快速查询某一会话的历史消息
     */
    @Index
    @NonNull
    private String conversationId;

    @NonNull
    private String senderUserId;


    private String body;

    @Transient
    private MessageBody bodyObject;

    private long sendTime;
    private long receiveTime;

    private String attrs;

    private String extra;

    @Convert(columnType = String.class, converter = MessageSentStatusConverter.class)
    private SentStatus sentStatus;

    @Convert(columnType = Integer.class, converter = MessageReceivedStatusConverter.class)
    private ReceivedStatus receivedStatus;


    @Generated(hash = 421227807)
    public Message(String id, Direction direction, Long sid, @NotNull String type,
            @NotNull ConversationType conversationType, @NotNull String conversationId,
            @NotNull String senderUserId, String body, long sendTime, long receiveTime, String attrs,
            String extra, SentStatus sentStatus, ReceivedStatus receivedStatus) {
        this.id = id;
        this.direction = direction;
        this.sid = sid;
        this.type = type;
        this.conversationType = conversationType;
        this.conversationId = conversationId;
        this.senderUserId = senderUserId;
        this.body = body;
        this.sendTime = sendTime;
        this.receiveTime = receiveTime;
        this.attrs = attrs;
        this.extra = extra;
        this.sentStatus = sentStatus;
        this.receivedStatus = receivedStatus;
    }

    @Generated(hash = 637306882)
    public Message() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
        this.bodyObject = MessageBody.decode(this);
    }

    public String getAttrs() {
        return this.attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public SentStatus getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(SentStatus sentStatus) {
        this.sentStatus = sentStatus;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ReceivedStatus getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(ReceivedStatus receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public Long getSid() {
        return this.sid;
    }


    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @NonNull
    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(@NonNull String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public ConversationType getConversationType() {
        return this.conversationType;
    }


    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public long getSendTime() {
        return this.sendTime;
    }


    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }


    public long getReceiveTime() {
        return this.receiveTime;
    }


    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }


    public String getExtra() {
        return this.extra;
    }


    public void setExtra(String extra) {
        this.extra = extra;
    }


    public String getId() {
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public MessageBody getBodyObject() {
        if (bodyObject == null) {
            bodyObject = MessageBody.decode(this);
        }
        return bodyObject;
    }

    public void setBodyObject(MessageBody bodyObject) {
        this.bodyObject = bodyObject;
        this.body = MessageBody.encode(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message o = (Message) obj;
            return id == o.id || (id != null && id.equals(o.id));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Message(" + id + ")";
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new String[]{id});
    }

    public enum SentStatus {
        SENDING,
        FAILED,
        SENT,
    }

    public enum Direction {
        SEND,
        RECEIVE,
    }

    public static class ReceivedStatus {

        private int flag;

        public static final int FLAG_READ = 1;
        public static final int FLAG_LISTENED = 2;
        //public static final int FLAG_DOWNLOADED = 4;

        public ReceivedStatus(int flag) {
            this.flag = flag;
        }


        public int getFlag() {
            return flag;
        }

        public void markAsRead() {
            this.flag = this.flag | FLAG_READ;
        }

        public boolean isRead() {
            return (flag & FLAG_READ) > 0;
        }

        public boolean isListened() {
            return (flag & FLAG_LISTENED) > 0;
        }

        public void markAsListened() {
            this.flag = this.flag | FLAG_LISTENED;
        }

    }


    public static class ReceivedStatusConverter implements PropertyConverter<ReceivedStatus, Integer> {

        @Override
        public ReceivedStatus convertToEntityProperty(Integer databaseValue) {
            return new ReceivedStatus(databaseValue != null ? databaseValue : 0);
        }

        @Override
        public Integer convertToDatabaseValue(ReceivedStatus entityProperty) {
            return entityProperty.flag;
        }
    }

}

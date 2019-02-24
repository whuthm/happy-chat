package com.whuthm.happychat.imlib.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whuthm.happychat.imlib.model.message.ImageMessageBody;
import com.whuthm.happychat.imlib.model.message.TextMessageBody;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MessageBody implements Serializable  {

    private UserInfo userInfo;

    private static final long serialVersionUID = -5801796451327916354L;

    private static final MessageBody UNKNOWN = new MessageBody();

    private final static Map<String, Class<? extends MessageBody>> BODY_CLASSES = new HashMap<>();
    private final static Map<String, MessageTag> TAGS = new HashMap<>();

    static {
        registerMessageBody(ImageMessageBody.class);
        registerMessageBody(TextMessageBody.class);
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public static void registerMessageBody(Class<? extends MessageBody> bodyClass) {
        MessageTag tag = bodyClass.getAnnotation(MessageTag.class);
        if (tag == null) {
            throw new RuntimeException("MessageTag not def MessageBody type");
        }
        String messageType = tag.type();
        TAGS.put(messageType, tag);
        BODY_CLASSES.put(messageType, bodyClass);
    }

    public static boolean isCounted(String messageType) {
        MessageTag tag = TAGS.get(messageType);
        return tag != null && (tag.flag() & MessageTag.FLAG_COUNTED) == MessageTag.FLAG_COUNTED;
    }

    public static boolean isPersisted(String messageType) {
        MessageTag tag = TAGS.get(messageType);
        return tag == null || (tag.flag() & MessageTag.FLAG_PERSISTED) == MessageTag.FLAG_PERSISTED;
    }

    public static MessageBody decode(Message message) {
        Class<? extends MessageBody> bodyClass = BODY_CLASSES.get(message.getType());
        if (bodyClass == null) {
            return UNKNOWN;
        }
        MessageBody body = null;
        try {
            body =  JSONObject.parseObject(message.getBody(), bodyClass);
        } catch (Exception ignored) {
        }
        if (body == null) {
            try {
                body = bodyClass.newInstance();
            } catch (Exception ignored) {
            }
        }
        return body;
    }

    public static String encode(Message message) {
        return JSON.toJSONString(message.getBodyObject());
    }

}

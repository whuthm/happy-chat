package com.whuthm.happychat.imlib.model;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by huangming on 2018/9/6.
 */

public enum ConversationType {
    PRIVATE("private"),
    GROUP("GroupChat", true),
    CHAT_ROOM("chat_room", true),
    DISCUSSION("discussion", true),
    SYSTEM("system", false),
    PUSH_SERVICE("push_service", false),
    CUSTOM_SERVICE("custom_service", false),
    PUBLIC_SERVICE("public_service", false);

    private final String value;
    private final boolean multiUser;


    ConversationType(String value) {
        this(value, false);
    }


    ConversationType(String value, boolean multiUser) {
        this.value = value;
        this.multiUser = multiUser;
    }

    public boolean isMultiUser() {
        return multiUser;
    }

    public String getValue() {
        return value;
    }


    public static ConversationType from(String value) {
        for (ConversationType type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public static class Converter implements PropertyConverter<ConversationType, String> {
        @Override
        public ConversationType convertToEntityProperty(String databaseValue) {
            return from(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(ConversationType entityProperty) {
            return entityProperty.getValue();
        }
    }
}

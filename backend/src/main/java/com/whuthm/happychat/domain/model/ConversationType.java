package com.whuthm.happychat.domain.model;

public enum ConversationType {

    SingleChat,
    GroupChat;

    public static ConversationType from(String value) {
        for (ConversationType type : ConversationType.values()) {
            if (type.name().equals(value)) {
                return type;
            }
        }
        return null;
    }

}

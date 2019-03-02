package com.whuthm.happychat.imlib.vo;

public class ConversationProperties {

    public static final int FLAG_NONE = 0x00000000;
    public static final int FLAG_UNREAD_COUNT = 0x00000001;
    public static final int FLAG_LATEST_MESSAGE = 0x00000002;
    public static final int FLAG_TOP = 0x00000004;
    public static final int FLAG_DRAFT = 0x00000008;
    public static final int FLAG_NOTIFYCATION_STATUS = 0x00000010;

    private int flag = FLAG_NONE;

    public ConversationProperties() {
    }

    public void addUnreadCountProperty() {
        flag |= FLAG_LATEST_MESSAGE;
    }

    public void addLatestMessageProperty() {
        flag |= FLAG_LATEST_MESSAGE;
    }

    public boolean containsUnreadCountProperty() {
        return (flag & FLAG_UNREAD_COUNT) != 0;
    }

    public boolean containsLatestMessageProperty() {
        return (flag & FLAG_LATEST_MESSAGE) != 0;
    }

    public boolean containsProperties() {
        return flag != FLAG_NONE;
    }

}

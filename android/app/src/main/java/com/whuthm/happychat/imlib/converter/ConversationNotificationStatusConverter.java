package com.whuthm.happychat.imlib.converter;

import com.whuthm.happychat.imlib.model.Conversation;

import org.greenrobot.greendao.converter.PropertyConverter;

public class ConversationNotificationStatusConverter implements PropertyConverter<Conversation.NotificationStatus, Integer> {
    @Override
    public Conversation.NotificationStatus convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        return Conversation.NotificationStatus.from(databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(Conversation.NotificationStatus entityProperty) {
        return entityProperty.getValue();
    }
}

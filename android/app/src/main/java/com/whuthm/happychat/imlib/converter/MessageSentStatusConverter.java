package com.whuthm.happychat.imlib.converter;

import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.greendao.converter.PropertyConverter;

public class MessageSentStatusConverter  implements PropertyConverter<Message.SentStatus, String> {

    @Override
    public Message.SentStatus convertToEntityProperty(String databaseValue) {
        try {
            return Message.SentStatus.valueOf(databaseValue);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseValue(Message.SentStatus entityProperty) {
        return entityProperty.name();
    }
}

package com.whuthm.happychat.imlib.converter;

import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.greendao.converter.PropertyConverter;

public class MessageReceivedStatusConverter  implements PropertyConverter<Message.ReceivedStatus, Integer> {

    @Override
    public Message.ReceivedStatus convertToEntityProperty(Integer databaseValue) {
        return new Message.ReceivedStatus(databaseValue != null ? databaseValue : 0);
    }

    @Override
    public Integer convertToDatabaseValue(Message.ReceivedStatus entityProperty) {
        return entityProperty.getFlag();
    }
}
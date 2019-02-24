package com.whuthm.happychat.imlib.converter;

import com.whuthm.happychat.imlib.model.Message;

import org.greenrobot.greendao.converter.PropertyConverter;

public class MessageDirectionConverter implements PropertyConverter<Message.Direction, String> {

    @Override
    public Message.Direction convertToEntityProperty(String databaseValue) {
        try {
            return Message.Direction.valueOf(databaseValue);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String convertToDatabaseValue(Message.Direction entityProperty) {
        return entityProperty.name();
    }
}

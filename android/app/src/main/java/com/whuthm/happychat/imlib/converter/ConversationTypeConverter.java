package com.whuthm.happychat.imlib.converter;

import com.whuthm.happychat.imlib.model.ConversationType;

import org.greenrobot.greendao.converter.PropertyConverter;

public class ConversationTypeConverter implements PropertyConverter<ConversationType, String> {
    @Override
    public ConversationType convertToEntityProperty(String databaseValue) {
        return ConversationType.from(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(ConversationType entityProperty) {
        return entityProperty.getValue();
    }
}

package com.whuthm.happychat.data;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Set<String>在greendao的存储转换类
 * 
 * Created by tanwei on 2018/8/15.
 */

public class StringConverter implements PropertyConverter<Set<String>, String> {
    @Override
    public Set<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        else {
            return new HashSet<>(Arrays.asList(databaseValue.split(",")));
        }
    }
    
    @Override
    public String convertToDatabaseValue(Set<String> entityProperty) {
        if (entityProperty == null) {
            return null;
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (String link : entityProperty) {
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}

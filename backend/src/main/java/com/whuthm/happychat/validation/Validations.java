package com.whuthm.happychat.validation;

import com.whuthm.happychat.data.MessageProtos;
import com.whuthm.happychat.domain.model.Message;
import com.whuthm.happychat.exception.InvalidObjectException;

import java.util.HashMap;
import java.util.Map;

public class Validations {

   private final static Map<Class<?>, Validation<?>> VALIDATIONS = new HashMap<>();

   static {
       VALIDATIONS.put(MessageProtos.MessageBean.class, new MessageBeanValidation());
       VALIDATIONS.put(Message.class, new MessageValidation());
   }

   @SuppressWarnings("unchecked")
    public static  <T> void requireValid(T obj) throws InvalidObjectException {
        Validation<T> validation = (Validation<T>) VALIDATIONS.get(obj.getClass());
        if (validation != null && !validation.isValid(obj)) {
            throw new InvalidObjectException(obj);
        }
    }

}

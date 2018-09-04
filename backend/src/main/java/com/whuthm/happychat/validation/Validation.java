package com.whuthm.happychat.validation;


import com.whuthm.happychat.exception.InvalidObjectException;

import java.util.HashMap;
import java.util.Map;

public interface Validation<T> {

    Map<Class<?>, Validation<?>> VALIDATIONS = new HashMap<>();

    default void requireValid(T obj) throws InvalidObjectException {
        if (!isValid(obj)) {
            throw new InvalidObjectException(obj);
        }
    }

    boolean isValid(T obj);

    @SuppressWarnings("unchecked")
    static <T> Validation<T> of(Class<T> clazz) {
        Validation<?> validation = VALIDATIONS.get(clazz);
        if (validation == null) {
            VALIDATIONS.put(clazz, validation);

            try {
                validation = (Validation<?>) clazz.newInstance();

            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return (Validation<T>) validation;
    }
}

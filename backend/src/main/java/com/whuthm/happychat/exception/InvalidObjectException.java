package com.whuthm.happychat.exception;

public class InvalidObjectException extends Exception {

    private final Object obj;

    public InvalidObjectException(Object obj) {
        this(obj, toObjectString(obj));
    }

    public InvalidObjectException(Object obj, String message) {
        super(message);
        this.obj = obj;
    }

    private static String toObjectString(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    public Object getObject() {
        return obj;
    }

    public boolean isNull() {
        return getObject() == null;
    }

}

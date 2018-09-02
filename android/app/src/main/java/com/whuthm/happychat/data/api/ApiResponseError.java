package com.whuthm.happychat.data.api;

public class ApiResponseError extends Exception {

    private static final long serialVersionUID = 4844671892902871465L;
    private final int code;
    private final String message;
    private final Object response;

    public ApiResponseError(int code, String message, Object response) {
        this.code = code;
        this.message = message;
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getResponse() {
        return response;
    }
}
